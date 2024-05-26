package me.wanttobee.mpaq

import me.wanttobee.commandtree.Description
import me.wanttobee.commandtree.ITreeCommand
import me.wanttobee.commandtree.partials.*
import net.md_5.bungee.api.ChatColor
import org.bukkit.Bukkit

object NPCCommands : ITreeCommand {
    override val description: Description = Description("Manage NPCs")
        .addSubDescription("create", "create a new NPC", "npc create <name> <skinName>")
        .addSubDescription("list", "list all NPCs", "npc list")
        .addSubDescription("delete", "delete an NPC", "npc delete <name>")

    override val command = BranchPartial("questnpc").setStaticPartials(
        PairPartial<String,String>("create").setPartials(
            StringPartial("name")
                .setDynamicOptions { Bukkit.getOnlinePlayers().map { p -> p.name }.toTypedArray() },
            StringPartial("skinName")
                .setDynamicOptions { Bukkit.getOnlinePlayers().map { p -> p.name }.toTypedArray() }
        ).setEffect { commander, (name, skinName) ->
            NPCManager.createNPC(name, skinName, commander.location)
            commander.sendMessage("${ChatColor.GREEN}Created NPC $name with skin $skinName")
        },
        //=============================================
        EmptyPartial("list").setEffect { commander ->
            NPCManager.listNPCs().forEach { npc ->
                commander.sendMessage("${ChatColor.GRAY}${npc.name} ${ChatColor.GOLD}${npc.name}")
            }
        },
        //=============================================
        StringPartial("delete")
            .setDynamicOptions { commander -> NPCManager.listNPCs().map {it.name}.toTypedArray() }
            .setEffect { commander, npcName ->
                if(NPCManager.deleteNPC(npcName))
                    commander.sendMessage("${ChatColor.GREEN}Deleted NPC $npcName")
                else commander.sendMessage("${ChatColor.RED}NPC $npcName not found")
            }
    )
}
