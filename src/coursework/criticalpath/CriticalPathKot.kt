package coursework.criticalpath

import coursework.project.Project
import coursework.task.Task
import coursework.task.TaskHandler

class CriticalPathKot {


    fun getAllTasksInProject(project: Project): MutableList<Task>{

        var allTasksInProject = mutableListOf<Task>()

        for (i in project.tasksAssigned.indices) {
            if (project.tasksAssigned.get(i) is Task) {
                // if the task found is stored as a task add it to the list
                allTasksInProject.add(project.tasksAssigned.get(i))
                // get the list of successors the task has
                var successorTasks = project.tasksAssigned.get(i).successors
                for (j in successorTasks.indices){
                    // for every task in the successor list create the task from a map and add it to the list
                    allTasksInProject.add(createTaskFromHashMap(successorTasks.get(j) as MutableMap<*, *>))
                }
            } else {
                // if the base task is not stored as a task create it from the map
                allTasksInProject.add(createTaskFromHashMap(project.tasksAssigned.get(i) as MutableMap<*, *>))
                // get the successors of this task
                var successorList = getSuccessors(project.tasksAssigned.get(i) as MutableMap<*, *>)
                for (j in successorList.indices) {
                    // loop through the successor list creating the tasks and adding them to the list of tasks
                    allTasksInProject.add(createTaskFromHashMap(successorList.get(j) as MutableMap<*, *>))
                }
            }
        }

        return allTasksInProject
    }

    fun getCriticalPath(allTasks: MutableList<Task>) : Pair<List<Task>, Int> {
        // walk through the list forwards
        var updatedList = walkListAhead(allTasks)
        // walk through the list backwards
        var (listWalkedBack, biggestCost) = WalkListBackwards(updatedList)

        var criticalPathList = mutableListOf<Task>()

        // flip the list do it goes from base task to end node
        for (i in listWalkedBack.size -1 downTo 0) {
            criticalPathList.add(listWalkedBack[i])
        }
        return Pair(criticalPathList, biggestCost)
    }

    fun walkListAhead(allTasks: List<Task>): List<Task>{
        // start with the first element in the list as this should always be a base node
        allTasks[0].eet = allTasks[0].est + allTasks[0].duration
        for (i in 1 until allTasks.size) {
            var predecessorList = allTasks[i].predecessors
            for (j in predecessorList.indices) {
                var task = createTaskFromHashMap(predecessorList.get(j) as MutableMap<*, *>)
                for (k in 0 until allTasks.size){
                    if (allTasks[k].taskTitle.equals(task.taskTitle)){
                        // if the current tasks est is bigger than its predecessors early end time
                        if (allTasks[i].est < allTasks[k].eet){
                            // set the start time to be the same as the end time
                            allTasks[i].est = allTasks[k].eet
                        }
                    }
                }
            }
            // set the current tasks early end time as the early start time plus the duration
            allTasks[i].eet = allTasks[i].est + allTasks[i].duration
        }

        println("walk list forwards")
        for(task : Task in allTasks){
            println("Task Title: ${task.taskTitle}\tEarly Start Time: ${task.est}\tEarly End Time: ${task.eet}\t" +
                    "late Start Time: ${task.lst}\tlate End Time: ${task.let}")
        }


        return allTasks
    }

    fun WalkListBackwards(allTasks: List<Task>) : Pair<MutableList<Task>, Int> {
        //end task is the node with the biggest early finish time this therefore has to be the end node and the most important one
        // find the node with the biggest early finish time
        var endNodePos = 0
        var endNode = allTasks[0]
        for (i in 0 until allTasks.size){
            if (allTasks[i].eet > endNode.eet){
                endNode = allTasks[i]
                endNodePos = i
            }
        }
        // go through the predecessors now and set all the values
        allTasks[endNodePos].let = allTasks[endNodePos].eet
        allTasks[endNodePos].lst = allTasks[endNodePos].eet - allTasks[endNodePos].duration
        // now that the end node has been set can backwards from this node

        var empty = mutableListOf<Task>()
        var recursiveList = recursiveGetPredecessor(allTasks[endNodePos], empty)

        println("walk list backwards")
        for(task : Task in allTasks){
            println("Task Title: ${task.taskTitle}\tEarly Start Time: ${task.est}\tEarly End Time: ${task.eet}\t" +
                    "late Start Time: ${task.lst}\tlate End Time: ${task.let}")
        }
        return Pair(recursiveList, allTasks[endNodePos].eet)
    }


    fun recursiveGetPredecessor(task:Task, taskList: MutableList<Task>) : MutableList<Task>{
        if (task.predecessors.isEmpty()){
            taskList.add(task)
            return taskList
        } else {
            taskList.add(task)
            var task = createTaskFromHashMap(task.predecessors.get(0) as MutableMap<*, *>)
            return recursiveGetPredecessor(task, taskList)
        }
    }

    fun createTaskFromHashMap(map: MutableMap<*, *>): Task{
        val taskHandler = TaskHandler()
        val taskTitle = map["taskTitle"].toString()
        val teamAssigned = map["teamAssigned"].toString()
        val projectFor = map["projectFor"].toString()
        val duration = map["duration"] as Int
        val predecessors = map["predecessors"] as List<Task>
        val successors = map["successors"] as List<Task>
        return taskHandler.createTask(taskTitle, teamAssigned, projectFor, duration,
                predecessors, successors)
    }

    fun getSuccessors(map: MutableMap<*, *>): List<Task>{
        return  map["successors"] as List<Task>
    }

}