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
        // get the task from the transfer file
        Task taskPassed = taskHandler.createTaskFromTransferFile();
        // set the info area to display the passed task
        infoArea.setText(taskHandler.getDisplay(taskPassed));
    }

    public void submit () {
        // ensure the needed information is provided
        if (newDurationField.getText().equals("")) {
            // display popup with the error message
            PopUpBox.display("Edit error", "Please enter the new duration for thwe task");
        } else {
            // get the task from the transfer file
            Task taskPassed = taskHandler.createTaskFromTransferFile();
            // set the new duration of the task
            taskPassed.setDuration(Integer.parseInt(newDurationField.getText()));
            // change the task back into the string
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
}
