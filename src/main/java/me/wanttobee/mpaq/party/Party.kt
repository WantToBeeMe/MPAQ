package me.wanttobee.mpaq.party

import org.bukkit.ChatColor
import org.bukkit.entity.Player
import java.util.UUID

//when joining an event you always join with your entire party
class Party(leader: Player) {
    private val members: MutableList<Player> = mutableListOf()
    private val leaderUUID  : UUID

    init{
        leaderUUID = leader.uniqueId
        members.add(leader)
        PartyManager.sendSuccessMessage(leader, "Party created")
    }

    // same broadcast, just not send to the invoking player
    private fun broadcastParty(invoker: Player, message: String) {
        members.filter { it != invoker }
            .forEach { PartyManager.sendInfoMessage(it, message) }
    }
    fun broadcastParty(message: String) {
        members.forEach { PartyManager.sendInfoMessage(it, message) }
    }

    // If the player is a leader, it should also be a member
    fun isLeader(player: Player): Boolean = player.uniqueId == leaderUUID
    fun isMember(player: Player): Boolean = members.contains(player)

    private fun leaderCheck(invoker: Player): Boolean {
        if (!isLeader(invoker)){
            PartyManager.sendErrorMessage(invoker, "You are not the leader of the party")
            return false
        }
        return true
    }

    fun disband(invoker: Player) {
        if (!leaderCheck(invoker)) return
        broadcastParty(invoker, "${ChatColor.RED} You left the party since the party is disbanded")
        PartyManager.sendSuccessMessage(invoker, "Party disbanded")
        PartyManager.removeParty(this)
    }

    fun kickPlayer(invoker: Player, player: Player) {
        if (!leaderCheck(invoker)) return
        if (!members.contains(player)) {
            PartyManager.sendErrorMessage(invoker, "Player is not in the party")
            return
        }
        broadcastParty("${player.name} has been kicked from the party")
        removeMember(player, true)
    }

    fun invitePlayer(invoker: Player, invited: Player) {
        if (!leaderCheck(invoker)) return
        val inviteEffect = { addMember(invited) }

        if ( PartyManager.getOutgoingInvites(invoker).any { it.second == invited } ) {
            PartyManager.sendErrorMessage(invoker, "Player is already invited")
            return
        }
        PartyManager.allPendingInvites.add(Triple(invoker, invited, inviteEffect))
        PartyManager.sendSuccessMessage(invoker, "Invited ${invited.name} to the party")
        PartyManager.sendInfoMessage(invited, "${invoker.name} has invited you to a party. Use /party accept ${invoker.name} to join")
    }

    fun listMembers(invoker: Player) {
        PartyManager.sendInfoMessage(invoker, "Party members:")
        members.forEach { invoker.sendMessage("- #${it.name}") }
    }

    fun leavePlayer(invoker: Player) {
        if (isLeader(invoker)) disband(invoker)
        else removeMember(invoker)
    }

    private fun addMember(player: Player, silent: Boolean = false) {
        // keep this private to ensure that no outside action can modify the members list
        members.add(player)
        if(!silent) broadcastParty("${player.name} has joined the party")
    }

    private fun removeMember(player: Player, silent: Boolean = false) {
        // keep this private to ensure that no outside action can modify the members list
        if(!silent) broadcastParty("${player.name} has left the party")
        members.remove(player)
    }

    fun swapSamePlayer(leavedPlayer: Player, joinedPlayer: Player)  {
        if (leavedPlayer.uniqueId != joinedPlayer.uniqueId) return

        removeMember(leavedPlayer, true)
        addMember(joinedPlayer, true)
        joinedPlayer.sendMessage("You have rejoined the party")
    }
}
