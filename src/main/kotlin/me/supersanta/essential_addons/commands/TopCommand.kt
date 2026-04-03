package me.supersanta.essential_addons.commands

import com.mojang.brigadier.builder.LiteralArgumentBuilder
import com.mojang.brigadier.context.CommandContext
import me.supersanta.essential_addons.EssentialSettings
import me.supersanta.essential_addons.utils.requires
import me.supersanta.essential_addons.utils.sendToActionBar
import net.casual.arcade.commands.CommandTree
import net.casual.arcade.commands.executes
import net.casual.arcade.utils.component.gold
import net.casual.arcade.utils.entity.teleportTo
import net.minecraft.commands.CommandBuildContext
import net.minecraft.commands.CommandSourceStack
import net.minecraft.network.chat.Component
import net.minecraft.world.level.levelgen.Heightmap

object TopCommand: CommandTree<CommandSourceStack> {
    override fun create(buildContext: CommandBuildContext): LiteralArgumentBuilder<CommandSourceStack> {
        return CommandTree.buildLiteral("top") {
            requires(EssentialSettings::commandTop, "command.top")
            executes(::teleportToTop)
        }
    }

    private fun teleportToTop(context: CommandContext<CommandSourceStack>) {
        val player = context.source.playerOrException
        val pos = player.level().getHeightmapPos(Heightmap.Types.MOTION_BLOCKING, player.blockPosition())
        player.teleportTo(pos.bottomCenter)
        player.sendToActionBar(Component.literal("You have been teleported to the top most block").gold())
    }
}