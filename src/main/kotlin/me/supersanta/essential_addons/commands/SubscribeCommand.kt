package me.supersanta.essential_addons.commands

import com.mojang.brigadier.builder.LiteralArgumentBuilder
import com.mojang.brigadier.context.CommandContext
import me.supersanta.essential_addons.feature.extensions.PlayerSubscriptionsExtension.Companion.hasSubscription
import me.supersanta.essential_addons.feature.extensions.PlayerSubscriptionsExtension.Companion.toggleSubscription
import me.supersanta.essential_addons.feature.subscription.EssentialSubscription
import me.supersanta.essential_addons.feature.subscription.EssentialSubscriptions
import me.supersanta.essential_addons.utils.EssentialRegistryKeys
import me.supersanta.essential_addons.utils.sendToActionBar
import net.casual.arcade.commands.CommandTree
import net.casual.arcade.commands.argument
import net.casual.arcade.commands.arguments.RegistryElementArgument
import net.casual.arcade.commands.executes
import net.casual.arcade.commands.literal
import net.casual.arcade.utils.component.aqua
import net.casual.arcade.utils.component.gold
import net.casual.arcade.utils.component.lime
import net.casual.arcade.utils.component.red
import net.casual.arcade.utils.toIdString
import net.minecraft.commands.CommandBuildContext
import net.minecraft.commands.CommandSourceStack
import net.minecraft.network.chat.Component

object SubscribeCommand: CommandTree<CommandSourceStack> {
    override fun create(buildContext: CommandBuildContext): LiteralArgumentBuilder<CommandSourceStack> {
        return CommandTree.buildLiteral("subscribe") {
            requires { EssentialSubscriptions.canUseSubscribeCommand() }
            literal("toggle") {
                argument("subscription", RegistryElementArgument.element(EssentialRegistryKeys.SUBSCRIPTION) { _, v -> v.available() }) {
                    executes(::toggleSubscription)
                }
            }
            literal("query") {
                argument("subscription", RegistryElementArgument.element(EssentialRegistryKeys.SUBSCRIPTION) { _, v -> v.available() }) {
                    executes(::querySubscription)
                }
            }
        }
    }

    private fun toggleSubscription(context: CommandContext<CommandSourceStack>) {
        val player = context.source.playerOrException
        val holder = RegistryElementArgument.getHolder<EssentialSubscription>(context, "subscription")
        val enabled = player.toggleSubscription(holder.value())
        val prefix = if (enabled) {
            Component.literal("SUBSCRIBED").lime()
                .append(" ").append(Component.literal("to").gold())
        } else {
            Component.literal("UNSUBSCRIBED").red()
                .append(" ").append(Component.literal("from").gold())
        }
        val formatted = Component.literal(holder.key().toIdString()).aqua()
        player.sendToActionBar(prefix.append(" ").append(formatted))
    }

    private fun querySubscription(context: CommandContext<CommandSourceStack>) {
        val player = context.source.playerOrException
        val holder = RegistryElementArgument.getHolder<EssentialSubscription>(context, "subscription")
        val formatted = Component.literal(holder.key().toIdString()).aqua()
        if (player.hasSubscription(holder.value())) {
            player.sendToActionBar(Component.literal("You are subscribed to ").append(formatted).gold())
        } else {
            player.sendToActionBar(Component.literal("You are not subscribed to ").append(formatted).gold())
        }
    }
}