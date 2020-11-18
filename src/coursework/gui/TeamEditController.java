package coursework.gui;

import coursework.team.Team;
import coursework.team.TeamHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class TeamEditController implements Initializable {

    @FXML
    private TextField memberField;
    @FXML
    private Button submitButton;

    private TeamHandler teamHandler = new TeamHandler();
    private FileWriter file;
    private Team currentTeam;



    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // get the team from the transfer file
        currentTeam = teamHandler.createTeamFromTransferFile();
    }

    public void addMember(){
        // ensure all the needed information works
        if (memberField.getText().equals("")){
            // display the pop up error message
            PopUpBox.display("Edit Error","Please ensure that a new members name has been provided");
        } else {
            currentTeam.getMembers().add(memberField.getText());
            String saveString = teamHandler.getSaveString(currentTeam);
            try {
                file = new FileWriter("src/coursework/data transfer.json");
                // write an empty line
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
