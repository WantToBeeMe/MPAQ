package me.wanttobee.mpaq

import me.wanttobee.commandtree.ICommandNamespace
import me.wanttobee.commandtree.ICommandObject
import net.citizensnpcs.api.CitizensAPI
import net.citizensnpcs.api.npc.NPC
import net.citizensnpcs.api.npc.NPCRegistry
import net.citizensnpcs.api.trait.Trait
import net.citizensnpcs.api.trait.TraitName
import net.citizensnpcs.npc.CitizensNPC
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.entity.EntityType
import org.bukkit.plugin.java.JavaPlugin

object NPCManager {
    private lateinit var register: NPCRegistry
    fun init(plugin: JavaPlugin) {
        register = CitizensAPI.getNPCRegistry()
    }

    fun createNPC(name: String, skinName: String, coordinates: Location): NPC {
        val npc = register.createNPC(EntityType.PLAYER, name)
        npc.setProtected(true)
        npc.isProtected = true
        npc.data().set("player-skin-name", skinName)
        npc.spawn(coordinates)
        return npc
    }

    fun listNPCs(): List<NPC> {
        return register
            .sorted()
            .toList()
    }

    fun deleteNPC(name: String): Boolean{
        val npc = register.toList().firstOrNull { it.name == name }
        return npc?.let{
            it.destroy()
            true
        } ?: false
    }
}
