package coursework.gui;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class PopUpBox {

    public static void display(String title, String message){

        Stage popUp = new Stage();

        //Block the events to other windows
        popUp.initModality(Modality.APPLICATION_MODAL);
        popUp.setTitle(title);
        popUp.setMinWidth(250);

        //Set up the label for the message and the close button
        Label label = new Label();
        label.setText(message);
        label.setAlignment(Pos.CENTER);
        Button closeButton = new Button("Close this window");
        closeButton.setOnAction(e -> popUp.close());

        //setting the layout for the pop up
        VBox vBox = new VBox(10);
        vBox.getChildren().addAll(label, closeButton);
        vBox.setAlignment(Pos.CENTER);

        //Display the pop up
        Scene scene = new Scene(vBox);
        popUp.setMinHeight(200);
        popUp.setMinWidth(300);
        popUp.setScene(scene);
        popUp.showAndWait();

    }
}