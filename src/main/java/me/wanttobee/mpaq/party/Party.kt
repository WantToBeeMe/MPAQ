package me.wanttobee.mpaq.party

import org.bukkit.entity.Player

class Party (
    val leader: Player,
    val members: MutableList<Player> = mutableListOf(),
    val outgoingInvites: MutableList<Player> = mutableListOf()) {

    fun messageParty(message: String) {
        leader.sendMessage(message)
        members.forEach { it.sendMessage(message) }
    }

    fun addMember(player: Player) {
        members.add(player)
        messageParty("${player.name} has joined the party")
    }

    fun removeMember(player: Player) {
        members.remove(player)
        messageParty("${player.name} has left the party")
    }
}