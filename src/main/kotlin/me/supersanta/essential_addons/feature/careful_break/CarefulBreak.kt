package me.supersanta.essential_addons.feature.careful_break

import me.supersanta.essential_addons.utils.hasCarefulBreak
import net.casual.arcade.events.GlobalEventHandler
import net.casual.arcade.events.ListenerRegistry.Companion.register
import net.casual.arcade.events.server.player.PlayerBlockDropEvent
import net.casual.arcade.utils.PlayerUtils.dropItemStackIntoInventory

object CarefulBreak {
    internal fun registerEvents() {
        GlobalEventHandler.Server.register<PlayerBlockDropEvent>(::onPlayerBlockDropEvent)
    }

    private fun onPlayerBlockDropEvent(event: PlayerBlockDropEvent) {
        if (event.player.hasCarefulBreak()) {
            event.drops.forEach { event.player.dropItemStackIntoInventory(it) { } }
        }
    }
}