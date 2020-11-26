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

public class TeamCreationController  implements Initializable {

    @FXML
    private TextField teamNameField;
    @FXML
    private TextField teamMemberOneField;
    @FXML
    private TextField teamMemberTwoField;
    @FXML
    private Button submitButton;

    private TransferReaderWriter transferReaderWriter =  new TransferReaderWriter();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //Clear the data transfer from the previous session
        transferReaderWriter.writeTransfer("");
    }

    public void submit() {
        // ensure that all the needed information is provided
        if (teamNameField.getText().equals("") || teamMemberOneField.getText().equals("") || teamMemberTwoField.getText().equals("")){
            // display a popup if not
            PopUpBox.display("Creation Error", "Please ensure all fields are filled out " +
                    "As all fields are needed to create a team");
        } else {

            // Make the team as a JSON object so that it can be saved to the transfer file
            JSONArray memberArray = new JSONArray();
            memberArray.put(teamMemberOneField.getText());
            memberArray.put(teamMemberTwoField.getText());
            String jsonString = new JSONObject()
                    .put("teamTitle", teamNameField.getText())
                    .put("members", memberArray).toString();
            transferReaderWriter.writeTransfer(jsonString);

            //Close this GUI
            Stage stage = (Stage) submitButton.getScene().getWindow();
            stage.close();
        }
    }
}