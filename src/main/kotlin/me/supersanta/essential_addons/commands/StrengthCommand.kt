package me.supersanta.essential_addons.commands

import com.mojang.brigadier.builder.LiteralArgumentBuilder
import com.mojang.brigadier.context.CommandContext
import me.supersanta.essential_addons.EssentialSettings
import me.supersanta.essential_addons.utils.requires
import me.supersanta.essential_addons.utils.sendToActionBar
import net.casual.arcade.commands.CommandTree
import net.casual.arcade.commands.executes
import net.casual.arcade.utils.ComponentUtils.gold
import net.casual.arcade.utils.ComponentUtils.lime
import net.casual.arcade.utils.ComponentUtils.red
import net.minecraft.commands.CommandBuildContext
import net.minecraft.commands.CommandSourceStack
import net.minecraft.network.chat.Component
import net.minecraft.world.effect.MobEffectInstance
import net.minecraft.world.effect.MobEffects

object StrengthCommand: CommandTree {
    override fun create(buildContext: CommandBuildContext): LiteralArgumentBuilder<CommandSourceStack> {
        return CommandTree.buildLiteral("strength") {
            requires(EssentialSettings::commandStrength, "command.strength")
            executes(::toggleStrength)
        }
    }

    private fun toggleStrength(context: CommandContext<CommandSourceStack>) {
        val player = context.source.playerOrException
        if (!player.hasEffect(MobEffects.STRENGTH)) {
            player.addEffect(MobEffectInstance(MobEffects.STRENGTH, MobEffectInstance.INFINITE_DURATION, 0, true, false))
            player.sendToActionBar(
                Component.literal("Strength has been ").append(Component.literal("ENABLED").lime()).gold()
            )
        } else {
            player.removeEffect(MobEffects.STRENGTH)
            player.sendToActionBar(
                Component.literal("Strength has been ").append(Component.literal("DISABLED").red()).gold()
            )
        }
    }
}