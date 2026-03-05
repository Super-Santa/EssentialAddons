package me.supersanta.essential_addons.feature.careful_break

import me.supersanta.essential_addons.utils.hasCarefulBreak
import net.casual.arcade.events.GlobalEventHandler
import net.casual.arcade.events.ListenerRegistry.Companion.register
import net.casual.arcade.events.server.player.PlayerBlockDropLootEvent
import net.casual.arcade.utils.player.dropItemStackIntoInventory

object CarefulBreak {
    internal fun registerEvents() {
        GlobalEventHandler.Server.register<PlayerBlockDropLootEvent>(::onPlayerBlockDropEvent)
    }

    private fun onPlayerBlockDropEvent(event: PlayerBlockDropLootEvent) {
        if (event.player.hasCarefulBreak()) {
            event.drops.forEach { event.player.dropItemStackIntoInventory(it) { } }
        }
    }
}