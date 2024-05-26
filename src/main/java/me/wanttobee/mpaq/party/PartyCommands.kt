package me.wanttobee.mpaq.party

import me.wanttobee.commandtree.Description
import me.wanttobee.commandtree.ITreeCommand
import me.wanttobee.commandtree.partials.*
import me.wanttobee.mpaq.quest.QuestManager

object PartyCommands : ITreeCommand {
    override val description = Description("Manage Parties")
        .addSubDescription("create", "create a new party", "party create")
        .addSubDescription("invite", "invite a player to your party", "party invite <player>")
        .addSubDescription("disband", "disband your party", "party disband")
        .addSubDescription("list", "list player in your current party", "party list")
        .addSubDescription("accept", "accept a party invite", "party accept <inviter>")
        .addSubDescription("decline", "decline a party invite", "party decline <inviter>")
        .addSubDescription("leave", "leave your current party", "party leave")
        .addSubDescription("kick", "kick a player from your party", "party kick <player>")
        .addSubDescription("stopQuest", "stop the current quest", "party stopQuest")
        .addSubDescription("startQuest", "start a quest", "party startQuest")

    override val command = BranchPartial("party").setStaticPartials(
        EmptyPartial("create").setEffect { commander -> PartyManager.createParty(commander) },
        EmptyPartial("disband").setEffect { commander -> PartyManager.partyDisband(commander) },
        EmptyPartial("list").setEmptyEffect { commander -> PartyManager.partyListMembers(commander) },
        EmptyPartial("leave").setEffect { commander -> PartyManager.partyLeave(commander) },

        PlayerPartial("invite")
            .exceptCommander(true)
            .setAllowTargetSelectors(atS=false ,atR=true, atA=true)
            .setEffect { commander, player -> PartyManager.partyInvite(commander, player) },

        PlayerPartial("kick").exceptCommander(true)
            .setEffect { commander, player -> PartyManager.partyKick(commander, player) },

        PlayerPartial("accept").setDynamicOptions { commander ->
                PartyManager.getIncomingInvites(commander).map { it.first }.toTypedArray()
            }.setEffect { commander, inviter -> PartyManager.acceptInvite(commander, inviter) }
            .setEmptyEffect{ commander -> PartyManager.acceptInvite(commander, null) },

        PlayerPartial("decline").setDynamicOptions { commander ->
                PartyManager.getIncomingInvites(commander).map { it.first }.toTypedArray()
            }.setEffect { commander, inviter -> PartyManager.declineInvite(commander, inviter) }
            .setEmptyEffect{ commander -> PartyManager.declineInvite(commander, null) },

        // ====================================
        EmptyPartial("stopQuest").setEffect { commander ->
            QuestManager.stopQuest(PartyManager.getParty(commander))
        },
        EmptyPartial("startQuest").setEffect { commander ->
            QuestManager.startQuest(PartyManager.getParty(commander), QuestManager.createExampleQuest())
        }
    )
}
