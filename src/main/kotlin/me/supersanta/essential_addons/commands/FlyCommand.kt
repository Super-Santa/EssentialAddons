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

object FlyCommand: CommandTree {
    override fun create(buildContext: CommandBuildContext): LiteralArgumentBuilder<CommandSourceStack> {
        return CommandTree.buildLiteral("fly") {
            requires(EssentialSettings::commandFly, "command.fly")
            executes(::toggleFlying)
        }
    }

    private fun toggleFlying(context: CommandContext<CommandSourceStack>) {
        val player = context.source.playerOrException
        if (player.abilities.mayfly) {
            player.abilities.mayfly = false
            player.abilities.flying = false
            player.sendToActionBar(
                Component.literal("Flying ").append(Component.literal("Disabled").red()).gold()
            )
        } else {
            player.abilities.mayfly = true
            player.sendToActionBar(
                Component.literal("Flying ").append(Component.literal("Enabled").lime()).gold()
            )
        }
        player.onUpdateAbilities()
    }
}