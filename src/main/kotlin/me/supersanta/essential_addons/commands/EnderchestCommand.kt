package me.supersanta.essential_addons.commands

import com.mojang.brigadier.Command
import com.mojang.brigadier.CommandDispatcher
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import com.mojang.brigadier.context.CommandContext
import me.supersanta.essential_addons.EssentialSettings
import me.supersanta.essential_addons.utils.requires
import net.casual.arcade.commands.CommandTree
import net.casual.arcade.commands.registerLiteral
import net.minecraft.commands.CommandBuildContext
import net.minecraft.commands.CommandSourceStack
import net.minecraft.network.chat.Component
import net.minecraft.world.SimpleMenuProvider
import net.minecraft.world.inventory.ChestMenu

object EnderchestCommand: CommandTree<CommandSourceStack> {
    override fun register(dispatcher: CommandDispatcher<CommandSourceStack>, buildContext: CommandBuildContext) {
        dispatcher.registerLiteral("ec") {
            requires(EssentialSettings::commandEnderChest, "command.ec")
            executes(::openEnderchest)
        }
        super.register(dispatcher, buildContext)
    }

    override fun create(buildContext: CommandBuildContext): LiteralArgumentBuilder<CommandSourceStack> {
        return CommandTree.buildLiteral("enderchest") {
            requires(EssentialSettings::commandEnderChest, "command.enderchest")
            executes(::openEnderchest)
        }
    }

    private fun openEnderchest(context: CommandContext<CommandSourceStack>): Int {
        val player = context.source.playerOrException
        val enderchest = player.enderChestInventory
        player.openMenu(SimpleMenuProvider({ id, inventory, _ ->
            ChestMenu.threeRows(id, inventory, enderchest)
        }, Component.translatable("container.enderchest")))
        return Command.SINGLE_SUCCESS
    }
}