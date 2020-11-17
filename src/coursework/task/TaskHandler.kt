package coursework.task

import org.json.JSONObject
import java.io.BufferedReader
import java.io.File
import java.io.FileReader
import java.util.ArrayList

class TaskHandler {

    private val tasks = ArrayList<Task>()

    // create a task with all the needed values
    fun createTask(taskTitle: String, teamAssigned: String, projectFor: String,
                   duration: Int, predecessors: List<Task>, successors: List<Task>): Task {
        return Task(taskTitle, teamAssigned, projectFor, duration, predecessors, successors)

    }

    fun getTasksFromSave(): ArrayList<Task> {
        var lineList = mutableListOf<String>()
        // retrieve the data from the save file using lambda expressions
        File("src/coursework/persistence/task Persistence.txt").useLines { lines -> lines.forEach { lineList.add(it) } }
        lineList.forEach { line ->
            // get the values within the lambda expression
            val jsonObj = JSONObject(line)
            var taskTitle = jsonObj.get("taskTitle").toString()
            var teamAssigned = jsonObj.get("teamAssigned").toString()
            var projectFor = jsonObj.get("projectFor").toString()
            var duration = jsonObj.getInt("duration")
            var predecessors = jsonObj.getJSONArray("predecessors").toList()
            var successors = jsonObj.getJSONArray("successors").toList()
            tasks.add(createTask(taskTitle, teamAssigned, projectFor,
                    duration, predecessors as List<Task>, successors as List<Task>))
        }
        // return the list of base tasks
        return tasks
    }

    fun getSaveString(task: Task): String {
        // convert the task object to a json object
        var jsonObj = JSONObject(task)
        // return a string version of the json object for saving
        return jsonObj.toString()
    }


    fun createTaskFromTransferFile(): Task {
        // Read the line from the
        var reader = BufferedReader(FileReader("src/coursework/data transfer.json"))
        var line = reader.readLine()
        // make json object from the line
        val jsonObj = JSONObject(line)
        // get the needed values
        var taskTitle = jsonObj.get("taskTitle").toString()
        var teamAssigned = jsonObj.get("teamAssigned").toString()
        var projectFor = jsonObj.get("projectFor").toString()
        var duration = jsonObj.getInt("duration")
        var predecessors = jsonObj.getJSONArray("predecessors").toList()
        var successors = jsonObj.getJSONArray("successors").toList()
        // create a base task object from these values
        return createTask(taskTitle, teamAssigned, projectFor,
                duration, predecessors as List<Task>, successors as List<Task>)
    }

    fun getDisplay(task: Task): String {
        var display = ""
        // add the base task details to the string
        if (task.predecessors.size == 0) {
             display = "TaskTitle:\n\t${task.taskTitle}\n" +
                    "Duration:\n\t ${task.duration}\nTeam Assigned:\n\t${task.teamAssigned}" +
                    "\nProject For: \n\t ${task.projectFor}\nPredecessor Task:\n\tThis is a base task"
        } else {
            display = "TaskTitle:\n\t${task.taskTitle}\n" +
                    "Duration:\n\t ${task.duration}\nTeam Assigned:\n\t${task.teamAssigned}" +
                    "\nProject For: \n\t ${task.projectFor}\nPredecessor Task:\n\t"
            for (i in task.predecessors.indices) {
                if (task.predecessors.get(i) is Task) {
                    display += "${task.predecessors.get(i).taskTitle}"
                } else {
                    display += getTaskTitle(task.predecessors.get(i) as MutableMap<*, *>)
                }
            }
        }
        return display
    }

    fun getTaskTitle(mp: MutableMap<*, *>) : String {
        // return the task title as a string from the map
        return "${mp.get("taskTitle")}"
    }

}