package me.supersanta.essential_addons.feature.teleport_blacklist

import me.supersanta.essential_addons.EssentialSettings
import me.supersanta.essential_addons.feature.extensions.PlayerSubscriptionsExtension.Companion.hasAvailableSubscription
import me.supersanta.essential_addons.feature.extensions.TeamTeleportBlacklistExtension.Companion.blacklistedFromTeleportingTo
import me.supersanta.essential_addons.feature.subscription.EssentialSubscriptions
import me.supersanta.essential_addons.utils.sendToActionBar
import net.casual.arcade.events.GlobalEventHandler
import net.casual.arcade.events.ListenerRegistry.Companion.register
import net.casual.arcade.events.server.player.PlayerSpectatorTeleportEvent
import net.casual.arcade.utils.ComponentUtils.gold
import net.casual.arcade.utils.ComponentUtils.red
import net.minecraft.network.chat.Component
import net.minecraft.server.level.ServerPlayer

object TeleportBlacklist {
    internal fun registerEvents() {
        GlobalEventHandler.Server.register<PlayerSpectatorTeleportEvent>(::onPlayerSpectatorTeleport)
    }

    private fun onPlayerSpectatorTeleport(event: PlayerSpectatorTeleportEvent) {
        val player = event.player
        if (player.hasPermissions(4)) {
            return
        }

        val target = event.getTarget()
        if (target !is ServerPlayer) {
            return
        }

        if (target.hasAvailableSubscription(EssentialSubscriptions.TELEPORT_BLACKLIST)) {
            player.sendToActionBar(
                Component.literal("This player has teleporting ").gold()
                    .append(Component.literal("DISABLED").red())
            )
            event.cancel()
            return
        }
        if (EssentialSettings.cameraModeTeamTeleportBlacklist) {
            val team = target.team ?: return
            if (team.blacklistedFromTeleportingTo) {
                player.sendToActionBar(
                    Component.literal("This player is on a team which you cannot teleport to").gold()
                )
                event.cancel()
            }
        }
    }
}