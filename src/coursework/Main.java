package coursework;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {


    /*

    VERY IMPORTANT PLEASE TAKE NOT FOR THIS TO WORK:
    PLEASE USE SDK 1.8 AND THE PROJECT LANGUAGE OF 8

    ALSO ENSURE THAT THE org.json JAR IS INCLUDED IN THE LIBRARY

     */

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("gui/MainGui.fxml"));
        primaryStage.setTitle("Project Management System");
        primaryStage.setScene(new Scene(root, 700, 400));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
