package me.wanttobee.mpaq.party

import me.wanttobee.commandtree.Description
import me.wanttobee.commandtree.ITreeCommand
import me.wanttobee.commandtree.partials.*
import me.wanttobee.mpaq.quest.QuestManager

object PartyCommands : ITreeCommand {
    override val description = Description("Manage Parties")
        .addSubDescription("invite", "invite a player to your party", "party invite <player>")
        .addSubDescription("disband", "disband your party", "party disband")
        .addSubDescription("list", "list player in your current party", "party list")
        .addSubDescription("accept", "accept a party invite", "party accept")
        .addSubDescription("leave", "leave your current party", "party leave")
        .addSubDescription("kick", "kick a player from your party", "party kick <player>")
        .addSubDescription("stopQuest", "stop the current quest", "party stopQuest")
        .addSubDescription("startQuest", "start a quest", "party startQuest")

    override val command = BranchPartial("party").setStaticPartials(
        PlayerPartial("invite").setEffect { commander, player -> PartyManager.inviteCommandLogic(commander, player) },
        EmptyPartial("disband").setEffect { commander -> PartyManager.disbandPartyByLeader(commander) },
        EmptyPartial("list").setEffect { commander -> PartyManager.listPartyMembers(commander) },
        EmptyPartial("accept").setEffect { commander -> PartyManager.acceptInvite(commander) },
        EmptyPartial("leave").setEffect { commander -> PartyManager.leaveParty(commander) },
        PlayerPartial("kick").setEffect { commander, player -> PartyManager.kickFromParty(commander, player) },
        EmptyPartial("stopQuest").setEffect { commander ->
            QuestManager.stopQuest(PartyManager.getPlayerParty(commander))
        },
        EmptyPartial("startQuest").setEffect { commander ->
            QuestManager.startQuest(PartyManager.getPlayerParty(commander), QuestManager.createExampleQuest())
        }
    )
}
