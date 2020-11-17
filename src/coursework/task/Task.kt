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
    var earlyStartTime = 0
    var lateStartTime = 0
    var earlyFinishTime = -1
    var lateFinishTime = 0


}