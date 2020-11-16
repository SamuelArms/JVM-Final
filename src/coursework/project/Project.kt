package coursework.project

import coursework.task.Task

data class Project(
        val projectTitle: String,
        var tasksAssigned: List<Task>,
)