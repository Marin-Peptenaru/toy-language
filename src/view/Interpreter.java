package view;


import controller.MenuController;
import controller.ProgramFactory;
import exception.MyException;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.expressions.*;
import model.program.*;
import model.program.exestack.ExecutionStack;
import model.program.exestack.ExecutionStackInterface;
import model.program.filetable.FileTable;
import model.program.heap.Heap;
import model.program.output.Output;
import model.program.symbdict.SymbolTable;
import model.values.*;

import java.io.IOException;


public class Interpreter extends Application {
    private static ObservableList<ProgramState> programs = FXCollections.observableArrayList();


    public static void main(String[] args)  {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws MyException, IOException {
        Stage window = new Stage();
        programs.addAll(ProgramFactory.getProgram1(), ProgramFactory.getProgram2(), ProgramFactory.getProgram3(),
                ProgramFactory.getProgram4(), ProgramFactory.getProgram5(), ProgramFactory.getProgram6(),
                ProgramFactory.getProgram7(), ProgramFactory.getProgram8(), ProgramFactory.getProgram9(),
                ProgramFactory.getProgram10());
        FXMLLoader loader = new FXMLLoader(getClass().getResource("menuWindow.fxml"));
        Parent root = loader.load();
        MenuController controller = loader.getController();
        controller.setProgramChoices(programs);
        Scene scene = new Scene(root, 800, 800);
        window.setScene(scene);
        window.show();
    }
}
