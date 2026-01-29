package me.supersanta.essential_addons.feature.extensions

import it.unimi.dsi.fastutil.objects.ReferenceOpenHashSet
import me.supersanta.essential_addons.EssentialAddons
import me.supersanta.essential_addons.feature.subscription.EssentialSubscription
import net.casual.arcade.events.GlobalEventHandler
import net.casual.arcade.events.ListenerRegistry.Companion.register
import net.casual.arcade.extensions.PlayerExtension
import net.casual.arcade.extensions.SerializableExtension
import net.casual.arcade.extensions.event.PlayerExtensionEvent
import net.casual.arcade.extensions.utils.getExtension
import net.minecraft.resources.Identifier
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.level.storage.ValueInput
import net.minecraft.world.level.storage.ValueOutput

class PlayerSubscriptionsExtension(player: ServerPlayer): PlayerExtension(player), SerializableExtension {
    private val subscriptions = ReferenceOpenHashSet<EssentialSubscription>()

    override fun id(): Identifier {
        return EssentialAddons.id("subscriptions")
    }

    override fun deserialize(input: ValueInput) {
        val subscriptions = input.read("subscriptions", EssentialSubscription.SET_CODEC)
        if (subscriptions.isPresent) {
            this.subscriptions.addAll(subscriptions.get())
        }
    }

    override fun serialize(output: ValueOutput) {
        output.store("subscriptions", EssentialSubscription.SET_CODEC, this.subscriptions)
    }

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