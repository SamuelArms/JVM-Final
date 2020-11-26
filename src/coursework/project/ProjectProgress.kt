package coursework.project

import coursework.criticalpath.CriticalPathKot
import coursework.task.Task
import kotlin.math.round

class ProjectProgress {

    fun getProjectProgress(project: Project) : Int{
        var criticalPathKot = CriticalPathKot()
        // get all the tasks associated with this project
        var taskList = criticalPathKot.getAllTasksInProject(project)
        // find the total project duration
        var projectDuration = 0
        for (task: Task in taskList) {
            // add the current task duration to the project duration
            projectDuration += task.duration
        }
        for (task: Task in taskList) {
            // find the tasks duration as a percentage of the project duration
            var taskPercentage = task.duration.toDouble() / projectDuration
            task.projectPercent = round(taskPercentage * 100).toInt()
        }
        // work out the projects percentage of completion
        project.progress = workOutPercentage(taskList)
        // return the project percentage
        return project.progress

    }

        fun workOutPercentage(taskList: MutableList<Task>) : Int{
            for (task : Task in taskList){
                // get the completion percentage of the task as a decimal
                var progressHolder = task.progress.toDouble() / 100
                /*
                get the project percentage of the task this is the the overall amount the task
                is of the project and the amount of the project that has been done
                so if the task is 50 % of the project and 50 % of the task is complete the
                project percent will be 25 %
                */
                task.projectPercent = (task.projectPercent * progressHolder).toInt()
            }
            // make a project percentage counter
            var projectPercentage = 0
            for (task: Task in taskList){
                // add up all the project percentages
                projectPercentage += task.projectPercent
            }
            return projectPercentage
        }

    }