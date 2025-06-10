package me.supersanta.essential_addons.feature.logging

import carpet.logging.HUDController
import carpet.logging.HUDLogger
import carpet.logging.LoggerRegistry
import net.casual.arcade.utils.ComponentUtils.grey
import net.casual.arcade.utils.ComponentUtils.wrap
import net.minecraft.ChatFormatting
import net.minecraft.network.chat.Component
import net.minecraft.server.MinecraftServer
import java.util.function.Supplier
import kotlin.reflect.jvm.javaField

object AutoSaveLogger {
    private const val NAME = "autosave"

    @JvmField var autosave: Boolean = false

    internal fun register() {
        LoggerRegistry.registerLogger(
            NAME, HUDLogger(this::autosave.javaField, NAME, null, null, false)
        )
        HUDController.register { server ->
            if (this.autosave) {
                LoggerRegistry.getLogger(NAME).log(Supplier { this.getLog(server) })
            }
        }
    }

    private fun getLog(server: MinecraftServer): Array<Component> {
        val tick = server.tickCount
        var previous = tick % 6000
        if (tick != 0 && previous == 0) {
            previous = 0
        }

        val next = 6000 - previous
        val color = when {
            next <= 100 -> ChatFormatting.LIGHT_PURPLE
            next <= 500 -> ChatFormatting.RED
            next <= 1000 -> ChatFormatting.YELLOW
            else -> ChatFormatting.DARK_GREEN
        }
        return arrayOf(
            Component.literal("Prev: ").grey().wrap()
                .append(Component.literal("$previous").withStyle(color))
                .append(" Next: ")
                .append(Component.literal("$next").withStyle(color))
        )
    }
}