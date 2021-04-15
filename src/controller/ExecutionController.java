package controller;
import exception.MyException;
import exception.SymbolException;
import exception.TypeException;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.binding.ListBinding;
import javafx.beans.binding.StringBinding;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.program.ProgramState;
import model.program.Statement;
import model.program.filetable.FileTableInterface;
import model.program.heap.HeapInterface;
import model.program.output.OutputInterface;
import model.program.synchronization.Lock;
import model.program.typecheck.TypeTable;
import model.program.typecheck.TypeTableInterface;
import model.values.StringValue;
import model.values.Value;
import repo.ProgramRepository;

import java.net.URL;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;

public class ExecutionController implements Initializable {

    @FXML
    public ListView<Statement> stackView;
    @FXML
    public TableView<Map.Entry<String, Value<?>>> symbolTableView;
    @FXML
    public TableColumn<Map.Entry<String, Value<?>>,String> varNameCol;
    @FXML
    public TableColumn<Map.Entry<String, Value<?>>,Value<?>> varValueCol;
    @FXML
    public ListView<StringValue> fileListView;
    @FXML
    public TableView<Map.Entry<Integer,Value<?>>> heapView;
    @FXML
    public TableColumn<Map.Entry<Integer,Value<?>>,String> heapAddrCol;
    @FXML
    public TableColumn<Map.Entry<Integer,Value<?>>,Value<?>> heapValueCol;
    @FXML
    public ListView<ProgramState> threadView;
    @FXML
    public ListView<String> outputView;
    @FXML
    public Button runButton;
    @FXML
    public Label activeThreadsCount;
    @FXML
    public TableView<Map.Entry<Integer, Lock>> lockTable;
    @FXML
    public TableColumn<Map.Entry<Integer, Lock>, String> locationCol;
    @FXML
    public TableColumn<Map.Entry<Integer, Lock>, String> stateCol;


    private ObservableList<ProgramState> threadList = FXCollections.observableArrayList();

    private boolean printFlag = false;
    private ProgramRepository programs;
    private  ExecutorService executor = Executors.newFixedThreadPool(2);
    private HeapInterface heap;
    private OutputInterface output;
    private FileTableInterface files;

    private class ThreadTextCell extends ListCell<ProgramState>{
        private Text text = new Text();
        public ThreadTextCell(){
            super();
        }

        @Override
        protected void updateItem(ProgramState thread, boolean emptyCell) {
            super.updateItem(thread, emptyCell);
            if (thread != null && !emptyCell) { // <== test for null item and empty parameter
                text.setText("Thread " + thread.getId() + ": " + (thread.isFinished()? "Finished" : "Active"));
                setGraphic(text);
            } else {
                setGraphic(null);
            }
        }
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        threadView.setItems(threadList);
        threadView.setCellFactory(tView -> new ThreadTextCell());
        stackView.setItems(FXCollections.observableArrayList());

        initColumns();
        initViews();

        threadView.getSelectionModel().selectedItemProperty().addListener((o, oldT, newT) ->{
            if(newT != null)
            {
                stackView.getItems().setAll(newT.getExecutionStack().statementStackProperty());
                Collections.reverse(stackView.getItems());
                symbolTableView.getItems().setAll(newT.getSymbolTable().variablesProperty().entrySet());
            }
        });

        runButton.setOnAction(e -> {
            try {
                oneStepExecution();
                if(threadView.getItems().isEmpty())
                    runButton.setDisable(true);
                setViews(getSelectedThread());

            } catch (MyException exception) {
                ErrorBox.display(exception);
            }
        });
    }

    public void initColumns(){
        varNameCol.setCellValueFactory(tableEntry -> {
            Map.Entry<String, Value<?>> entryValue = tableEntry.getValue();
            return new SimpleStringProperty(entryValue.getKey());
        });
        varValueCol.setCellValueFactory(tableEntry -> {
            Map.Entry<String, Value<?>> entryValue = tableEntry.getValue();
            return new SimpleObjectProperty<>(entryValue.getValue());
        });
        heapAddrCol.setCellValueFactory(tableEntry -> {
            Map.Entry<Integer, Value<?>> entryValue = tableEntry.getValue();
            return new SimpleStringProperty("@" + entryValue.getKey());
        });
        heapValueCol.setCellValueFactory(tableEntry -> {
            Map.Entry<Integer, Value<?>> entryValue = tableEntry.getValue();
            return new SimpleObjectProperty<>(entryValue.getValue());
        });
        locationCol.setCellValueFactory(lockEntry -> {
            Map.Entry<Integer, Lock> lockValue = lockEntry.getValue();
            return new SimpleStringProperty("@" + lockValue.getKey());
        });
        stateCol.setCellValueFactory(lockEntry -> {
            Map.Entry<Integer, Lock> lockValue = lockEntry.getValue();
            return new SimpleStringProperty( lockValue.getValue().toString());
        });
    }

