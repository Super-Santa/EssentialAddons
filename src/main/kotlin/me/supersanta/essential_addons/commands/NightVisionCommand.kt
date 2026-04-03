package me.supersanta.essential_addons.commands

import com.mojang.brigadier.builder.LiteralArgumentBuilder
import com.mojang.brigadier.context.CommandContext
import me.supersanta.essential_addons.EssentialSettings
import me.supersanta.essential_addons.utils.requires
import me.supersanta.essential_addons.utils.sendToActionBar
import net.casual.arcade.commands.CommandTree
import net.casual.arcade.commands.executes
import net.casual.arcade.utils.component.gold
import net.casual.arcade.utils.component.lime
import net.casual.arcade.utils.component.red
import net.minecraft.commands.CommandBuildContext
import net.minecraft.commands.CommandSourceStack
import net.minecraft.network.chat.Component
import net.minecraft.world.effect.MobEffectInstance
import net.minecraft.world.effect.MobEffects

object NightVisionCommand: CommandTree<CommandSourceStack> {
    override fun create(buildContext: CommandBuildContext): LiteralArgumentBuilder<CommandSourceStack> {
        return CommandTree.buildLiteral("night-vision") {
            requires(EssentialSettings::commandNightVision, "command.night-vision")
            executes(::toggleNightVision)
        }
    }

    private fun toggleNightVision(context: CommandContext<CommandSourceStack>) {
        val player = context.source.playerOrException
        if (!player.hasEffect(MobEffects.NIGHT_VISION)) {
            player.addEffect(MobEffectInstance(MobEffects.NIGHT_VISION, MobEffectInstance.INFINITE_DURATION, 0, true, false))
            player.sendToActionBar(
                Component.literal("Night vision has been ").append(Component.literal("ENABLED").lime()).gold()
            )
        } else {
            player.removeEffect(MobEffects.NIGHT_VISION)
            player.sendToActionBar(
                Component.literal("Night vision has been ").append(Component.literal("DISABLED").red()).gold()
            )
        }
    }
}