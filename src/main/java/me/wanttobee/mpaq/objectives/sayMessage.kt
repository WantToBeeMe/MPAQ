package me.wanttobee.mpaq.objectives

import me.wanttobee.mpaq.Objective
import org.bukkit.Bukkit
import org.bukkit.entity.Player

class SayMessage(players: List<Player>, amount: Int) : Objective(
    players,
    amount,
    0,
    "Say Message",
    "Say a message"
){
    override fun startObjective() {
        Bukkit.broadcastMessage("Say a message")
    }

    override fun clearObjective() {
        Bukkit.broadcastMessage("Objective cleared")
    }
}