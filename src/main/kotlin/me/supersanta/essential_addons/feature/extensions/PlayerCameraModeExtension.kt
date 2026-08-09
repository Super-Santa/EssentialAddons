package me.supersanta.essential_addons.feature.extensions

import me.supersanta.essential_addons.EssentialAddons
import net.casual.arcade.events.GlobalEventHandler
import net.casual.arcade.events.utils.register
import net.casual.arcade.extensions.PlayerExtension
import net.casual.arcade.extensions.SerializableExtension
import net.casual.arcade.extensions.event.PlayerExtensionEvent
import net.casual.arcade.extensions.utils.getExtension
import net.casual.arcade.utils.math.location.LocationWithLevel
import net.casual.arcade.utils.player.server
import net.minecraft.resources.Identifier
import net.minecraft.server.level.ServerLevel
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.level.storage.ValueInput
import net.minecraft.world.level.storage.ValueOutput

class PlayerCameraModeExtension(player: ServerPlayer): PlayerExtension(player), SerializableExtension {
    private var location: LocationWithLevel<ServerLevel>? = null

    override fun id(): Identifier {
        return EssentialAddons.id("camera_mode")
    }

    override fun deserialize(input: ValueInput) {
        val resolvable = input.read("location", LocationWithLevel.Resolvable.CODEC)
        if (resolvable.isPresent) {
            this.location = resolvable.get().resolve(this.player.server)
        }
    }

    override fun serialize(output: ValueOutput) {
        val resolvable = this.location?.resolvable() ?: return
        output.store("location", LocationWithLevel.Resolvable.CODEC, resolvable)
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