package me.wanttobee.mpaq.quest

import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.entity.EntityType
import org.bukkit.event.entity.EntityDeathEvent
import org.bukkit.event.entity.EntityPickupItemEvent
import org.bukkit.event.player.PlayerPickupItemEvent

interface Task {
    val name: String
    val description: String
    val amount: Int
    val progress: Int

    fun isCompleted(): Boolean = progress >= amount
    fun incrementProgress()
    fun handleEvent(event: Any) //abstract
}

//tijdelijk hier deze classes gemakt voor de test

class CollectItemTask(
    override val name: String,
    override val description: String,
    override val amount: Int,
    val itemType: Material,
    override var progress: Int = 0
) : Task {
    override fun incrementProgress() {
        progress++
    }

    override fun handleEvent(event: Any){
        if (event is EntityPickupItemEvent && event.item.itemStack.type == itemType) {
            incrementProgress()
        }
    }

    class KillEntityTask(
        override val name: String,
        override val description: String,
        override val amount: Int,
        val entityType: EntityType,
        override var progress: Int = 0
    ) : Task {
        override fun incrementProgress() {
            progress++
        }

        override fun handleEvent(event: Any) {
            if (event is EntityDeathEvent && event.entityType == entityType) {
                val killer = event.entity.killer
                if (killer != null) {
                    incrementProgress()
                }
            }
        }
    }

}