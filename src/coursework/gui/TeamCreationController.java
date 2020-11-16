package coursework.gui;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.FileWriter;
import java.io.IOException;

public class TeamCreationController {

    @FXML
    private TextField teamNameField;
    @FXML
    private TextField teamMemberOneField;
    @FXML
    private TextField teamMemberTwoField;
    @FXML
    private Button submitButton;

    private FileWriter file;

    public void submit() {

        // Make the team as a JSON object so that it can be saved to the transfer file
        JSONArray memberArray = new JSONArray();
        memberArray.put(teamMemberOneField.getText());
        memberArray.put(teamMemberTwoField.getText());
        String jsonString = new JSONObject()
                .put("teamTitle", teamNameField.getText())
                .put("members", memberArray).toString();
        try {
            // Write the team to the transfer file
            file = new FileWriter("src/coursework/data transfer.json");
            file.write(jsonString);
            file.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //Close this GUI
        Stage stage = (Stage) submitButton.getScene().getWindow();
        stage.close();
    }

}