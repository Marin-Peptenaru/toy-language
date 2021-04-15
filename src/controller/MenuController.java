package controller;

import exception.MyException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import model.program.ProgramState;
import model.program.Statement;
import repo.ProgramRepository;

import java.io.IOException;
import java.net.URL;
import java.util.Collections;
import java.util.ResourceBundle;

public class MenuController implements Initializable {
    public ListView<ProgramState> programs;
    public ListView<Statement> statements;
    public Button runButton;

    private ObservableList<Statement> selectedProgramStatements = FXCollections.observableArrayList();

    private class ProgramTextCell extends ListCell<ProgramState>{
        private Text text = new Text();
        public ProgramTextCell(){
            super();
        }

        @Override
        protected void updateItem(ProgramState program, boolean emptyCell) {
            super.updateItem(program, emptyCell);
            if (program != null && !emptyCell) { // <== test for null item and empty parameter
                text.setText("Program -" + program.getId());
                setGraphic(text);
            } else {
                setGraphic(null);
            }
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        statements.setItems(selectedProgramStatements);
        programs.setCellFactory(lView -> new ProgramTextCell());
        programs.getSelectionModel().selectedItemProperty().addListener((bean, oldP, newP) -> {
            if(newP!=null){
                selectedProgramStatements.setAll(newP.getExecutionStack().statementStackProperty());
                Collections.reverse(selectedProgramStatements);
            }
        });

    }

    public void setProgramChoices(ObservableList<ProgramState> programs){
        this.programs.setItems(programs);
    }

    public String showFileDialog() throws IOException {
        Stage dialogWindow = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/logFileDialog.fxml"));
        Parent root = loader.load();
        dialogWindow.setTitle("Program log file dialog");
        dialogWindow.setScene(new Scene(root, 250, 150));
        dialogWindow.initModality(Modality.APPLICATION_MODAL);
        LogFileDialog dialog = loader.getController();
        dialog.setWindow(dialogWindow);
        dialogWindow.showAndWait();
        return dialog.nameField.getText();
    }

    public void runClick() throws IOException {
        ProgramState initialState = programs.getSelectionModel().getSelectedItem();
        if(initialState == null)
            return;
        String fileName = showFileDialog();
        if(fileName.length() == 0)
            return;

        ProgramRepository repo = new ProgramRepository(ProgramFactory.getProgram(initialState.getId()),fileName);
        Stage executionWindow = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/executionWindow.fxml"));
        Parent root = loader.load();
        executionWindow.setTitle("Program log file dialog");
        executionWindow.setScene(new Scene(root, 1200, 1000));
        ExecutionController exec = loader.getController();
        exec.setRepo(repo);

        try{
            exec.staticTypeCheck();
            executionWindow.show();
        }catch(MyException e){
            ErrorBox.display(e);
        }

    }


}
