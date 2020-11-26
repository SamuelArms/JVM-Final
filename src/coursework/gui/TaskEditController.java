package coursework.gui;

import coursework.task.Task;
import coursework.task.TaskHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import coursework.transfer.TransferReaderWriter;

import java.net.URL;
import java.util.ResourceBundle;

public class TaskEditController implements Initializable {

    @FXML
    private TextArea infoArea;
    @FXML
    private Button submitButton;
    @FXML
    private TextField newDurationField;
    @FXML
    private TextField newProgressField;

    private TaskHandler taskHandler = new TaskHandler();
    private TransferReaderWriter transferReaderWriter = new TransferReaderWriter();


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
            PopUpBox.display("Edit error", "Please enter the new duration for the task");
        } else {
            try {
                Integer.parseInt(newDurationField.getText());
                // get the task from the transfer file
                Task taskPassed = taskHandler.createTaskFromTransferFile();
                // set the new duration of the task
                taskPassed.setDuration(Integer.parseInt(newDurationField.getText()));
                // change the task back into the string
                String saveString = taskHandler.getSaveString(taskPassed);
                transferReaderWriter.writeTransfer(saveString);
                // Close the GUI
                Stage stage = (Stage) submitButton.getScene().getWindow();
                stage.close();
            } catch (NumberFormatException e) {
                PopUpBox.display("Edit error", "progress has to be a number");
            }
        }
    }

    public void submitProgress(){
        // ensure the needed information is provided
        if (newProgressField.getText().equals("")) {
            // display popup with the error message
            PopUpBox.display("Edit error", "Please enter the new progress for the task");
        } else {
            try {
                Integer.parseInt(newProgressField.getText());
                if (Integer.parseInt(newProgressField.getText()) >= 0 && Integer.parseInt(newProgressField.getText()) <= 100) {
                    // get the task from the transfer file
                    Task taskPassed = taskHandler.createTaskFromTransferFile();
                    // set the new duration of the task
                    taskPassed.setProgress(Integer.parseInt(newProgressField.getText()));
                    // change the task back into the string
                    String saveString = taskHandler.getSaveString(taskPassed);
                    transferReaderWriter.writeTransfer(saveString);
                    // Close the GUI
                    Stage stage = (Stage) submitButton.getScene().getWindow();
                    stage.close();
                } else {
                    PopUpBox.display("Error","The integer must be between 0 and 100");
                }
            } catch(NumberFormatException e){
                PopUpBox.display("Edit error", "progress has to be a whole number between 0 - 100");
            }
        }
    }
}
