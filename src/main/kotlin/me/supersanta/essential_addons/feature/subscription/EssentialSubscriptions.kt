package me.supersanta.essential_addons.feature.subscription

import me.supersanta.essential_addons.EssentialAddons
import me.supersanta.essential_addons.EssentialSettings
import me.supersanta.essential_addons.utils.EssentialRegistries
import net.minecraft.core.Registry

object EssentialSubscriptions {
    @JvmField val TELEPORT_BLACKLIST = this.register("teleport_blacklist", EssentialSettings::cameraModeTeleportBlacklist)
    @JvmField val CAREFUL_BREAK = this.register("careful_break", EssentialSettings::essentialCarefulBreak)
    @JvmField val CAREFUL_DROP = this.register("careful_drop", EssentialSettings::essentialCarefulDrop)
    @JvmField val ALWAYS_CAREFUL = this.register("always_careful") { CAREFUL_BREAK.available() || CAREFUL_DROP.available() }

    fun canUseSubscribeCommand(): Boolean {
        return EssentialRegistries.SUBSCRIPTION.any { it.available() }
    }

    internal fun load() {

    }

    private fun register(path: String, subscription: EssentialSubscription): EssentialSubscription {
        return Registry.register(EssentialRegistries.SUBSCRIPTION, EssentialAddons.id(path), subscription)
    }
}