    public void setRepo(ProgramRepository repo){
        programs = repo;
        threadList.setAll(programs.programListProperty());
        ProgramState initialState = repo.getProgramList().get(0);
        heap = initialState.getHeap();
        output = initialState.getOutput();
        files = initialState.getFileTable();

        activeThreadsCount.textProperty().bind(new StringBinding(){
            {super.bind(programs.programListProperty());}

            @Override
            protected String computeValue() {
                return "Number of active threads: " + programs.programListProperty().size();
            }
        });

        runButton.disableProperty().bind(repo.programListProperty().emptyProperty());

    }
    private void initViews(){
        stackView.setItems(FXCollections.observableArrayList());
        heapView.setItems(FXCollections.observableArrayList());
        outputView.setItems(FXCollections.observableArrayList());
        symbolTableView.setItems(FXCollections.observableArrayList());
        fileListView.setItems(FXCollections.observableArrayList());
    }
    private void bindSharedThreadData(){
        outputView.itemsProperty().bind(output.outputProperty());

        fileListView.itemsProperty().bind(new ListBinding<>(){

            {super.bind(files.filesProperty());}

            @Override
            protected ObservableList<StringValue> computeValue() {
                return FXCollections.observableArrayList(files.filesProperty().keySet());
            }
        });

        heapView.itemsProperty().bind(new ListBinding<>(){

            {super.bind(heap.memoryProperty());}

            @Override
            protected ObservableList<Map.Entry<Integer, Value<?>>> computeValue() {
                return FXCollections.observableArrayList(heap.memoryProperty().entrySet());
            }
        });


        threadView.getSelectionModel().selectedItemProperty().addListener((o, oldT, newT) ->{
            if(newT != null)
                bindThreadData(newT);
            else{
                if(symbolTableView.itemsProperty().isBound()){
                    symbolTableView.itemsProperty().unbind();
                }
            }
        });

    }

    private void bindThreadData(ProgramState thread){
        stackView.getItems().setAll(thread.getExecutionStack().statementStackProperty());
        Collections.reverse(stackView.getItems());

        if(symbolTableView.itemsProperty().isBound())
            symbolTableView.itemsProperty().unbind();

        symbolTableView.itemsProperty().bind(new ListBinding<>(){
            {super.bind(thread.getSymbolTable().variablesProperty());}

            @Override
            protected ObservableList<Map.Entry<String, Value<?>>> computeValue() {
                return FXCollections.observableArrayList(thread.getSymbolTable().variablesProperty().entrySet());
            }
        });

    }

    private ProgramState getSelectedThread(){
        return threadView.getSelectionModel().getSelectedItem();
    }

    private void setViews(ProgramState thread){
        if(thread != null){
            stackView.getItems().setAll(thread.getExecutionStack().statementStackProperty());
            Collections.reverse(stackView.getItems());
            symbolTableView.getItems().setAll(thread.getSymbolTable().variablesProperty().entrySet());
        }
        outputView.getItems().setAll(output.outputProperty());
        fileListView.getItems().setAll(files.filesProperty().keySet());
        heapView.getItems().setAll(heap.memoryProperty().entrySet());
        lockTable.getItems().setAll(thread.getLocks().locksProperty().entrySet());
    }
    public void setPrint(){
        printFlag = true;
    }

    public void unsetPrint(){
        printFlag = false;
    }

    private List<ProgramState> getUnfinishedThreads(List<ProgramState> threads){
        return threads.stream().filter(t -> !t.isFinished()).collect(Collectors.toList());
    }

    private void oneStepForAllThreads(List<ProgramState> threads) throws MyException{
        threads.forEach(p -> {
            try {
                programs.logProgramState(p);
            } catch (MyException e) {
                ErrorBox.display(e);
            }
        });
        List<Callable<ProgramState>> callList = threads.stream()
                .map(p -> (Callable<ProgramState>)(p::executeStatement)).collect(Collectors.toList());
        try{
            List<ProgramState> newThreads = executor.invokeAll(callList).stream()
                    .map((Future<ProgramState> f) ->
                    {
                        ProgramState p = null;
                        try{
                            p = f.get();
                        }catch(ExecutionException e){
                            ErrorBox.display(e);
                        }
                        catch(InterruptedException e){
                            ErrorBox.display(e);
                        } return p;
                    } ).filter(Objects::nonNull).collect(Collectors.toList());
            threadList.addAll(newThreads);
            threads.addAll(newThreads);
            /*finished threads are remove here, but are those finished in the PREVIOUS iteration of the loop in
             completeExecution, because here you only add the newly generated threads
             the completed ones are removed from the threads list only after this method is called in the loop
             and are removed from the repository only here where you change the programs list. */
            programs.setProgramList(threads);
            threads.forEach(p -> {
                try {
                    programs.logProgramState(p);
                } catch (MyException e) {
                    ErrorBox.display(e);
                }
            });
        }catch(InterruptedException e){
            ErrorBox.display(e);
        }
    }

    public void oneStepExecution() throws MyException{
        List<ProgramState> threads = getUnfinishedThreads(programs.getProgramList());

        if(threads.size() > 0){
            oneStepForAllThreads(threads);
            collectGarbage();
            threads = getUnfinishedThreads(programs.getProgramList());
        }
        else{
            executor.shutdownNow();
        }

        programs.setProgramList(threads);
    }
    public void completeExecution() throws  MyException{
        List<ProgramState> threads = getUnfinishedThreads(programs.getProgramList());

        while(threads.size() > 0){
            oneStepForAllThreads(threads);
            collectGarbage();
            threads = getUnfinishedThreads(programs.getProgramList());
        }

        executor.shutdownNow();
        programs.setProgramList(threads);
    }

    private void collectGarbage(){
        Set<Integer> referencedAddresses = new HashSet<>();
        List<ProgramState> threads = getUnfinishedThreads(programs.getProgramList());
        threads.forEach(t -> referencedAddresses.addAll(t.getSymbolTable().getAddresses()));
        heap.collectGarbage(referencedAddresses);
    }

    public void staticTypeCheck() throws SymbolException, TypeException {
        TypeTableInterface typeTable = new TypeTable();
        ProgramState currentProgram = programs.getProgramList().iterator().next();
        currentProgram.getExecutionStack().typeCheck(typeTable);
    }






}
