package coursework.gui;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;


public class ProjectCreationController  implements Initializable {

    @FXML
    private TextField projectTitleField;
    @FXML
    private Button submitButton;

    private FileWriter file;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
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

    public void submit() {
        if (projectTitleField.getText().equals("")){
            PopUpBox.display("Creation Error", "Please enter a project title to create a project");
        } else {
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
}

