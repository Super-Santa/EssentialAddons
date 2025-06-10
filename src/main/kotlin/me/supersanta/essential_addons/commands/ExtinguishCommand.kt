package me.supersanta.essential_addons.commands

import com.mojang.brigadier.Command
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import com.mojang.brigadier.context.CommandContext
import me.supersanta.essential_addons.EssentialSettings
import me.supersanta.essential_addons.utils.requires
import me.supersanta.essential_addons.utils.sendToActionBar
import net.casual.arcade.commands.CommandTree
import net.casual.arcade.utils.ComponentUtils.gold
import net.casual.arcade.utils.ComponentUtils.red
import net.minecraft.commands.CommandBuildContext
import net.minecraft.commands.CommandSourceStack
import net.minecraft.network.chat.Component

object ExtinguishCommand: CommandTree {
    override fun create(buildContext: CommandBuildContext): LiteralArgumentBuilder<CommandSourceStack> {
        return CommandTree.buildLiteral("extinguish") {
            requires(EssentialSettings::commandExtinguish, "command.extinguish")
            executes(::extinguish)
        }
    }

    private fun extinguish(context: CommandContext<CommandSourceStack>): Int {
        val player = context.source.playerOrException
        if (player.isOnFire) {
            player.extinguishFire()
            player.sendToActionBar(Component.literal("You have been extinguished").gold())
        } else {
            player.sendToActionBar(Component.literal("You are not on fire").red())
        }
        return Command.SINGLE_SUCCESS
    }
}