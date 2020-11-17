package coursework.gui;

import coursework.task.Task;
import coursework.task.TaskHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class TaskEditController implements Initializable {

    @FXML
    private TextArea infoArea;
    @FXML
    private Button submitButton;
    @FXML
    private TextField newDurationField;

    private TaskHandler taskHandler = new TaskHandler();
    private FileWriter file;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Task taskPassed = taskHandler.createTaskFromTransferFile();
        infoArea.setText(taskHandler.getDisplay(taskPassed));
        //Clear the data transfer from the previous session
        try {
            file = new FileWriter("src/coursework/data transfer.json");
            // write an empty line
            file.write("");
            file.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void submit (){
        Task taskPassed = taskHandler.createTaskFromTransferFile();
        taskPassed.setDuration(Integer.parseInt(newDurationField.getText()));
        String saveString = taskHandler.getSaveString(taskPassed);
        try {
            // Write the tasks to a transfer file
            file = new FileWriter("src/coursework/data transfer.json");
            file.write(saveString);
            file.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // Close the GUI
        Stage stage = (Stage) submitButton.getScene().getWindow();
        stage.close();
    }
}
