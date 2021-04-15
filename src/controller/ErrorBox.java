package controller;

import exception.MyException;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Modality;
import javafx.stage.Stage;

public final class ErrorBox {
    private static Stage window;
    private static VBox layout;
    private static Text errorMessage;

    static{
        window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setResizable(false);
        layout = new VBox();
        errorMessage = new Text();
        errorMessage.setTextAlignment(TextAlignment.CENTER);
        errorMessage.setFill(Color.RED);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(20,20,20,20));
        layout.getChildren().add(errorMessage);
        window.setScene(new Scene(layout));
    }

    public static void display(Exception e){
        window.setTitle(e.getClass().getName());
        errorMessage.setText(e.getMessage());
        window.show();
    }

}
