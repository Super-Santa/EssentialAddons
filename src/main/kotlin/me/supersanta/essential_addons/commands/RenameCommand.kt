package me.supersanta.essential_addons.commands

import com.mojang.brigadier.arguments.StringArgumentType
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import com.mojang.brigadier.context.CommandContext
import me.supersanta.essential_addons.EssentialSettings
import me.supersanta.essential_addons.utils.requires
import net.casual.arcade.commands.*
import net.casual.arcade.utils.component.gold
import net.casual.arcade.utils.component.unitalicize
import net.casual.arcade.utils.component.wrap
import net.minecraft.commands.CommandBuildContext
import net.minecraft.commands.CommandSourceStack
import net.minecraft.commands.arguments.ComponentArgument
import net.minecraft.core.component.DataComponents
import net.minecraft.network.chat.Component

object RenameCommand: CommandTree {
    override fun create(buildContext: CommandBuildContext): LiteralArgumentBuilder<CommandSourceStack> {
        return CommandTree.buildLiteral("rename") {
            requires(EssentialSettings::commandRename, "command.rename")
            literal("nbt") {
                argument("name", ComponentArgument.textComponent(buildContext)) {
                    executes(::renameWithNbt)
                }
            }
            literal("literal") {
                argument("name", StringArgumentType.greedyString()) {
                    executes(::renameWithLiteral)
                }
            }
        }
    }

    private fun renameWithNbt(context: CommandContext<CommandSourceStack>): Int {
        val stack = context.source.playerOrException.mainHandItem
        if (stack.isEmpty) {
            return context.source.fail("Cannot rename air!")
        }
        val name = ComponentArgument.getRawComponent(context, "name")
        stack.set(DataComponents.CUSTOM_NAME, name)
        return context.source.success(Component.literal("Item name set to: ").gold().wrap().append(name))
    }

    private fun renameWithLiteral(context: CommandContext<CommandSourceStack>): Int {
        val stack = context.source.playerOrException.mainHandItem
        if (stack.isEmpty) {
            return context.source.fail("Cannot rename air!")
        }
        val name = Component.literal(StringArgumentType.getString(context, "name")).unitalicize()
        stack.set(DataComponents.CUSTOM_NAME, name)
        return context.source.success(Component.literal("Item name set to: ").gold().wrap().append(name))
    }
}