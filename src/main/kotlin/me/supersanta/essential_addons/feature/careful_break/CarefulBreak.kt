package me.supersanta.essential_addons.feature.careful_break

import me.supersanta.essential_addons.utils.hasCarefulBreak
import net.casual.arcade.events.GlobalEventHandler
import net.casual.arcade.events.server.player.PlayerBlockDropLootEvent
import net.casual.arcade.events.utils.register
import net.casual.arcade.utils.player.dropItemStackIntoInventory
import net.minecraft.server.level.ServerPlayer

object CarefulBreak {
    @JvmField
    val BREAKER: ScopedValue<ServerPlayer> = ScopedValue.newInstance()

    internal fun registerEvents() {
        GlobalEventHandler.Server.register(::onPlayerBlockDropEvent)
    }

    private fun onPlayerBlockDropEvent(event: PlayerBlockDropLootEvent) {
        if (event.player.hasCarefulBreak()) {
            event.drops.forEach { event.player.dropItemStackIntoInventory(it) { } }
        }
    }
}