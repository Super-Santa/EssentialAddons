package me.supersanta.essential_addons.commands

import com.mojang.brigadier.builder.LiteralArgumentBuilder
import com.mojang.brigadier.context.CommandContext
import me.supersanta.essential_addons.EssentialSettings
import me.supersanta.essential_addons.utils.requires
import net.casual.arcade.commands.CommandTree
import net.casual.arcade.commands.fail
import net.casual.arcade.commands.success
import net.casual.arcade.utils.ComponentUtils.aqua
import net.casual.arcade.utils.ComponentUtils.gold
import net.casual.arcade.utils.ComponentUtils.join
import net.casual.arcade.utils.ComponentUtils.lime
import net.fabricmc.loader.api.FabricLoader
import net.fabricmc.loader.api.metadata.ModOrigin
import net.fabricmc.loader.impl.metadata.AbstractModMetadata
import net.minecraft.commands.CommandBuildContext
import net.minecraft.commands.CommandSourceStack
import net.minecraft.network.chat.Component

object ModsCommand: CommandTree {
    override fun create(buildContext: CommandBuildContext): LiteralArgumentBuilder<CommandSourceStack> {
        return CommandTree.buildLiteral("mods") {
            requires(EssentialSettings::commandMods, "command.mods")
            executes(::displayLoadedMods)
        }
    }

    private fun displayLoadedMods(context: CommandContext<CommandSourceStack>): Int {
        val mods = this.getLoadedMods()
        if (mods.isEmpty()) {
            return context.source.fail("There are no mods installed")
        }
        val formatted = mods.map { (key, value) ->
            Component.literal(key).append(" ").append(Component.literal(value).aqua()).lime()
        }.join()
        return context.source.success(
            Component.literal("Installed mods: ").append(formatted).gold()
        )
    }

    private fun getLoadedMods(): Map<String, String> {
        return FabricLoader.getInstance().allMods
            .filter { it.origin.kind != ModOrigin.Kind.NESTED }
            .filter { it.metadata.type != AbstractModMetadata.TYPE_BUILTIN }
            .associateBy({ it.metadata.id }, { it.metadata.version.friendlyString })
    }
}