package coursework.criticalpath

import coursework.project.Project
import coursework.task.Task
import coursework.task.TaskHandler

class CriticalPathKot {


    fun getAllTasksInProject(project: Project): MutableList<Task>{

        // mutable list to hold all the tasks
        var allTasksInProject = mutableListOf<Task>()

        // loop through all the tasks for the project
        for (i in project.tasksAssigned.indices) {
            if (project.tasksAssigned.get(i) is Task) {
                // if the task found is stored as a task add it to the list
                allTasksInProject.add(project.tasksAssigned.get(i))
            } else {
                // if the task is not stored as a task create it from the map
                allTasksInProject.add(createTaskFromHashMap(project.tasksAssigned.get(i) as MutableMap<*, *>))
            }
        }

        return allTasksInProject
    }

    fun getCriticalPath(allTasks: MutableList<Task>) : Pair<List<Task>, Int> {
        // walk through the list forwards
        var updatedList = walkListAhead(allTasks)
        // walk through the list backwards
        var (listWalkedBack, biggestCost) = walkListBackwards(updatedList)

        var criticalPathList = mutableListOf<Task>()

        // flip the list do it goes from base task to end node
        for (i in listWalkedBack.size -1 downTo 0) {
            criticalPathList.add(listWalkedBack[i])
        }
        return Pair(criticalPathList, biggestCost)
    }

    fun walkListAhead(allTasks: List<Task>): List<Task>{
        // start with the first element in the list as this should always be a base node
        allTasks[0].earlyFinishTime = allTasks[0].earlyStartTime + allTasks[0].duration
        for (i in 1 until allTasks.size) {
            // get the predecessor from the list
            var predecessorList = allTasks[i].predecessors
            for (j in predecessorList.indices) {
                // create the predecessor task
                var task = createTaskFromHashMap(predecessorList.get(j) as MutableMap<*, *>)
                for (k in 0 until allTasks.size){
                    if (allTasks[k].taskTitle.equals(task.taskTitle)){
                        // if the current tasks early start time is bigger than its predecessors early end time
                        if (allTasks[i].earlyStartTime < allTasks[k].earlyFinishTime){
                            // set the early start time to be the same as the end time of the predecessor
                            allTasks[i].earlyStartTime = allTasks[k].earlyFinishTime
                        }
                    }
                }
            }
            // set the current tasks early end time as the early start time plus the duration
            allTasks[i].earlyFinishTime = allTasks[i].earlyStartTime + allTasks[i].duration
        }

        return allTasks
    }

    fun walkListBackwards(allTasks: List<Task>) : Pair<MutableList<Task>, Int> {
        //end task is the node with the biggest early finish time this therefore has to be the end node and the most important one
        // find the node with the biggest early finish time
        var endNodePos = 0
        var endNode = allTasks[0]
        for (i in 0 until allTasks.size){
            if (allTasks[i].earlyFinishTime > endNode.earlyFinishTime){
                endNode = allTasks[i]
                // store the index at which the end node is found
                endNodePos = i
            }
        }
        // go through the predecessors now and set all the values
        allTasks[endNodePos].lateFinishTime = allTasks[endNodePos].earlyFinishTime
        allTasks[endNodePos].lateStartTime = allTasks[endNodePos].earlyFinishTime - allTasks[endNodePos].duration
        // now that the end node has been set can backwards from this node

        var empty = mutableListOf<Task>()
        // recursively get the predecessors of the task
        var recursiveList = recursiveGetPredecessor(allTasks[endNodePos], empty)

        return Pair(recursiveList, allTasks[endNodePos].earlyFinishTime)
    }


    fun recursiveGetPredecessor(task:Task, taskList: MutableList<Task>) : MutableList<Task>{
        // check if the task had a predecessor task
        if (task.predecessors.isEmpty()){
            // if not add the current task to the list
            taskList.add(task)
            // break out of the recursion
            return taskList
        } else {
            // add the current task to the list
            taskList.add(task)
            // create the predecessor
            var task = createTaskFromHashMap(task.predecessors.get(0) as MutableMap<*, *>)
            // call this function recursively
            return recursiveGetPredecessor(task, taskList)
        }
    }

    fun createTaskFromHashMap(map: MutableMap<*, *>): Task{
        // task in the task hashmap as a mutable map
        // get all the needed elemenets from the map
        val taskHandler = TaskHandler()
        val taskTitle = map["taskTitle"].toString()
        val teamAssigned = map["teamAssigned"].toString()
        val projectFor = map["projectFor"].toString()
        val duration = map["duration"] as Int
        val predecessors = map["predecessors"] as List<Task>
        val successors = map["successors"] as List<Task>
        val progress = map["progress"] as Int
        // return the elements as a task
        return taskHandler.createTask(taskTitle, teamAssigned, projectFor, duration,
                predecessors, successors, progress)
    }

}