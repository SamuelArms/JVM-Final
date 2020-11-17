package coursework.project

import coursework.criticalpath.CriticalPathKot
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

    fun getProjectsFromSave(): ArrayList<Project> {
        // retrieve the data from the save file using lambda expressions
        File("src/coursework/persistence/project Persistence.txt").useLines { lines ->
            lines.forEach { line ->
                // within the lambda expression retrieve the data and get the needed values to create the project
                val jsonObj = JSONObject(line)
                var projectTitle = jsonObj.get("projectTitle").toString()
                var tasksAssigned = jsonObj.getJSONArray("tasksAssigned").toList()
                // create the project and add it to the projects list within the lambda expression
                projects.add(createProject(projectTitle, tasksAssigned as List<Task>))
            }
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
        // get the tasks from the save file as the save file is auto updated this should never be out of date
        var tasks = taskHandler.getTasksFromSave()
        // add the project title to the string
        var display = "Project Title:\n\t${project.projectTitle} \nTasks for this project:"
        // loop through all the tasks saved
        for (task : Task in tasks){
            // if the task found is assigned to the current project add the title of the task to the string
            if (task.projectFor.equals(project.projectTitle)){
                display += "\n\t${task.taskTitle}"
            }
        }
        return display
    }

    fun getKotlinCritical(project: Project, display : String) : String {
        // initialize the class needed to get the critical path with kotlin
        var criticalPath = CriticalPathKot()
        var display = display
        // get all the tasks that need to be worked on to see if there is a critical path
        var allTasks = criticalPath.getAllTasksInProject(project)
        // get the tasks of the critical path using the kotlin method and the biggest early finish time
        var (criticalPathList, biggestCost) = criticalPath.getCriticalPath(allTasks)
        // build the display string
        display += "\n\nCritical Path Calculated with Kotlin:"
        display += "\nCritical Cost:\n\t$biggestCost"
        display += "\nCritical Path:"
        for (task: Task in criticalPathList) {
            // add the task titles of the tasks in the critical path
            display += "\n\t${task.taskTitle}"
        }
        return display
    }

    fun getScalaCritical(criticalTasks: MutableList<Task>, display: String): String {
        // get the index of the final position in the list
        var finalPosition = criticalTasks.size - 1
        var display = display

        // build up the display string
        display += "\n\nCritical Path Calculated with Scala:"
        display += "\nCritical Cost:\n\t${criticalTasks[finalPosition].earlyFinishTime}"
        display += "\nCritical Path:"
        for (task: Task in criticalTasks){
            // add the titles of the tasks on the critical path
            display += "\n\t${task.taskTitle}"
        }

        return display
    }

    fun getDisplayNoTasks(project: Project): String{
        return "Project Title:\n\t${project.projectTitle} \nTasks for this project:\n\tNo Tasks assigned yet"
    }

    fun updateProject(task: Task, project: Project): Project{
        /*
        this function is needed so that when the details of the project is changed
        new task ect. the project is over written in the save file as well
        so that things like the critical path can be calculated successfully
         */
        for (i in project.tasksAssigned.indices){
            if (project.tasksAssigned.get(i) is Task){
                if (task.taskTitle.equals(project.tasksAssigned.get(i).taskTitle)){
                    // if the task is stored as a task and is the correct one convert it to a mutable list
                    var changeList = project.tasksAssigned as MutableList<Task>
                    // change the task in the mutable list
                    changeList[i] = task
                    // reassign the task list to the project
                    project.tasksAssigned = changeList
                }
            } else {
                // same but have to get the task title from the hash map
                if (task.taskTitle.equals(getTaskTitle(project.tasksAssigned.get(i) as MutableMap<*, *>))){
                    // get the list of tasks again
                    var changeList = project.tasksAssigned as MutableList<Task>
                    // change the needed element
                    changeList[i] = task
                    // reassign the updated list
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