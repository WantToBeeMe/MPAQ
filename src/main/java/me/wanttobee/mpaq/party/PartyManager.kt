package me.wanttobee.mpaq.party

import org.bukkit.ChatColor
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent
import org.bukkit.plugin.java.JavaPlugin

object PartyManager : Listener {
    private var title = ""
    private val allParties = mutableListOf<Party>()
    // no map since a leader can invite multiple times, and a player can get multiple invites
    val allPendingInvites = mutableListOf<Triple<Player, Player, ()->Unit>>() // <leader, invitee, invitationProcess>
    private val offlinePlayers = mutableListOf<Player>()

    fun initialize(plugin: JavaPlugin, title: String?) {
        // plugin is never used, but here for consistency (since all other systems have it)
        // also, if you want to access the plugin you now have to do `MinecraftPlugin.instance.you_action`
        // so then this would make it easier since you only have to use `plugin.you_action`
        this.title = title ?: ""
    }

    // Helper functions to make sure all outgoing messages are formatted the same
    fun sendSuccessMessage(player: Player, message: String) = player.sendMessage("$title${ChatColor.GREEN} $message")
    fun sendErrorMessage(player: Player, message: String) = player.sendMessage("$title${ChatColor.RED} $message")
    fun sendInfoMessage(player: Player, message: String) = player.sendMessage("$title${ChatColor.WHITE} $message")

    fun removeParty(party: Party) {
        allPendingInvites.removeIf { party.isLeader(it.first) }
        allParties.remove(party)
    }

    fun getOrCreateParty(player: Player): Party {
        for (party in allParties) {
            if (party.isMember(player))
                return party
        }
        val party = Party(player)
        allParties.add(party)
        return party
    }
    fun createParty(player: Player) {
        if (isPlayerInParty(player)) {
            sendErrorMessage(player,"You are already in a party")
            return
        }
        val party = Party(player)
        allParties.add(party)
    }

    fun isPlayerInParty(player: Player): Boolean = getParty(player) != null

    fun getParty(player: Player): Party? {
        for (party in allParties) {
            if (party.isMember(player))
                return party
        }
        return null
    }

    // =========================
    //  PARTY SHORTCUTS/ALIASES
    // =========================
    fun partyInvite(leader: Player, invitee: Player) = getOrCreateParty(leader).invitePlayer(leader, invitee)
    fun partyDisband(player: Player) = getParty(player)?.disband(player)
    fun partyListMembers(player: Player) = getParty(player)?.listMembers(player)
    fun partyLeave(player: Player) = getParty(player)?.leavePlayer(player)
    fun partyKick(player: Player, target: Player) = getParty(player)?.kickPlayer(player, target)

    // ======================
    //    INVITATION LOGIC
    // ======================
    fun getIncomingInvites(player : Player): List<Triple<Player, Player, ()->Unit>> {
        return allPendingInvites.filter { it.second == player }
    }
    fun getOutgoingInvites(player : Player): List<Triple<Player, Player, ()->Unit>> {
        return allPendingInvites.filter { it.first == player }
    }

    private fun getRelevantInvite(invitee: Player, fromInviter: Player? = null): Triple<Player, Player, ()->Unit>? {
        val invites = getIncomingInvites(invitee)
        if (invites.isEmpty()) {
            sendErrorMessage(invitee, "You have no invites")
            return null
        }
        // if there is more then 1 invite, you HAVE to specify the inviter
        if (invites.size > 1 && fromInviter == null) {
            sendErrorMessage(invitee, "You have multiple invites, please specify the inviter")
            return null
        }
        return if(fromInviter == null) invites[0]
        else invites.firstOrNull { it.first == fromInviter }
    }


    fun acceptInvite(invitee: Player, fromInviter: Player?) {
        val invite = getRelevantInvite(invitee, fromInviter) ?: return
        invite.third()
        allPendingInvites.removeIf { it.second == invitee }
    }

    fun declineInvite(invitee: Player, fromInviter: Player?) {
        val invite = getRelevantInvite(invitee, fromInviter) ?: return
        sendErrorMessage(invite.first, "${invitee.name} declined your invite")
        allPendingInvites.remove(invite)
    }

    // ======================
    // REJOIN PARTY MECHANISM
    // ======================
    @EventHandler
    fun onPlayerQuit(event: PlayerQuitEvent) {
        val player = event.player
        // if you want to make it to also replace the invites, then you would also need to update the invite unit
        allPendingInvites.removeIf { it.first == player || it.second == player }
        val party = getParty(player)
        if (party != null)
            offlinePlayers.add(player)
    }

    @EventHandler
    fun rejoinParty(event: PlayerJoinEvent) {
        val player = event.player
        for(offlinePlayer in offlinePlayers) {
            if (player.uniqueId == offlinePlayer.uniqueId) {
                offlinePlayers.remove(player)
                val party = getParty(offlinePlayer)
                party?.swapSamePlayer(offlinePlayer, player)
                break
            }
        }
    }
}
