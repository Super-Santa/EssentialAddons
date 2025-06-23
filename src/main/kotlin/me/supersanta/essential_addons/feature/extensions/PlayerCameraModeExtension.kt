package me.supersanta.essential_addons.feature.extensions

import me.supersanta.essential_addons.EssentialAddons
import net.casual.arcade.events.GlobalEventHandler
import net.casual.arcade.events.ListenerRegistry.Companion.register
import net.casual.arcade.extensions.DataExtension
import net.casual.arcade.extensions.PlayerExtension
import net.casual.arcade.extensions.event.EntityExtensionEvent.Companion.getExtension
import net.casual.arcade.extensions.event.PlayerExtensionEvent
import net.casual.arcade.utils.PlayerUtils.levelServer
import net.casual.arcade.utils.math.location.LocationWithLevel
import net.minecraft.nbt.NbtOps
import net.minecraft.nbt.Tag
import net.minecraft.server.level.ServerLevel
import net.minecraft.server.level.ServerPlayer
import kotlin.jvm.optionals.getOrNull

class PlayerCameraModeExtension(player: ServerPlayer): PlayerExtension(player), DataExtension {
    private var location: LocationWithLevel<ServerLevel>? = null

    override fun getName(): String {
        return "${EssentialAddons.MOD_ID}_camera_mode_extension"
    }

    override fun deserialize(element: Tag) {
        val resolvable = LocationWithLevel.Resolvable.CODEC.parse(NbtOps.INSTANCE, element).result()
        if (resolvable.isPresent) {
            this.location = resolvable.get().resolve(this.player.levelServer)
        }
    }

    override fun serialize(): Tag? {
        val resolvable = this.location?.resolvable() ?: return null
        return LocationWithLevel.Resolvable.CODEC.encodeStart(NbtOps.INSTANCE, resolvable).result().getOrNull()
    }

    companion object {
        @JvmStatic
        var ServerPlayer.cameraModeLocation: LocationWithLevel<ServerLevel>?
            get() = this.getExtension<PlayerCameraModeExtension>().location
            set(value) { this.getExtension<PlayerCameraModeExtension>().location = value }

        internal fun registerEvents() {
            GlobalEventHandler.Server.register<PlayerExtensionEvent> {
                it.addExtension(::PlayerCameraModeExtension)
            }
        }
    }
}