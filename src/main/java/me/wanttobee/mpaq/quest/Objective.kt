package me.wanttobee.mpaq.quest


class Objective(val description: String, val name: String, val tasks: List<Task>) {
    fun isCompleted(): Boolean = tasks.all { it.isCompleted() }
}
