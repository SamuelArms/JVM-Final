package coursework.gui;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.json.JSONArray;
import org.json.JSONObject;
import coursework.transfer.TransferReaderWriter;

import java.net.URL;
import java.util.ResourceBundle;


public class ProjectCreationController  implements Initializable {

    @FXML
    private TextField projectTitleField;
    @FXML
    private Button submitButton;

    private TransferReaderWriter transferReaderWriter = new TransferReaderWriter();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //Clear the data transfer from the previous session
        transferReaderWriter.writeTransfer("");
    }

    public void submit() {
        if (projectTitleField.getText().equals("")){
            PopUpBox.display("Creation Error", "Please enter a project title to create a project");
        } else {
            // Make a JSON object of the project so that the project can be transferred from scene to scene
            JSONArray taskArray = new JSONArray();
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("projectTitle", projectTitleField.getText());
            jsonObject.put("tasksAssigned", taskArray).toString();
            jsonObject.put("progress", 0);
            String transferString = jsonObject.toString();
            transferReaderWriter.writeTransfer(transferString);
            // Return the main GUI
            Stage stage = (Stage) submitButton.getScene().getWindow();
            stage.close();
        }
    }
}

