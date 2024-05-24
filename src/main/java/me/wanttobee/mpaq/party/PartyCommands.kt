package me.wanttobee.mpaq.party

import me.wanttobee.commandtree.ICommandNamespace
import me.wanttobee.commandtree.ICommandObject
import me.wanttobee.commandtree.nodes.*
import me.wanttobee.mpaq.quest.QuestManager
import me.wanttobee.mpaq.quest.QuestManager.createExampleQuest
import org.bukkit.Bukkit

object PartyCommands : ICommandNamespace {
    override val commandName: String = "party"
    override val commandSummary: String = "Manage parties"
    override val hasOnlyOneGroupMember: Boolean = false
    override val isZeroParameterCommand: Boolean = false
    override val systemCommands: Array<ICommandObject> = arrayOf(
        invitePlayer,
        DisbandParty,
        acceptPartyInvite,
        listParty,
        leaveParty,
        kickFromParty,
        startQuest,
        stopQuest,
    )


    // call the inviteAndCreateParty function because when you invite a player to a party, you also create a party
    object invitePlayer : ICommandObject {
        override val baseTree: ICommandNode = CommandPlayerLeaf(
            "invite",
            { Bukkit.getOnlinePlayers()},
            { commander, player ->
                PartyManager.inviteCommandLogic(commander, player)
            }
        )
        override val helpText: String = "/party invite <player>"
    }

    object DisbandParty : ICommandObject {
        override val baseTree: ICommandNode = CommandEmptyLeaf(
            "disband"
        ) { commander ->
            PartyManager.disbandPartyByLeader(commander)
        }
        override val helpText: String = "/party disband"
    }

    object acceptPartyInvite : ICommandObject {
        override val baseTree: ICommandNode = CommandEmptyLeaf(
            "accept"
        ) { commander ->
            PartyManager.acceptInvite(commander)
        }
        override val helpText: String = "/party accept"
    }

    object listParty : ICommandObject {
        override val baseTree: ICommandNode = CommandEmptyLeaf(
            "list"
        ) { commander ->
            PartyManager.listPartyMembers(commander)
        }
        override val helpText: String = "/party list"
    }

    object leaveParty : ICommandObject {
        override val baseTree: ICommandNode = CommandEmptyLeaf(
            "leave"
        ) { commander ->
            PartyManager.leaveParty(commander)
        }
        override val helpText: String = "/party leave"
    }

    object kickFromParty : ICommandObject {
        override val baseTree: ICommandNode = CommandPlayerLeaf(
            "kick",
            { Bukkit.getOnlinePlayers()},
            { commander, player ->
                PartyManager.kickFromParty(commander, player)
            }
        )
        override val helpText: String = "/party kick <player>"
    }
}

object stopQuest : ICommandObject {
    override val baseTree: ICommandNode = CommandEmptyLeaf(
        "stopQuest"
    ) { commander ->
        QuestManager.stopQuest(PartyManager.getPlayerParty(commander))
    }
    override val helpText: String = "/party stopQuest"
}

object startQuest : ICommandObject {
    override val baseTree: ICommandNode = CommandEmptyLeaf(
        "startQuest"
    ) { commander ->
        QuestManager.startQuest(PartyManager.getPlayerParty(commander), createExampleQuest() )
    }
    override val helpText: String = "/party startQuest"

}
