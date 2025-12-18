package me.supersanta.essential_addons.commands

import com.mojang.brigadier.builder.LiteralArgumentBuilder
import com.mojang.brigadier.context.CommandContext
import me.supersanta.essential_addons.EssentialSettings
import me.supersanta.essential_addons.utils.requires
import net.casual.arcade.commands.CommandTree
import net.casual.arcade.commands.fail
import net.casual.arcade.commands.success
import net.casual.arcade.utils.component.gold
import net.minecraft.commands.CommandBuildContext
import net.minecraft.commands.CommandSourceStack
import net.minecraft.network.chat.Component

object RepairCommand: CommandTree {
    override fun create(buildContext: CommandBuildContext): LiteralArgumentBuilder<CommandSourceStack> {
        return CommandTree.buildLiteral("repair") {
            requires(EssentialSettings::commandRepair, "command.repair")
            executes(::repair)
        }
    }

    private fun repair(context: CommandContext<CommandSourceStack>): Int {
        val player = context.source.playerOrException
        val stack = player.mainHandItem
        if (stack.isDamaged) {
            stack.damageValue = 0
            return context.source.success(Component.literal("Item was repaired").gold())
        }
        return context.source.fail("Cannot repair item")
    }
}