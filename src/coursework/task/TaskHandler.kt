package coursework.task

import org.json.JSONObject
import coursework.transfer.TransferReaderWriter
import java.io.File
import java.util.ArrayList

class TaskHandler {

    private val tasks = ArrayList<Task>()

    // create a task with all the needed values
    fun createTask(taskTitle: String, teamAssigned: String, projectFor: String,
                   duration: Int, predecessors: List<Task>, successors: List<Task>, progress: Int): Task {
        return Task(taskTitle, teamAssigned, projectFor, duration, predecessors, successors, progress)

    }

    fun getTasksFromSave(): ArrayList<Task> {
        // retrieve the data from the save file using lambda expressions
        File("src/coursework/persistence/task Persistence.txt").useLines { lines ->
            lines.forEach { line ->
                // within the lambda expression retrieve the data and get the needed values to create the task
                val jsonObj = JSONObject(line)
                var taskTitle = jsonObj.get("taskTitle").toString()
                var teamAssigned = jsonObj.get("teamAssigned").toString()
                var projectFor = jsonObj.get("projectFor").toString()
                var duration = jsonObj.getInt("duration")
                var predecessors = jsonObj.getJSONArray("predecessors").toList()
                var successors = jsonObj.getJSONArray("successors").toList()
                var progress = jsonObj.getInt("progress")
                // create the task and add it to the tasks list within the lambda expression
                tasks.add(createTask(taskTitle, teamAssigned, projectFor,
                        duration, predecessors as List<Task>, successors as List<Task>, progress))
            }
        }
        // return the list of tasks
        return tasks
    }

    fun getSaveString(task: Task): String {
        // convert the task object to a json object
        var jsonObj = JSONObject(task)
        // return a string version of the json object for saving
        return jsonObj.toString()
    }


    fun createTaskFromTransferFile(): Task {
        val transferReaderWriter = TransferReaderWriter()

        // Read the line from the
        var line = transferReaderWriter.readTransfer()
        // make json object from the line
        val jsonObj = JSONObject(line)
        // get the needed values
        var taskTitle = jsonObj.get("taskTitle").toString()
        var teamAssigned = jsonObj.get("teamAssigned").toString()
        var projectFor = jsonObj.get("projectFor").toString()
        var duration = jsonObj.getInt("duration")
        var predecessors = jsonObj.getJSONArray("predecessors").toList()
        var successors = jsonObj.getJSONArray("successors").toList()
        var progress = jsonObj.getInt("progress")
        // create a base task object from these values
        return createTask(taskTitle, teamAssigned, projectFor,
                duration, predecessors as List<Task>, successors as List<Task>, progress)
    }

    fun getDisplay(task: Task): String {
        var display = ""
        // add the base task details to the string
        if (task.predecessors.size == 0) {
             display = "TaskTitle:\n\t${task.taskTitle}\n" +
                    "Duration:\n\t ${task.duration}\nTeam Assigned:\n\t${task.teamAssigned}" +
                    "\nProject For: \n\t ${task.projectFor}\nProgress: \n\t ${task.progress}%\nPredecessor Task:\n\tThis is a base task"
        } else {
            // if the task is found to be a successor task then also add the task that has to be completed before it
            display = "TaskTitle:\n\t${task.taskTitle}\n" +
                    "Duration:\n\t ${task.duration}\nTeam Assigned:\n\t${task.teamAssigned}" +
                    "\nProject For: \n\t ${task.projectFor}\nProgress: \n\t${task.progress}%\nPredecessor Task:\n\t"
            for (i in task.predecessors.indices) {
                if (task.predecessors.get(i) is Task) {
                    // if the predecessor task is stored as a task simply ass the title to the string
                    display += "${task.predecessors.get(i).taskTitle}"
                } else {
                    // if the predecessor is stored as a has map get the value out of the map
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