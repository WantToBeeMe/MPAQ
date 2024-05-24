package me.wanttobee.mpaq.quest

import me.wanttobee.mpaq.party.Party
import org.bukkit.Material
import org.bukkit.entity.EntityType

object QuestManager {
     val activeQuests = mutableMapOf<Party, Quest>()

    fun startQuest(party: Party?, quest: Quest) {
        if(party == null) return
        activeQuests[party] = quest
        party.messageParty("Quest started: ${quest.name}")
    }

    fun stopQuest(party: Party?) {
        if(party == null) return
        activeQuests.remove(party)
        party.messageParty("Quest stopped")
    }




    //TIJDELIJKE QUEST
    fun createExampleQuest(): Quest {
        val collectWoodTask = CollectItemTask(
            name = "Collect Wood",
            description = "Collect 10 pieces of oak wood",
            amount = 10,
            itemType = Material.OAK_WOOD
        )

        val collectStoneTask = CollectItemTask(
            name = "Collect Stone",
            description = "Collect 20 pieces of stone",
            amount = 20,
            itemType = Material.COBBLESTONE
        )

        val killZombieTask = CollectItemTask.KillEntityTask(
            name = "Kill Zombie",
            description = "Kill 5 zombies",
            amount = 5,
            entityType = EntityType.ZOMBIE
        )

        val killSkeletonTask = CollectItemTask.KillEntityTask(
            name = "Kill Skeleton",
            description = "Kill 5 skeletons",
            amount = 5,
            entityType = EntityType.SKELETON
        )

        val objective1 = Objective(
            name = "Gather Resources",
            description = "Collect wood and stone",
            tasks = listOf(collectWoodTask, collectStoneTask)
        )

        val objective2 = Objective(
            name = "Monster Slayer",
            description = "Kill zombies and skeletons",
            tasks = listOf(killZombieTask, killSkeletonTask)
        )

        return Quest(
            name = "Adventurer's Path",
            id = 1,
            objectives = listOf(objective1, objective2)
        )
    }

}