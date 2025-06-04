package me.supersanta.essential_addons.feature.remove_after_threshold

import me.supersanta.essential_addons.EssentialSettings
import net.casual.arcade.events.GlobalEventHandler
import net.casual.arcade.events.ListenerRegistry.Companion.register
import net.casual.arcade.events.server.level.LevelTickEvent
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.EntityType

object RemoveAfterThreshold {
    internal fun registerEvents() {
        GlobalEventHandler.Server.register<LevelTickEvent>(::onLevelTick)
    }

    private fun onLevelTick(event: LevelTickEvent) {
        val (level) = event
        if (EssentialSettings.removeXpEntitiesAfterThreshold > 0) {
            val xp = level.getEntities(EntityType.EXPERIENCE_ORB) { true }
            if (xp.size > EssentialSettings.removeXpEntitiesAfterThreshold) {
                xp.forEach { it.remove(Entity.RemovalReason.DISCARDED) }
            }
        }
        if (EssentialSettings.removeItemEntitiesAfterThreshold > 0) {
            val items = level.getEntities(EntityType.ITEM) { true }
            if (items.size > EssentialSettings.removeItemEntitiesAfterThreshold) {
                items.forEach { it.remove(Entity.RemovalReason.DISCARDED) }
            }
        }
    }
}