package me.wanttobee.mpaq

import me.wanttobee.commandtree.ICommandNamespace
import me.wanttobee.commandtree.ICommandObject
import me.wanttobee.commandtree.nodes.*
import net.md_5.bungee.api.ChatColor
import org.bukkit.entity.Player

object NPCCommands : ICommandNamespace {
    override val commandName: String = "questnpc"
    override val commandSummary: String = "Manage NPCs"
    override val hasOnlyOneGroupMember: Boolean = false
    override val isZeroParameterCommand: Boolean = false
    override val systemCommands: Array<ICommandObject> = arrayOf(
        ListNPCs,
        DeleteNPCs,
        CreateNPCs
    )

    object CreateNPCs : ICommandObject {
        override val baseTree: ICommandNode = CommandPairLeaf(
            "create",
            CommandStringLeaf("name", null, { commander, name -> name }),
            CommandStringLeaf("skinName", null, { commander, skinName -> skinName }),
            { commander, pair ->
                val (name, skinName) = pair
                val npc = NPCManager.createNPC(name, skinName, commander.location)
                commander.sendMessage("${ChatColor.GREEN}Created NPC ${npc.name} with skin $skinName")
            }
        )
        override val helpText: String = "/npc create <name> <skinName>"
    }

    object ListNPCs : ICommandObject {
        override val baseTree: ICommandNode = CommandEmptyLeaf(
            "list"
        ) { commander ->
            val npcs = NPCManager.listNPCs()
            npcs.forEach { npc ->
                commander.sendMessage("${ChatColor.GREEN}${npc.name}")
            }
        }
        override val helpText: String = "/npc list"
    }

    object DeleteNPCs : ICommandObject {
        override val baseTree: ICommandNode = CommandStringLeaf(
            "delete",
            {NPCManager.listNPCs().map { it.name }.toTypedArray()},
            { commander, name ->
                val success = NPCManager.deleteNPC(name)
                if (success) {
                    commander.sendMessage("${ChatColor.GREEN}Deleted NPC $name")
                } else {
                    commander.sendMessage("${ChatColor.RED}NPC $name not found")
                }
            }
        )
        override val helpText: String = "/npc delete <name>"
    }

}