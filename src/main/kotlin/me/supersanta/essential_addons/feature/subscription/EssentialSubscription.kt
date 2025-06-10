package me.supersanta.essential_addons.feature.subscription

import com.mojang.serialization.Codec
import me.supersanta.essential_addons.utils.EssentialRegistries
import net.casual.arcade.utils.setOf

fun interface EssentialSubscription {
    fun available(): Boolean

    companion object {
        private val CODEC: Codec<EssentialSubscription> = Codec.lazyInitialized {
            EssentialRegistries.SUBSCRIPTION.byNameCodec()
        }

        val SET_CODEC = CODEC.setOf()
    }
}