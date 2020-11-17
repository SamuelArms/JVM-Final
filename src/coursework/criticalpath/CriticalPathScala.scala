package coursework.criticalpath

import java.util
import coursework.task.{Task, TaskHandler}
import scala.collection.mutable
import scala.collection.mutable.ArrayBuffer
import scala.jdk.CollectionConverters.ListHasAsScala

class CriticalPathScala {

  def getCriticalPath(allTasks: util.ArrayList[Task]) : util.ArrayList[Task] ={
    var listWalkedForwards = walkListAhead(allTasks)
    var listWalkedBackwards = walkListBackwards(listWalkedForwards)
    var criticalPathList = new util.ArrayList[Task]()
    for (i <- listWalkedBackwards.reverse){
      criticalPathList.add(i)
    }

    criticalPathList
  }

  def walkListAhead(allTasks: util.ArrayList[Task]): mutable.Buffer[Task] = {
    val taskHandler = new TaskHandler
    var scalaAllTasks = allTasks.asScala
    scalaAllTasks(0).setEet(scalaAllTasks(0).getEst + scalaAllTasks(0).getDuration)
    for (i <- 1 to scalaAllTasks.size - 1){
      var predecessorList = scalaAllTasks(i).getPredecessors
      for (j <- 0 to predecessorList.size() - 1){
        var taskTitle = getTaskTitle(predecessorList.get(j).asInstanceOf[util.HashMap[Any,Any]])
        for (k <- 0 to scalaAllTasks.size - 1){
          if (scalaAllTasks(k).getTaskTitle.equals(taskTitle)){
            if (scalaAllTasks(i).getEst < scalaAllTasks(k).getEet){
              scalaAllTasks(i).setEst(scalaAllTasks(k).getEet)
            }
          }
        }
      }
      scalaAllTasks(i).setEet(scalaAllTasks(i).getEst + scalaAllTasks(i).getDuration)
    }

    println("walk list forwards")
    for(task <- scalaAllTasks){
      println("Task Title: " + task.getTaskTitle + " Early Start Time: " + task.getEst + " Early End Time: " +task.getEet+
        " late Start Time: " +task.getLst+" late End Time: " + task.getLet)
    }
    scalaAllTasks
  }

  def walkListBackwards(allTasks: mutable.Buffer[Task]): mutable.Buffer[Task] = {
    var endNodePos = 0
    var endNode = allTasks(0)
    for (i <- 0 to allTasks.size - 1){
      if (allTasks(i).getEet > endNode.getEet){
        endNode = allTasks(i)
        endNodePos = i
      }
    }

    allTasks(endNodePos).setLet(allTasks(endNodePos).getEet)
    allTasks(endNodePos).setLst(allTasks(endNodePos).getEet - allTasks(endNodePos).getDuration)

    var empty = new ArrayBuffer[Task]()
    var recursiveList = recursiveGetPredecessor(allTasks(endNodePos), empty)

    recursiveList
  }

  def recursiveGetPredecessor(task: Task, taskList: ArrayBuffer[Task]): ArrayBuffer[Task] = {
    var taskHandler = new TaskHandler
    if (task.getPredecessors.isEmpty){
      taskList += task
      return taskList
    } else {
      taskList += task
      var taskToSearch = createTaskFromHashMap(task.getPredecessors.get(0).asInstanceOf[util.HashMap[Any,Any]])
      return recursiveGetPredecessor(taskToSearch, taskList)
    }
  }

  def getTaskTitle(map: util.HashMap[Any,Any]): String = {
    var taskTitle = map.get("taskTitle").toString
    taskTitle
  }

  def createTaskFromHashMap(map: util.HashMap[Any,Any]): Task = {
    val taskHandler = new TaskHandler
    var taskTitle = map.get("taskTitle").toString
    var teamAssigned = map.get("teamAssigned").toString
    var projectFor = map.get("projectFor").toString
    var duration = Integer.parseInt(map.get("duration").toString)
    var predecessors = map.get("predecessors").asInstanceOf[util.List[Task]]
    var successors = map.get("successors").asInstanceOf[util.List[Task]]

    taskHandler.createTask(taskTitle,teamAssigned,projectFor,duration,predecessors,successors)
  }

}
