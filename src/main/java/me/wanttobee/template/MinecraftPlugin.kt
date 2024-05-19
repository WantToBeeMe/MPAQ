package me.wanttobee.template;

import org.bukkit.ChatColor
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.plugin.java.annotation.command.Commands
import org.bukkit.plugin.java.annotation.dependency.Library
import org.bukkit.plugin.java.annotation.plugin.ApiVersion
import org.bukkit.plugin.java.annotation.plugin.Description
import org.bukkit.plugin.java.annotation.plugin.Plugin
import org.bukkit.plugin.java.annotation.plugin.author.Author


@Plugin(name = "Template", version ="1.0.1")
@ApiVersion(ApiVersion.Target.v1_20)
@Author("WantToBeeMe")
@Description("A super cool plugin")

@Commands(
       // Command(name = "helloWorld", aliases = ["hw","hello"], usage = "/helloWorld"),
       // Command(name = "byeWorld", aliases = ["bw","bye"], usage = "/byeWorld reason"),
)

// library has to be loaded in order to use kotlin
@Library("org.jetbrains.kotlin:kotlin-stdlib-jdk8:1.7.22") //kotlin !!
class MinecraftPlugin : JavaPlugin() {
    companion object {
        lateinit var instance: MinecraftPlugin
        val title = "${ChatColor.GRAY}[${ChatColor.GOLD}Template Plugin${ChatColor.GRAY}]${ChatColor.RESET}"
    }

    override fun onEnable() {
        instance = this

        server.onlinePlayers.forEach { player ->
            player.sendMessage("$title Plugin has been enabled!")
        }
    }

    override fun onDisable() {
        server.onlinePlayers.forEach { player ->
            player.sendMessage("$title Plugin has been disabled!")
        }
    }
}
