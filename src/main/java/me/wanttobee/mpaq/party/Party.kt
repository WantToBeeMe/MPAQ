package me.wanttobee.mpaq.party

import org.bukkit.entity.Player
import java.util.UUID

//when joining an event you always join with your entire party
class Party (
    val leaderUUID: UUID,
    val members: MutableList<Player> = mutableListOf(),
    val outgoingInvites: MutableList<Player> = mutableListOf()) {

    fun messageParty(message: String) {
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