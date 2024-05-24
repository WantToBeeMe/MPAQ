package me.wanttobee.mpaq.quest

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityPickupItemEvent
import org.bukkit.event.player.PlayerPickupItemEvent

class QuestListener : Listener {

    @EventHandler
    fun onPlayerPickupItem(event: EntityPickupItemEvent) {
        handleEvent(event)
    }

    fun handleEvent(event: Any) {
        for (quest in QuestManager.activeQuests.values) {
            for (objective in quest.objectives) {
                for (task in objective.tasks) {
                    task.handleEvent(event)
                }
            }
        }
    }

}