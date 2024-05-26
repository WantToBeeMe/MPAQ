package me.wanttobee.mpaq.quest

class Quest(val name: String, val id: Int, val objectives: List<Objective>) {
    fun isCompleted(): Boolean = objectives.all { it.isCompleted() }

    fun getTaskByName(taskName: String): Task? {
        for (objective in objectives) {
            for (task in objective.tasks) {
                if (task.name == taskName) {
                    return task
                }
            }
        }
        return null
    }
}
