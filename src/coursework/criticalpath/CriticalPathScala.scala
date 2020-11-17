package coursework.criticalpath

import java.util
import java.util.List

import coursework.task.{Task, TaskHandler}

import scala.collection.mutable
import scala.jdk.CollectionConverters.ListHasAsScala
import scala.collection.mutable.Map

class CriticalPathScala {

  def getCriticalPath(allTasks: util.ArrayList[Task]) : util.ArrayList[Task] ={
    var listWalkedForwards = walkListAhead(allTasks)
    var listWalkedBackwards = walkListBackwards(listWalkedForwards)
    var criticalPathList = new util.ArrayList[Task]()
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



  def getTaskTitle(map: util.HashMap[Any,Any]): String = {
    var taskTitle = map.get("taskTitle").toString
    taskTitle
  }

}
