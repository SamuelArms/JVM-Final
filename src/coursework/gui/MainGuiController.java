package coursework.gui;

import coursework.criticalpath.CriticalPathScala;
import coursework.persistence.Persistence;
import coursework.project.Project;
import coursework.project.ProjectHandler;
import coursework.task.Task;
import coursework.task.TaskHandler;
import coursework.team.Team;
import coursework.team.TeamHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.stage.Modality;
import javafx.stage.Stage;
import coursework.transfer.TransferReaderWriter;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class MainGuiController implements Initializable {
    @FXML
    private ListView projectList;
    @FXML
    private ListView teamList;
    @FXML
    private ListView taskList;
    @FXML
    private TextArea infoArea;
    @FXML
    private RadioButton kotlinCriticalPath;
    @FXML
    private RadioButton scalaCriticalPath;

    // Define the master lists and the handlers for this instance of running
    private ArrayList<Project> projects = new ArrayList<>();
    private ArrayList<Team> teams = new ArrayList<>();
    private ArrayList<Task> tasks = new ArrayList<>();
    private final ProjectHandler projectHandler = new ProjectHandler();
    private final TeamHandler teamHandler = new TeamHandler();
    private final TaskHandler taskHandler = new TaskHandler();
    private final CriticalPathScala criticalPathSca = new CriticalPathScala();
    private TransferReaderWriter transferReaderWriter = new TransferReaderWriter();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // get the projects from the save file and set the list
        projects = projectHandler.getProjectsFromSave();
        setProjectListView();

        // get the teams from the save file and set the list
        teams = teamHandler.getTeamsFromSave();
        setTeamListView();

        // get the tasks from the save file and set the list
        tasks = taskHandler.getTasksFromSave();
        setTaskListView();

        //Clear the data transfer from the previous session
        transferReaderWriter.writeTransfer("");
    }

    public void createProject() {
        try {
            // Set the scene that is used to create the project by loading the fxml file
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("ProjectCreation.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 250, 400);
            Stage stage = new Stage();
            stage.setTitle("Project creation");
            stage.setScene(scene);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
            File file = new File("src/coursework/transfer/data transfer.json");
            if (file.length() != 0) {
                // Get the created project from the transfer file add it to the main GUI and save it
                projects.add(projectHandler.createProjectFromTransferFile());
                // update the listview and save with the new project made
            }
            setProjectListView();
            save();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void createTeam() {
        try {
            // Set the scene that is used to create the team by loading the fxml file
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("TeamCreation.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 250, 400);
            Stage stage = new Stage();
            stage.setTitle("Team creation");
            stage.setScene(scene);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
            File file = new File("src/coursework/data transfer.json");
            if (file.length() != 0) {
                // Get the created team from the transfer file add it to the main GUI and save it
                teams.add(teamHandler.createTeamFromTransferFile());
                // save and update the gui
            }
            save();
            setTeamListView();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }


    public void createTask() {
        // Set up variables to be used in this method
        teamList.getSelectionModel().clearSelection();
        projectList.getSelectionModel().clearSelection();
        String placeholderProjectTitle;
        String testString;
        Task placeholderTask;
        Project placeholderProject;
        try {
            // Set the scene that is used to create the task by loading the fxml file
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("TaskCreation.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 800, 400);
            Stage stage = new Stage();
            stage.setTitle("Task creation");
            stage.setScene(scene);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
            File file = new File("src/coursework/transfer/data transfer.json");
            if(file.length() != 0) {
                // Get the created task from the transfer file
                tasks.add(taskHandler.createTaskFromTransferFile());
                // Get the task as a placeholder variable
                placeholderTask = taskHandler.createTaskFromTransferFile();
                placeholderProjectTitle = placeholderTask.getProjectFor();
                // For loop to find the project the task is assigned to
                for (int i = 0; i < projects.size(); i++) {
                    if (placeholderProjectTitle.equals(projects.get(i).getProjectTitle())) {
                        placeholderProject = projectHandler.addTask(projects.get(i), placeholderTask);
                        projects.set(i, placeholderProject);

                    }
                }
            }
            // Set the GUI to show updated values and save
            setTaskListView();
            setProjectListView();
            save();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void save() {
        String saveString = "";
        // for every project in the project list get the save string
        for (Project project : projects){
            saveString += projectHandler.getSaveString(project) + "\n";
        }
        // save the projects
        Persistence.Companion.createFilePersistence().saveProject(saveString);

        saveString = "";
        // for every project in the team list get the save string
        for (Team team : teams){
            saveString += teamHandler.getSaveString(team) + "\n";
        }
        // save the teams
        Persistence.Companion.createFilePersistence().saveTeam(saveString);

        saveString = "";
        // for every task in the project list get the save string
        for (Task task : tasks){
            saveString += taskHandler.getSaveString(task) + "\n";
        }
        // save the tasks
        Persistence.Companion.createFilePersistence().saveTask(saveString);


    }

    public void editItem() {
        if (teamList.getSelectionModel().getSelectedItem() != null) {
            // if a team is selected call the edit team function
            editTeam();
        }
        if (taskList.getSelectionModel().getSelectedItem() != null) {
            // if a task is selected call the edit task function
            editTask();
        }
        if (projectList.getSelectionModel().getSelectedItem() != null) {
            // projects cant be edited yet so display a popup menu
            PopUpBox.display("Error", "Projects can not be edited");
        }
    }

    public void viewProject() {
        teamList.getSelectionModel().clearSelection();
        taskList.getSelectionModel().clearSelection();
        String display = "";
        // make sure that a valid project was selected
        if (projectList.getSelectionModel().getSelectedItem() != null) {
            String placeholder = projectList.getSelectionModel().getSelectedItem().toString();
            for (Project project: projects){
                // find the project that has been selected
                if (placeholder.equals(project.getProjectTitle())){
                    // set the info area to show the information of the project
                    if (kotlinCriticalPath.isSelected()){
                        // if kotlin was selected display the critical path with the critical path calculated by kotlin
                        if (!project.getTasksAssigned().isEmpty()) {
                            display = projectHandler.getKotlinCritical(project, projectHandler.getDisplay(project));
                        } else {
                            // kotlin is selected but no tasks have been added so dont get the critical path yet
                            display = projectHandler.getDisplayNoTasks(project);
                        }

                    }
                    // if the critical path needs to be worked out with scala code
                    if (scalaCriticalPath.isSelected()){
                        // define the array lists to be worked with
                        ArrayList<Task> allTasks;
                        ArrayList<Task> criticalTasks;
                        if (!project.getTasksAssigned().isEmpty()){
                            // pass the project to scala where a ArrayBuffer is filled with the tasks
                            // the ArrayBuffer is then turned into a Java ArrayList with a for loop
                            allTasks = criticalPathSca.getAllTasks(project);
                            // get the critical path on the scala side this ArrayList is converted into a scala datatype the Buffer
                            criticalTasks = criticalPathSca.getCriticalPath(allTasks);
                            // when the project has tasks workout the critical path with scala and then display the information
                            display = projectHandler.getScalaCritical(criticalTasks, projectHandler.getDisplay(project));
                        } else {
                            // set the display when no tasks are set yet
                            display = projectHandler.getDisplayNoTasks(project);
                        }
                    }
                }
            }
            infoArea.setText(display);
        }
    }
    public void viewTeam() {
        projectList.getSelectionModel().clearSelection();
        taskList.getSelectionModel().clearSelection();
        // ensure that a valid team has been selected
        if (teamList.getSelectionModel().getSelectedItem() != null) {
            String placeholder = teamList.getSelectionModel().getSelectedItem().toString();
            for (Team team: teams){
                // find the team that has been selected
                if (placeholder.equals(team.getTeamTitle())){
                    // set the info area to show the information of the team
                    infoArea.setText(teamHandler.getDisplay(team));
                }
            }
        }

    }

    public void viewTask() {
        teamList.getSelectionModel().clearSelection();
        projectList.getSelectionModel().clearSelection();
        // ensure a valid task has been selected
        if (taskList.getSelectionModel().getSelectedItem() != null){
            String placeholder = taskList.getSelectionModel().getSelectedItem().toString();
            for (Task task: tasks){
                // find the task that has been selected
                if (placeholder.equals(task.getTaskTitle())) {
                    // set the info area to show the information of the task
                    infoArea.setText(taskHandler.getDisplay(task));
                }
            }
        }

    }

    public void editTask(){
        // get the title of the task that is updating
        String updateTask = taskList.getSelectionModel().getSelectedItem().toString();
        for (Task task: tasks){
            if (updateTask.equals(task.getTaskTitle())){
                // get save string of the task
                String transferString = taskHandler.getSaveString(task);
                transferReaderWriter.writeTransfer(transferString);
            }
        }
        // show the new gui
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("TaskEdit.fxml"));
        try {
            Scene scene = new Scene(fxmlLoader.load(), 700, 400);
            Stage stage = new Stage();
            stage.setTitle("Task Edit");
            stage.setScene(scene);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // get the updated task from the transfer file
        Task taskFromSave = taskHandler.createTaskFromTransferFile();
        int count = 0;
        for (Task task: tasks){
            if (taskFromSave.getTaskTitle().equals(task.getTaskTitle())){
                // set the updated task in the task list
                tasks.set(count, taskFromSave);
            }
            count += 1;
        }

        // Update the project with the new task
        for (int i = 0; i < projects.size(); i++) {
            if (taskFromSave.getProjectFor().equals(projects.get(i).getProjectTitle())){
                // update the project
                Project project = projectHandler.updateProject(taskFromSave, projects.get(i));
                // update the project list
                projects.set(i, project);
            }
        }

        // save and update the gui
        infoArea.setText("");
        taskList.getSelectionModel().clearSelection();
        save();
        setTeamListView();
        setProjectListView();
        setTaskListView();
    }

    public void editTeam(){
        // get the title of the task that is updating
        String updateTeam = teamList.getSelectionModel().getSelectedItem().toString();
        for (Team team: teams){
            if (updateTeam.equals(team.getTeamTitle())){
                // get save string of the task
                String transferString = teamHandler.getSaveString(team);
               transferReaderWriter.writeTransfer(transferString);
            }
        }
        // show the new gui
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("TeamEdit.fxml"));
        try {
            Scene scene = new Scene(fxmlLoader.load(), 250, 400);
            Stage stage = new Stage();
            stage.setTitle("Team Edit");
            stage.setScene(scene);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // get the updated task from the transfer file
        Team teamFromSave = teamHandler.createTeamFromTransferFile();
        int count = 0;
        for (Team team: teams){
            if (teamFromSave.getTeamTitle().equals(team.getTeamTitle())){
                // set the updated task in the task list
                teams.set(count, teamFromSave);
            }
            count += 1;
        }
        // save and update the gui
        infoArea.setText("");
        teamList.getSelectionModel().clearSelection();
        save();
        setTeamListView();
    }



    public void setProjectListView(){
        projectList.getItems().clear();
        for (Project project: projects){
            // add the project titles to the list view
            projectList.getItems().add(project.getProjectTitle());
        }
    }

    public void setTeamListView(){
        teamList.getItems().clear();
        for (Team team: teams){
            // add the team titles to the list view
            teamList.getItems().add(team.getTeamTitle());
        }
    }

    public void setTaskListView(){
        taskList.getItems().clear();
        for (Task task: tasks){
            // add the task titles to the list view
            taskList.getItems().add(task.getTaskTitle());
        }
    }

    public void kotlinPath(){
        if (projectList.getSelectionModel().getSelectedItem() != null){
            // update the project information to be shown with kotlin critical path
            viewProject();
        }
    }

    public void scalaPath(){
        if (projectList.getSelectionModel().getSelectedItem() != null){
            // update the project information to be shown with scala critical path
            viewProject();
        }
    }
}



