package coursework.gui;

import coursework.project.Project;
import coursework.project.ProjectHandler;
import coursework.task.Task;
import coursework.task.TaskHandler;
import coursework.team.Team;
import coursework.team.TeamHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.json.JSONObject;

import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class TaskCreationController implements Initializable {
    @FXML
    private ListView teamListView;
    @FXML
    private ListView projectListView;
    @FXML
    private ListView taskListView;
    @FXML
    private TextField taskTitleField;
    @FXML
    private TextField taskDurationField;
    @FXML
    private Button submitButton;

    private ArrayList<Project> projects = new ArrayList<>();
    private ArrayList<Task> tasks = new ArrayList<>();
    private ArrayList<Team> teams = new ArrayList<>();
    private ProjectHandler projectHandler = new ProjectHandler();
    private TaskHandler taskHandler = new TaskHandler();
    private TeamHandler teamHandler = new TeamHandler();
    private FileWriter file;



    @Override
    public void initialize(URL location, ResourceBundle resources) {
        projects = projectHandler.getProjectsFromSave();
        tasks = taskHandler.getTasksFromSave();
        teams = teamHandler.getTeamsFromSave();
        setProjectListView();
        setTeamListView();
        // display a popupbox with the instructions on how to use this screen
        PopUpBox.display("Instructions", "Select the project this is a task for\n" +
                "Select the team to complete this task\nIf this task is a successor Please select the task that has to be completed first");
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


    public void setProjectListView() {
        /*
        This methods are the same as found in the main GUI
        but due to the framework they cannot be called from this scene
         */
        projectListView.getItems().clear();
        for (int i = 0; i < projects.size(); i++) {
            projectListView.getItems().add(projects.get(i).getProjectTitle());

        }
    }

    public void setTeamListView() {
        /*
        This methods are the same as found in the main GUI
        but due to the framework they cannot be called from this scene
         */
        teamListView.getItems().clear();
        for (int i = 0; i < teams.size(); i++) {
            teamListView.getItems().add(teams.get(i).getTeamTitle());

        }
    }

    public void setTaskListView() {
        taskListView.getItems().clear();
        // check if a project is selected
        if (projectListView.getSelectionModel().getSelectedItem() != null) {
            // get the project title
            String projectSelected = projectListView.getSelectionModel().getSelectedItem().toString();
            for (int i = 0; i < tasks.size(); i++) {
                // if the task is for the project selected
                if (tasks.get(i).getProjectFor().equals(projectSelected)) {
                    // add the task to the task list
                    taskListView.getItems().add(tasks.get(i).getTaskTitle());
                }
            }
        }
    }

    public void submit() {
        // ensure all the needed information is selected
        if (projectListView.getSelectionModel().getSelectedItem() == null || teamListView.getSelectionModel().getSelectedItem() == null) {
            // pop up with a error message
            PopUpBox.display("Creation error", "Please at minimum select a Project to assign the task to\nPlease also select a team for the task to be completed by");
        } else {
            // addd the needed base information for the task
            ArrayList<Task> empty = new ArrayList<>();
            ArrayList<Task> predecessors = new ArrayList<>();
            JSONObject jsonObj = new JSONObject();
            jsonObj.put("taskTitle", taskTitleField.getText());
            jsonObj.put("teamAssigned", teamListView.getSelectionModel().getSelectedItem().toString());
            jsonObj.put("projectFor", projectListView.getSelectionModel().getSelectedItem().toString());
            jsonObj.put("duration", Integer.parseInt(taskDurationField.getText()));
            // find out if this task is a successor of a different task
            if (taskListView.getSelectionModel().getSelectedItem() == null) {
                // if it is not there is no need for predecessors
                jsonObj.put("predecessors", empty);
                jsonObj.put("successors", empty);
            } else {
                // if it is a successor
                for (int i = 0; i < tasks.size(); i++) {
                    if (tasks.get(i).getTaskTitle().equals(taskListView.getSelectionModel().getSelectedItem().toString())) {
                        // add the task it is a successor of to the predecessors list
                        predecessors.add(tasks.get(i));
                    }
                }
                // put the task it is a successor od in the predecessor list
                jsonObj.put("predecessors", predecessors);
                jsonObj.put("successors", empty);
            }
            String jsonString = jsonObj.toString();
            try {
                // Write the tasks to a transfer file
                file = new FileWriter("src/coursework/data transfer.json");
                file.write(jsonString);
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
