package me.supersanta.essential_addons.commands

import com.mojang.brigadier.builder.LiteralArgumentBuilder
import com.mojang.brigadier.context.CommandContext
import me.supersanta.essential_addons.EssentialSettings
import me.supersanta.essential_addons.utils.requires
import me.supersanta.essential_addons.utils.sendToActionBar
import net.casual.arcade.commands.CommandTree
import net.casual.arcade.commands.executes
import net.casual.arcade.utils.ComponentUtils.gold
import net.casual.arcade.utils.ComponentUtils.red
import net.minecraft.commands.CommandBuildContext
import net.minecraft.commands.CommandSourceStack
import net.minecraft.network.chat.Component

object GodCommand: CommandTree {
    override fun create(buildContext: CommandBuildContext): LiteralArgumentBuilder<CommandSourceStack> {
        return CommandTree.buildLiteral("god") {
            requires(EssentialSettings::commandGod, "command.god")
            executes(::toggleInvulnerability)
        }
    }

    private fun toggleInvulnerability(context: CommandContext<CommandSourceStack>) {
        val player = context.source.playerOrException
        if (player.abilities.invulnerable) {
            player.abilities.invulnerable = false
            player.sendToActionBar(
                Component.literal("Invulnerability ").append(Component.literal("Disabled").red()).gold()
            )
        } else {
            player.abilities.invulnerable = true
            player.sendToActionBar(
                Component.literal("Invulnerability ").append(Component.literal("Enabled").red()).gold()
            )
        }
        player.onUpdateAbilities()
    }
}