package coursework.criticalpath

import java.util

import coursework.project.Project
import coursework.task.{Task, TaskHandler}

import scala.collection.mutable
import scala.collection.mutable.ArrayBuffer
import scala.jdk.CollectionConverters.ListHasAsScala

class CriticalPathScala {

  def getAllTasks(project : Project): util.ArrayList[Task] ={
    /*
    get all of the tasks assigned to this project and return it in as a java Util list
    this is done so that the list of tasks can be passed back to a java class error free
    */
    // declare an ArrayBuffer of the Task data type
    var allTasks = new ArrayBuffer[Task]()
    // loop over every task assigned
    for (i <- 0 to project.getTasksAssigned.size() - 1){
      // if the current task is stored as a instance of the Task class simply add it
      if (project.getTasksAssigned.get(i).isInstanceOf[Task]){
        allTasks.addOne(project.getTasksAssigned.get(i))
      } else {
        // if the task is stored as a hashmap recreate the task from the hashmap then add it
        allTasks.addOne(createTaskFromHashMap(project.getTasksAssigned.get(i).asInstanceOf[util.HashMap[Any,Any]]))
      }
    }

    // create a new java array list so that the tasks can be sent back to a java class easily
    var allTaskScala = new util.ArrayList[Task]()
    // loop through the ArrayBuffer adding the elements to an ArrayList
    for (i <- allTasks){
      allTaskScala.add(i)
    }

    allTaskScala
  }

  def getCriticalPath(allTasks: util.ArrayList[Task]) : util.ArrayList[Task] ={
    // do the forward walk on the list
    var listWalkedForwards = walkListAhead(allTasks)
    // do the backwards walk on the list
    var listWalkedBackwards = walkListBackwards(listWalkedForwards)
    // make a new arrayList so that the Array list can be easily passed back
    var criticalPathList = new util.ArrayList[Task]()
    // add the elements in the reverse order so they are stored base node to end node
    for (i <- listWalkedBackwards.reverse){
      criticalPathList.add(i)
    }

    criticalPathList
  }

  def walkListAhead(allTasks: util.ArrayList[Task]): mutable.Buffer[Task] = {
    val taskHandler = new TaskHandler
    // convert the array list into a scala collection being Buffer in this case
    var scalaAllTasks = allTasks.asScala
    // set the early finish time of the first node
    scalaAllTasks(0).setEarlyFinishTime(scalaAllTasks(0).getEarlyStartTime + scalaAllTasks(0).getDuration)
    // loop through all the tasks for this project
    for (i <- 1 to scalaAllTasks.size - 1){
      var predecessorList = scalaAllTasks(i).getPredecessors
      for (j <- 0 to predecessorList.size() - 1){
        // get the title of the predecessor so the start and finish times can be checked
        var taskTitle = getTaskTitle(predecessorList.get(j).asInstanceOf[util.HashMap[Any,Any]])
        for (k <- 0 to scalaAllTasks.size - 1){
          if (scalaAllTasks(k).getTaskTitle.equals(taskTitle)){
            // if the tasks early start time is bigger than the predecessors early finish time this is wrong so change it to the right value
            if (scalaAllTasks(i).getEarlyStartTime < scalaAllTasks(k).getEarlyFinishTime){
              // if it is needed set the early start  time of this task
              scalaAllTasks(i).setEarlyStartTime(scalaAllTasks(k).getEarlyFinishTime)
            }
          }
        }
      }
      // set the early finish time of this task
      scalaAllTasks(i).setEarlyFinishTime(scalaAllTasks(i).getEarlyStartTime + scalaAllTasks(i).getDuration)
    }
    scalaAllTasks
  }

  def walkListBackwards(allTasks: mutable.Buffer[Task]): mutable.Buffer[Task] = {
    var endNodePos = 0
    var endNode = allTasks(0)
    // loop through the list looking for the node that will serve as the end node biggest early finish time
    for (i <- 0 to allTasks.size - 1){
      if (allTasks(i).getEarlyFinishTime > endNode.getEarlyFinishTime){
        // change the end node
        endNode = allTasks(i)
        // store the index of the end node in the whole list
        endNodePos = i
      }
    }

    // set the values of the end node
    allTasks(endNodePos).setLateFinishTime(allTasks(endNodePos).getEarlyFinishTime)
    allTasks(endNodePos).setLateStartTime(allTasks(endNodePos).getEarlyFinishTime - allTasks(endNodePos).getDuration)

    // make a new array buffer to be used in the recursive function
    var empty = new ArrayBuffer[Task]()
    // recursively find the predecessors of the node with the highest values
    var recursiveList = recursiveGetPredecessor(allTasks(endNodePos), empty)

    recursiveList
  }

  def recursiveGetPredecessor(task: Task, taskList: ArrayBuffer[Task]): ArrayBuffer[Task] = {
    var taskHandler = new TaskHandler
    // check if the task has predecessors stored
    if (task.getPredecessors.isEmpty){
      // if not break out of the recursion returning the critical list
      taskList += task
      taskList
    } else {
      // if the task is not the base task
      // add it to the task list
      taskList += task
      // create the next task to search
      var taskToSearch = createTaskFromHashMap(task.getPredecessors.get(0).asInstanceOf[util.HashMap[Any,Any]])
      // recursively call this function
      recursiveGetPredecessor(taskToSearch, taskList)
    }
  }

  def getTaskTitle(map: util.HashMap[Any,Any]): String = {
    // get the task name from a task stored as a hash map
    var taskTitle = map.get("taskTitle").toString
    taskTitle
  }

  def createTaskFromHashMap(map: util.HashMap[Any,Any]): Task = {
    // get the task as a hash map
    val taskHandler = new TaskHandler
    // get all of the needed values out of the hashmap
    var taskTitle = map.get("taskTitle").toString
    var teamAssigned = map.get("teamAssigned").toString
    var projectFor = map.get("projectFor").toString
    var duration = Integer.parseInt(map.get("duration").toString)
    var predecessors = map.get("predecessors").asInstanceOf[util.List[Task]]
    var successors = map.get("successors").asInstanceOf[util.List[Task]]
    var progress = Integer.parseInt(map.get("progress").toString)


    // create a task from these values and return the task
    taskHandler.createTask(taskTitle,teamAssigned,projectFor,duration,predecessors,successors,progress)
  }

}
