package me.supersanta.essential_addons.commands

import com.mojang.brigadier.arguments.StringArgumentType
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import com.mojang.brigadier.context.CommandContext
import com.mojang.brigadier.suggestion.SuggestionProvider
import me.supersanta.essential_addons.EssentialSettings
import me.supersanta.essential_addons.feature.extensions.PlayerWarpsExtension.Companion.addWarp
import me.supersanta.essential_addons.feature.extensions.PlayerWarpsExtension.Companion.getWarp
import me.supersanta.essential_addons.feature.extensions.PlayerWarpsExtension.Companion.getWarpNames
import me.supersanta.essential_addons.feature.extensions.PlayerWarpsExtension.Companion.removeWrap
import me.supersanta.essential_addons.utils.requires
import net.casual.arcade.commands.*
import net.casual.arcade.utils.component.lime
import net.casual.arcade.utils.math.location.LocationWithLevel.Companion.locationWithLevel
import net.casual.arcade.utils.teleportTo
import net.minecraft.commands.CommandBuildContext
import net.minecraft.commands.CommandSourceStack
import net.minecraft.commands.SharedSuggestionProvider
import net.minecraft.network.chat.Component

object WarpCommand: CommandTree {
    override fun create(buildContext: CommandBuildContext): LiteralArgumentBuilder<CommandSourceStack> {
        return CommandTree.buildLiteral("warp") {
            requires(EssentialSettings::commandWarp, "command.warp")
            literal("add") {
                argument("name", StringArgumentType.word()) {
                    executes(::addNewWarp)
                }
            }
            literal("remove") {
                argument("name", StringArgumentType.word()) {
                    suggests(suggestWarpNames())
                    executes(::removeWarp)
                }
            }
            literal("to") {
                argument("name", StringArgumentType.word()) {
                    suggests(suggestWarpNames())
                    executes(::teleportToWarp)
                }
            }
        }
    }

    private fun addNewWarp(context: CommandContext<CommandSourceStack>): Int {
        val name = StringArgumentType.getString(context, "name")
        val player = context.source.playerOrException
        player.addWarp(name, player.locationWithLevel)
        return context.source.success(Component.literal("Successfully set warp $name").lime())
    }

    private fun removeWarp(context: CommandContext<CommandSourceStack>): Int {
        val name = StringArgumentType.getString(context, "name")
        val player = context.source.playerOrException
        if (player.removeWrap(name)) {
            return context.source.success(Component.literal("Successfully removed warp $name").lime())
        }
        return context.source.fail("No such warp $name")
    }

    private fun teleportToWarp(context: CommandContext<CommandSourceStack>): Int {
        val name = StringArgumentType.getString(context, "name")
        val player = context.source.playerOrException
        val warp = player.getWarp(name)
            ?: return context.source.fail("No such warp with name $name")
        val location = warp.resolve(context.source.server)
            ?: return context.source.fail("Warp $name is no longer valid!")
        player.teleportTo(location)
        return context.source.success(Component.literal("Successfully warped to $name").lime())
    }

    private fun suggestWarpNames(): SuggestionProvider<CommandSourceStack> {
        return SuggestionProvider { context, builder ->
            val player = context.source.playerOrException
            SharedSuggestionProvider.suggest(player.getWarpNames(), builder)
        }
    }
}