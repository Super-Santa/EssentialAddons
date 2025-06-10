package me.supersanta.essential_addons.feature.extensions

import com.mojang.serialization.Codec
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap
import me.supersanta.essential_addons.utils.essentialAddonsPath
import net.casual.arcade.events.GlobalEventHandler
import net.casual.arcade.events.ListenerRegistry.Companion.register
import net.casual.arcade.extensions.ExternalDataExtension
import net.casual.arcade.extensions.ExternalDataExtension.Companion.read
import net.casual.arcade.extensions.ExternalDataExtension.Companion.write
import net.casual.arcade.extensions.PlayerExtension
import net.casual.arcade.extensions.event.EntityExtensionEvent.Companion.getExtension
import net.casual.arcade.extensions.event.PlayerExtensionEvent
import net.casual.arcade.utils.math.location.LocationWithLevel
import net.minecraft.nbt.NbtOps
import net.minecraft.nbt.Tag
import net.minecraft.server.level.ServerLevel
import net.minecraft.server.level.ServerPlayer
import java.nio.file.Path
import kotlin.jvm.optionals.getOrNull

class PlayerWarpsExtension(player: ServerPlayer): PlayerExtension(player), ExternalDataExtension {
    private val warps = Object2ObjectOpenHashMap<String, LocationWithLevel.Resolvable>()

    init {
        this.read()
    }

    fun get(name: String): LocationWithLevel.Resolvable? {
        return this.warps[name]
    }

    fun add(name: String, location: LocationWithLevel<ServerLevel>) {
        this.warps[name] = location.resolvable()
        this.write()
    }

    fun remove(name: String): Boolean {
        val value = this.warps.remove(name)
        if (value != null) {
            this.write()
            return true
        }
        return false
    }

    override fun path(): Path {
        return this.player.server.essentialAddonsPath.resolve("warps")
            .resolve("${this.player.stringUUID}.nbt")
    }

    override fun deserialize(element: Tag) {
        val optional = WARP_CODEC.parse(NbtOps.INSTANCE, element).result()
        if (optional.isPresent) {
            this.warps.putAll(optional.get())
        }
    }

    override fun serialize(): Tag? {
        return WARP_CODEC.encodeStart(NbtOps.INSTANCE, this.warps).result().getOrNull()
    }

    companion object {
        private val WARP_CODEC = Codec.unboundedMap(Codec.STRING, LocationWithLevel.Resolvable.CODEC)

        fun ServerPlayer.getWarp(name: String): LocationWithLevel.Resolvable? {
            return this.getExtension<PlayerWarpsExtension>().get(name)
        }

        fun ServerPlayer.getWarpNames(): Collection<String> {
            return this.getExtension<PlayerWarpsExtension>().warps.keys
        }

        fun ServerPlayer.addWarp(name: String, location: LocationWithLevel<ServerLevel>) {
            this.getExtension<PlayerWarpsExtension>().add(name, location)
        }

        fun ServerPlayer.removeWrap(name: String): Boolean {
            return this.getExtension<PlayerWarpsExtension>().remove(name)
        }

        internal fun registerEvents() {
            GlobalEventHandler.Server.register<PlayerExtensionEvent> {
                it.addExtension(::PlayerWarpsExtension)
            }
        }
    }
}