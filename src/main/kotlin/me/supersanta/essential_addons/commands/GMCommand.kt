package me.supersanta.essential_addons.commands

import com.mojang.brigadier.Command
import com.mojang.brigadier.CommandDispatcher
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import me.supersanta.essential_addons.EssentialSettings
import me.supersanta.essential_addons.utils.requires
import net.casual.arcade.commands.CommandTree
import net.casual.arcade.commands.registerLiteral
import net.minecraft.commands.CommandBuildContext
import net.minecraft.commands.CommandSourceStack
import net.minecraft.world.level.GameType

object GMCommand: CommandTree {
    override fun register(dispatcher: CommandDispatcher<CommandSourceStack>, buildContext: CommandBuildContext) {
        dispatcher.registerLiteral("gms") {
            requires(EssentialSettings::commandGM, "command.gms")
            executes(changeGameModeTo(GameType.SURVIVAL))
        }
        dispatcher.registerLiteral("gma") {
            requires(EssentialSettings::commandGM, "command.gma")
            executes(changeGameModeTo(GameType.ADVENTURE))
        }
        dispatcher.registerLiteral("gmsp") {
            requires(EssentialSettings::commandGM, "command.gmsp")
            executes(changeGameModeTo(GameType.SPECTATOR))
        }
        super.register(dispatcher, buildContext)
    }

    override fun create(buildContext: CommandBuildContext): LiteralArgumentBuilder<CommandSourceStack> {
        return CommandTree.buildLiteral("gmc") {
            requires(EssentialSettings::commandGM, "command.gmc")
            executes(changeGameModeTo(GameType.CREATIVE))
        }
    }

    private fun changeGameModeTo(type: GameType): Command<CommandSourceStack> {
        return Command { context ->
            context.source.playerOrException.setGameMode(type)
            Command.SINGLE_SUCCESS
        }
    }
}