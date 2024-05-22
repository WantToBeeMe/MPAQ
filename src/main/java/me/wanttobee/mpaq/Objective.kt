package me.wanttobee.mpaq

import org.bukkit.entity.Player

abstract class Objective(
    val players : List<Player>,
    val amount: Int,
    val currentAmount: Int,
    val name: String,
    val description: String,
)
 {
     fun getPercentage(): Double {
         return (currentAmount.toDouble() / amount.toDouble()) * 100
     }
     abstract fun startObjective()
     abstract fun clearObjective()
     fun isFinished(): Boolean {
         return currentAmount >= amount
     }


 }