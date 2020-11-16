package coursework.gui;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.FileWriter;
import java.io.IOException;


public class ProjectCreationController {

    @FXML
    private TextField projectTitleField;
    @FXML
    private Button submitButton;

    private FileWriter file;

    public void submit() {
        // Make a JSON object of the project so that the project can be transferred from scene to scene
        JSONArray taskArray = new JSONArray();
        JSONObject jsonString = new JSONObject();
        jsonString.put("projectTitle", projectTitleField.getText());
        jsonString.put("tasksAssigned", taskArray).toString();
        String tansferString = jsonString.toString();
        try {
            // Write the json object to the transfer file
            file = new FileWriter("src/coursework/data transfer.json");
            file.write(tansferString);
            file.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // Return the main GUI
        Stage stage = (Stage) submitButton.getScene().getWindow();
        stage.close();
    }
}

