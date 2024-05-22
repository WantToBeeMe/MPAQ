package me.wanttobee.mpaq.party

import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent
import java.util.*

object PartyManager : Listener {
    private val allParties = mutableListOf<Party>()
    private val allPendingInvites = mutableMapOf<Player, Party>()
    //player here is an reference to the old player object
    private val OfflinePlayers = mutableListOf<Player>()

    fun isPlayerInParty(player: Player): Boolean {
        for (party in allParties) {
            if (party.members.contains(player)){
                return true
            }
        }
        return false
    }
    fun isPlayerLeader(player: Player): Boolean {
        for (party in allParties) {
            if (player.uniqueId == party.leaderUUID) {
                return true
            }
        }
        return false
    }

    fun getPlayerParty(player: Player): Party? {
        for (party in allParties) {
            if (party.members.contains(player)) {
                return party
            }
        }
        return null
    }

    fun getLeaderParty(player: Player): Party? {
        for (party in allParties) {
            if (player.uniqueId == party.leaderUUID) {
                return party
            }
        }
        return null
    }

    fun createParty(leader: Player) {
        //first we check all the party members to see if they are in a party
        if (isPlayerInParty(leader)) {
            leader.sendMessage("You are already in a party")
            return
        }
        val party = Party(leader.uniqueId)
        allParties.add(party)
        party.addMember(leader)
        leader.sendMessage("Party created")

    }

    fun disbandParty(leader: Player) {
        val party = getLeaderParty(leader)
        if (party != null) {
            allParties.remove(party)
            party.messageParty("Party disbanded")
        }
        else {
            leader.sendMessage("You are not the leader or a party")
        }
    }

    fun inviteToParty(leader: Player, player: Player) {
        val party = getLeaderParty(leader)
        if (party != null) {
            party.outgoingInvites.add(player)
            allPendingInvites[player] = party
            player.sendMessage("${leader.name} has invited you to their party")
        }
        else {
            leader.sendMessage("You are not the leader of a party")
        }

    }

    fun inviteCommandLogic(leader: Player, player: Player) {
        //check if player is in a party
        if (isPlayerInParty(player)) {
            leader.sendMessage("Player is already in a party")
            return
        }
        //check if player is the leader
        if (isPlayerLeader(player)) {
            leader.sendMessage("Player is already in a party")
            return
        }
        //now we check if the player has a pending invite
        if (allPendingInvites.containsKey(player)) {
            leader.sendMessage("Player already has a pending invite")
            return
        }
        //now check if the leader is already in a party, if not create a party
        if (!isPlayerInParty(leader)) {
            createParty(leader)
        }
        inviteToParty(leader, player)

    }

    fun acceptInvite(invitee: Player) {
        val party = allPendingInvites[invitee]
        if (party != null) {
            party.addMember(invitee)
            allPendingInvites.remove(invitee)
            party.messageParty("${invitee.name} has joined the party")
        }
        else {
            invitee.sendMessage("You have no pending invites")
        }
    }

    fun declineInvite(invitee: Player) {
        val party = allPendingInvites[invitee]
        if (party != null) {
            allPendingInvites.remove(invitee)

        }
        else {
            invitee.sendMessage("You have no pending invites")
        }
    }

    fun leaveParty(player: Player) {
        val party = getPlayerParty(player)
        if (party != null) {
            party.removeMember(player)
            player.sendMessage("You have left the party")
        }
        else {
            player.sendMessage("You are not in a party")
        }
    }

    fun listPartyMembers(player: Player) {
        val party = getPlayerParty(player)
        if (party != null) {
            player.sendMessage("Party members:")
            party.members.forEach { player.sendMessage(it.name) }
        }
        else {
            player.sendMessage("You are not in a party")
        }
    }

    @EventHandler
    fun onPlayerQuit(event: PlayerQuitEvent) {
        val player = event.player
        val party = getPlayerParty(player)
        if (party != null)
            OfflinePlayers.add(player)

    }

    @EventHandler
    fun rejoinParty(event: PlayerJoinEvent) {
        val player = event.player
        for(offlinePlayer in OfflinePlayers) {
            if (player.uniqueId == offlinePlayer.uniqueId) {
                OfflinePlayers.remove(player)
                val party = getPlayerParty(offlinePlayer)
                if (party != null) {
                    party.removeMember(offlinePlayer)
                    party.addMember(player)
                    player.sendMessage("You have rejoined the party")
                }
                break
            }
        }

    }


}