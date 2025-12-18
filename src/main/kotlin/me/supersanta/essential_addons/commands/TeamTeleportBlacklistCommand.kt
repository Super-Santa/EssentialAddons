package me.supersanta.essential_addons.commands

import com.mojang.brigadier.builder.LiteralArgumentBuilder
import com.mojang.brigadier.context.CommandContext
import me.supersanta.essential_addons.feature.extensions.TeamTeleportBlacklistExtension.Companion.blacklistedFromTeleportingTo
import net.casual.arcade.commands.CommandTree
import net.casual.arcade.commands.argument
import net.casual.arcade.commands.literal
import net.casual.arcade.commands.success
import net.casual.arcade.utils.component.lime
import net.casual.arcade.utils.component.wrap
import net.minecraft.commands.CommandBuildContext
import net.minecraft.commands.CommandSourceStack
import net.minecraft.commands.arguments.TeamArgument
import net.minecraft.network.chat.Component

object TeamTeleportBlacklistCommand: CommandTree {
    override fun create(buildContext: CommandBuildContext): LiteralArgumentBuilder<CommandSourceStack> {
        return CommandTree.buildLiteral("team-teleport-blacklist") {
            literal("add") {
                argument("team", TeamArgument.team()) {
                    executes(::addTeamToBlacklist)
                }
            }
            literal("remove") {
                argument("team", TeamArgument.team()) {
                    executes(::removeTeamFromBlacklist)
                }
            }
            literal("query") {
                argument("team", TeamArgument.team()) {
                    executes(::queryTeamBlacklist)
                }
            }
        }
    }

    private fun addTeamToBlacklist(context: CommandContext<CommandSourceStack>): Int {
        val team = TeamArgument.getTeam(context, "team")
        team.blacklistedFromTeleportingTo = true
        return context.source.success(
            Component.literal("Successfully added team: ").lime().wrap().append(team.displayName)
        )
    }

    private fun removeTeamFromBlacklist(context: CommandContext<CommandSourceStack>): Int {
        val team = TeamArgument.getTeam(context, "team")
        team.blacklistedFromTeleportingTo = false
        return context.source.success(
            Component.literal("Successfully removed team: ").lime().wrap().append(team.displayName)
        )
    }

    private fun queryTeamBlacklist(context: CommandContext<CommandSourceStack>): Int {
        val team = TeamArgument.getTeam(context, "team")
        val blacklisted = team.blacklistedFromTeleportingTo
        val suffix = if (blacklisted) "blacklisted" else "not blacklisted"
        return context.source.success(
            team.displayName.wrap().append(Component.literal("is $suffix").lime())
        )
    }
}