package coursework.project

import coursework.task.Task
import coursework.task.TaskHandler
import org.json.JSONObject
import java.io.BufferedReader
import java.io.File
import java.io.FileReader

class ProjectHandler {

    private val projects = ArrayList<Project>()

    // create a project with the needed values
    fun createProject(projectTitle: String, tasksAssigned: List<Task>): Project{
        return Project(projectTitle, tasksAssigned)
    }

    fun getProjectsFromSave(): ArrayList<Project>{
        var lineList = mutableListOf<String>()
        // retrieve the data from the save file using lambda expressions
        File("src/coursework/persistence/project Persistence.txt").useLines { lines -> lines.forEach { lineList.add(it) }}
        lineList.forEach { line ->
            // get the values within the lambda expression
            val jsonObj = JSONObject(line)
            var projectTitle = jsonObj.get("projectTitle").toString()
            var tasksAssigned = jsonObj.getJSONArray("tasksAssigned").toList()
            // create a project with the values
            projects.add(createProject(projectTitle, tasksAssigned as List<Task>))
        }
        return projects
    }

    fun createProjectFromTransferFile(): Project {
        // read the line of the transfer file
        var reader = BufferedReader(FileReader("src/coursework/data transfer.json"))
        var line = reader.readLine()
        // turn the line into a json object
        val jsonObj = JSONObject(line)
        // get the values out of the json object
        var projectTitle = jsonObj.get("projectTitle").toString()
        var tasksAssigned = jsonObj.getJSONArray("tasksAssigned").toList()
        // create a project form these values
        return createProject(projectTitle, tasksAssigned as List<Task>)

    }

    fun getSaveString(project: Project): String{
        // turn the json object into a string
        var jsonObj = JSONObject(project)
        // return the json object as a string
        return jsonObj.toString()
    }

    fun addTask(project: Project, task: Task): Project{
        // add the newly created task to the list of tasks within the project
        project.tasksAssigned += task
        return project
    }

    fun getDisplay(project: Project): String{
        var taskHandler = TaskHandler()
        var tasks = taskHandler.getTasksFromSave()

        var display = "Project Title:\n\t ${project.projectTitle} \n Tasks for this project:"
        for (task : Task in tasks){
            if (task.projectFor.equals(project.projectTitle)){
                display += "\n\t${task.taskTitle}"
            }
        }
        return display
    }

    fun updateProject(task: Task, project: Project): Project{
        for (i in project.tasksAssigned.indices){
            if (project.tasksAssigned.get(i) is Task){
                if (task.taskTitle.equals(project.tasksAssigned.get(i).taskTitle)){
                    // if the task is stored as a task and is the correct one convert it to a mutable list
                    var changeList = project.tasksAssigned as MutableList<Task>
                    // change the task in the mutable list
                    changeList[i] = task
                    // reassign the list to the project
                    project.tasksAssigned = changeList
                }
            } else {
                // same but have to get the task title from the hash map
                if (task.taskTitle.equals(getTaskTitle(project.tasksAssigned.get(i) as MutableMap<*, *>))){
                    var changeList = project.tasksAssigned as MutableList<Task>
                    changeList[i] = task
                    project.tasksAssigned = changeList
                }
            }
        }
        return project
    }

    fun getTaskTitle(mp: MutableMap<*, *>) : String {
        // get the task title if the task is a hash map
        return mp.get("taskTitle") as String
    }

}