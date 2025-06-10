package me.supersanta.essential_addons.feature.extensions

import it.unimi.dsi.fastutil.objects.ReferenceOpenHashSet
import me.supersanta.essential_addons.EssentialAddons
import me.supersanta.essential_addons.feature.subscription.EssentialSubscription
import net.casual.arcade.events.GlobalEventHandler
import net.casual.arcade.events.ListenerRegistry.Companion.register
import net.casual.arcade.extensions.DataExtension
import net.casual.arcade.extensions.PlayerExtension
import net.casual.arcade.extensions.event.EntityExtensionEvent.Companion.getExtension
import net.casual.arcade.extensions.event.PlayerExtensionEvent
import net.minecraft.nbt.NbtOps
import net.minecraft.nbt.Tag
import net.minecraft.server.level.ServerPlayer
import kotlin.jvm.optionals.getOrNull

class PlayerSubscriptionsExtension(player: ServerPlayer): PlayerExtension(player), DataExtension {
    private val subscriptions = ReferenceOpenHashSet<EssentialSubscription>()

    fun has(subscription: EssentialSubscription): Boolean {
        return this.subscriptions.contains(subscription)
    }

    fun add(subscription: EssentialSubscription): Boolean {
        return this.subscriptions.add(subscription)
    }

    fun remove(subscription: EssentialSubscription): Boolean {
        return this.subscriptions.remove(subscription)
    }

    fun toggle(subscription: EssentialSubscription): Boolean {
        if (!this.add(subscription)) {
            this.remove(subscription)
            return false
        }
        return true
    }

    override fun getName(): String {
        return "${EssentialAddons.MOD_ID}_subscriptions_extension"
    }

    override fun deserialize(element: Tag) {
        val subscriptions = EssentialSubscription.SET_CODEC.parse(NbtOps.INSTANCE, element).result()
        if (subscriptions.isPresent) {
            this.subscriptions.addAll(subscriptions.get())
        }
    }

    override fun serialize(): Tag? {
        return EssentialSubscription.SET_CODEC.encodeStart(NbtOps.INSTANCE, this.subscriptions).result().getOrNull()
    }

    companion object {
        fun ServerPlayer.hasSubscription(subscription: EssentialSubscription): Boolean {
            return this.getExtension<PlayerSubscriptionsExtension>().has(subscription)
        }

        fun ServerPlayer.hasAvailableSubscription(subscription: EssentialSubscription): Boolean {
            return subscription.available() && this.hasSubscription(subscription)
        }

        fun ServerPlayer.toggleSubscription(subscription: EssentialSubscription): Boolean {
            return this.getExtension<PlayerSubscriptionsExtension>().toggle(subscription)
        }

        internal fun registerEvents() {
            GlobalEventHandler.Server.register<PlayerExtensionEvent> {
                it.addExtension(::PlayerSubscriptionsExtension)
            }
        }
    }
}