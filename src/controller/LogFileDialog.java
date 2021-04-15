package controller;

import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class LogFileDialog {
    private Stage window;
    public TextField nameField;
    public Button cancelButton;
    public Button nextButton;


    public void setWindow(Stage w){
        window = w;
        window.setOnCloseRequest(e -> {
            if(e.getSource() != nextButton)
                nameField.clear();
        });
    }

    public void nextClick(){
        if (nameField.getText().length() > 0) {
            window.close();
        }
    }

    public void cancelClick(){
        window.close();
    }
}
