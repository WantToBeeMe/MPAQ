package me.wanttobee.mpaq


open class Quest (
    val id: String,
    val name: String,
    val description: String,
    val questType: Type,
    val objectives: List<Objective>) {
    enum class Type {
        BIG,
        MID,
        SMALL,
    }

}
