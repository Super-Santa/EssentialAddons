package me.supersanta.essential_addons.commands

import com.mojang.brigadier.arguments.IntegerArgumentType
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import com.mojang.brigadier.context.CommandContext
import me.supersanta.essential_addons.EssentialSettings
import me.supersanta.essential_addons.feature.lag_spike.LagSpike
import me.supersanta.essential_addons.feature.lag_spike.LagSpikes
import me.supersanta.essential_addons.utils.requires
import net.casual.arcade.commands.CommandTree
import net.casual.arcade.commands.argument
import net.casual.arcade.commands.arguments.EnumArgument
import net.casual.arcade.commands.executes
import net.casual.arcade.commands.success
import net.casual.arcade.utils.ComponentUtils.lime
import net.minecraft.commands.CommandBuildContext
import net.minecraft.commands.CommandSourceStack
import net.minecraft.commands.SharedSuggestionProvider
import net.minecraft.network.chat.Component
import kotlin.time.Duration.Companion.milliseconds

object LagSpikeCommand: CommandTree {
    private const val MAX_LAG_TIME_MS = 60_000

    override fun create(buildContext: CommandBuildContext): LiteralArgumentBuilder<CommandSourceStack> {
        return CommandTree.buildLiteral("lag-spike") {
            requires(EssentialSettings::commandLagSpike, "command.lag-spike")
            argument("duration-ms", IntegerArgumentType.integer(1, MAX_LAG_TIME_MS)) {
                suggests { _, b -> SharedSuggestionProvider.suggest(listOf("600", "1200", "6000"), b) }
                argument("tick-phase", EnumArgument.enumeration<LagSpike.TickPhase> { it.pretty }) {
                    argument("sub-phase", EnumArgument.enumeration<LagSpike.SubPhase> { it.name.lowercase() }) {
                        executes(::addLagSpike)
                    }
                }
            }
        }
    }

    private fun addLagSpike(context: CommandContext<CommandSourceStack>) {
        val duration = IntegerArgumentType.getInteger(context, "duration-ms")
        val phase = EnumArgument.getEnumeration<LagSpike.TickPhase>(context, "tick-phase")
        val sub = EnumArgument.getEnumeration<LagSpike.SubPhase>(context, "sub-phase")
        LagSpikes.submit(LagSpike(phase, sub, duration.milliseconds))
        context.source.success(Component.literal("Successfully added lag spike").lime())
    }
}