package me.supersanta.essential_addons.commands

import com.mojang.brigadier.builder.LiteralArgumentBuilder
import com.mojang.brigadier.context.CommandContext
import me.supersanta.essential_addons.EssentialSettings
import me.supersanta.essential_addons.utils.requires
import me.supersanta.essential_addons.utils.sendToActionBar
import net.casual.arcade.commands.CommandTree
import net.casual.arcade.commands.executes
import net.casual.arcade.utils.component.gold
import net.casual.arcade.utils.component.red
import net.minecraft.commands.CommandBuildContext
import net.minecraft.commands.CommandSourceStack
import net.minecraft.network.chat.Component

object MoreCommand: CommandTree {
    override fun create(buildContext: CommandBuildContext): LiteralArgumentBuilder<CommandSourceStack> {
        return CommandTree.buildLiteral("more") {
            requires(EssentialSettings::commandMore, "command.more")
            executes(::giveMore)
        }
    }

    private fun giveMore(context: CommandContext<CommandSourceStack>) {
        val player = context.source.playerOrException
        var stack = player.mainHandItem
        if (stack.isEmpty) {
            stack = player.offhandItem
        }
        if (stack.isEmpty) {
            player.sendToActionBar(Component.literal("You are not holding an item").red())
            return
        }
        stack.count = stack.maxStackSize
        player.sendToActionBar(Component.literal("You have been given a full stack").gold())
    }
}