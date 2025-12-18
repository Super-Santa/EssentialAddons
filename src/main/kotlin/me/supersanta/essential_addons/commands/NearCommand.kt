package me.supersanta.essential_addons.commands

import com.mojang.brigadier.arguments.DoubleArgumentType
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import com.mojang.brigadier.context.CommandContext
import me.supersanta.essential_addons.EssentialSettings
import me.supersanta.essential_addons.utils.requires
import net.casual.arcade.commands.*
import net.casual.arcade.utils.component.gold
import net.casual.arcade.utils.component.joinToComponent
import net.casual.arcade.utils.component.lime
import net.minecraft.commands.CommandBuildContext
import net.minecraft.commands.CommandSourceStack
import net.minecraft.network.chat.Component

object NearCommand: CommandTree {
    override fun create(buildContext: CommandBuildContext): LiteralArgumentBuilder<CommandSourceStack> {
        return CommandTree.buildLiteral("near") {
            requires(EssentialSettings::commandNear, "command.near")
            argument("range", DoubleArgumentType.doubleArg(0.0)) {
                executes(::listNearbyPlayers)
            }
        }
    }

    private fun listNearbyPlayers(context: CommandContext<CommandSourceStack>): Int {
        val range = DoubleArgumentType.getDouble(context, "range")
        val location = context.source.locationWithLevel
        val player = context.source.player
        val players = location.level.getPlayers {
            it.distanceToSqr(location.position) < range * range && it != player
        }
        if (players.isEmpty()) {
            return context.source.fail("There are no players near you")
        }
        val formatted = players.joinToComponent { it.name }.lime()
        return context.source.success(
            Component.literal("Players near you: ").append(formatted).gold()
        )
    }
}