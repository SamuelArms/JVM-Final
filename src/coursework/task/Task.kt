package coursework.task


class Task(
        var taskTitle: String,
        var teamAssigned: String,
        var projectFor: String,
        var duration: Int,
        var predecessors: List<Task>,
        var successors: List<Task>
) {
    // Extra values that do not need to be declared in the primary constructor
    var criticalCost =0
    var est = 0
    var lst = 0
    var eet = -1
    var let = 0


}