package me.supersanta.essential_addons.commands

import carpet.CarpetSettings
import com.mojang.brigadier.arguments.IntegerArgumentType
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import com.mojang.brigadier.context.CommandContext
import me.supersanta.essential_addons.EssentialSettings
import me.supersanta.essential_addons.utils.requires
import me.supersanta.essential_addons.utils.sendToActionBar
import net.casual.arcade.commands.CommandTree
import net.casual.arcade.commands.argument
import net.casual.arcade.commands.executes
import net.casual.arcade.commands.success
import net.casual.arcade.utils.ComponentUtils.gold
import net.casual.arcade.utils.ComponentUtils.lime
import net.casual.arcade.utils.ComponentUtils.red
import net.minecraft.commands.CommandBuildContext
import net.minecraft.commands.CommandSourceStack
import net.minecraft.network.chat.Component

object ViewDistanceCommand: CommandTree {
    override fun create(buildContext: CommandBuildContext): LiteralArgumentBuilder<CommandSourceStack> {
        return CommandTree.buildLiteral("view-distance") {
            requires(EssentialSettings::commandViewDistance, "command.view-distance")
            executes(::queryViewDistance)
            argument("distance", IntegerArgumentType.integer(10, 32)) {
                executes(::setViewDistance)
            }
        }
    }

    private fun queryViewDistance(context: CommandContext<CommandSourceStack>) {
        val player = context.source.playerOrException
        val server = context.source.server
        val distance = server.playerList.viewDistance
        player.sendToActionBar(
            Component.literal("View distance is currently ").gold()
                .append(Component.literal("$distance").lime())
        )
    }

    private fun setViewDistance(context: CommandContext<CommandSourceStack>) {
        val player = context.source.playerOrException
        val server = context.source.server
        val distance = IntegerArgumentType.getInteger(context, "distance")
        if (server.isDedicatedServer) {
            if (distance != server.playerList.viewDistance) {
                server.playerList.viewDistance = distance
                CarpetSettings.viewDistance = distance
            }
            context.source.success("View distance has changed to: $distance", true)
            player.sendToActionBar(
                Component.literal("View distance has been changed to: ").gold()
                    .append(Component.literal("$distance").lime())
            )
        } else {
            player.sendToActionBar(Component.literal("View distance can only be changed on a server").red())
        }
    }
}