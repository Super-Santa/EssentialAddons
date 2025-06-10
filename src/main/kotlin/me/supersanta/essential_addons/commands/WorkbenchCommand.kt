package me.supersanta.essential_addons.commands

import com.mojang.brigadier.builder.LiteralArgumentBuilder
import com.mojang.brigadier.context.CommandContext
import me.supersanta.essential_addons.EssentialSettings
import me.supersanta.essential_addons.utils.requires
import net.casual.arcade.commands.CommandTree
import net.casual.arcade.commands.executes
import net.minecraft.commands.CommandBuildContext
import net.minecraft.commands.CommandSourceStack
import net.minecraft.network.chat.Component
import net.minecraft.world.SimpleMenuProvider
import net.minecraft.world.entity.player.Player
import net.minecraft.world.inventory.ContainerLevelAccess
import net.minecraft.world.inventory.CraftingMenu

object WorkbenchCommand: CommandTree {
    override fun create(buildContext: CommandBuildContext): LiteralArgumentBuilder<CommandSourceStack> {
        return CommandTree.buildLiteral("workbench") {
            requires(EssentialSettings::commandWorkbench, "command.workbench")
            executes(::openWorkbench)
        }
    }

    private fun openWorkbench(context: CommandContext<CommandSourceStack>) {
        context.source.playerOrException.openMenu(SimpleMenuProvider({ id, inventory, player ->
            object: CraftingMenu(id, inventory, ContainerLevelAccess.create(player.level(), player.blockPosition())) {
                override fun stillValid(player: Player): Boolean {
                    return true
                }
            }
        }, Component.translatable("container.crafting")))
    }
}