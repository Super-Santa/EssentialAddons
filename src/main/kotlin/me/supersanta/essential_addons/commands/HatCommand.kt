package me.supersanta.essential_addons.commands

import com.mojang.brigadier.builder.LiteralArgumentBuilder
import com.mojang.brigadier.context.CommandContext
import me.supersanta.essential_addons.EssentialSettings
import me.supersanta.essential_addons.utils.requires
import net.casual.arcade.commands.CommandTree
import net.casual.arcade.commands.executes
import net.minecraft.commands.CommandBuildContext
import net.minecraft.commands.CommandSourceStack
import net.minecraft.world.entity.EquipmentSlot

object HatCommand: CommandTree<CommandSourceStack> {
    override fun create(buildContext: CommandBuildContext): LiteralArgumentBuilder<CommandSourceStack> {
        return CommandTree.buildLiteral("hat") {
            requires(EssentialSettings::commandHat, "command.hat")
            executes(::setHat)
        }
    }

    private fun setHat(context: CommandContext<CommandSourceStack>) {
        val player = context.source.playerOrException
        val current = player.getItemBySlot(EquipmentSlot.HEAD).copy()
        val copy = player.mainHandItem.copyWithCount(1)
        player.mainHandItem.consume(1, player)
        player.setItemSlot(EquipmentSlot.HEAD, copy)
        if (!player.hasInfiniteMaterials()) {
            if (!player.inventory.add(current)) {
                val entity = player.drop(current, false)
                if (entity != null) {
                    entity.setNoPickUpDelay()
                    entity.setTarget(player.uuid)
                }
            }
        }
    }
}