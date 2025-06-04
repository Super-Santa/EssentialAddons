package me.supersanta.essential_addons.feature.extensions

import carpet.fakes.ServerPlayerInterface
import carpet.helpers.EntityPlayerActionPack.Action
import carpet.helpers.EntityPlayerActionPack.ActionType
import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import me.supersanta.essential_addons.EssentialAddons
import me.supersanta.essential_addons.EssentialSettings
import me.supersanta.essential_addons.mixins.feature.reload_fake_players.ActionAccessor
import me.supersanta.essential_addons.mixins.feature.reload_fake_players.EntityPlayerActionPackAccessor
import net.casual.arcade.events.GlobalEventHandler
import net.casual.arcade.events.ListenerRegistry.Companion.register
import net.casual.arcade.extensions.DataExtension
import net.casual.arcade.extensions.PlayerExtension
import net.casual.arcade.extensions.event.PlayerExtensionEvent
import net.casual.arcade.utils.codec.ArcadeExtraCodecs
import net.minecraft.nbt.NbtOps
import net.minecraft.nbt.Tag
import net.minecraft.server.level.ServerPlayer
import kotlin.jvm.optionals.getOrNull

class PlayerActionPackExtension(player: ServerPlayer): PlayerExtension(player), DataExtension {
    override fun getName(): String {
        return "${EssentialAddons.MOD_ID}_action_pack_extension"
    }

    override fun deserialize(element: Tag) {
        if (!EssentialSettings.savePlayerActions) {
            return
        }

        val data = PLAYER_ACTION_PACK_CODEC.parse(NbtOps.INSTANCE, element).result().getOrNull() ?: return
        val pack = (this.player as ServerPlayerInterface).actionPack
        pack.setSneaking(data.sneaking)
        pack.setSprinting(data.sprinting)
        pack.setForward(data.forward)
        pack.setStrafing(data.strafing)
        for ((type, action) in data.actions) {
            pack.start(type, action)
        }
    }

    override fun serialize(): Tag? {
        val pack = (this.player as ServerPlayerInterface).actionPack as EntityPlayerActionPackAccessor
        val data = ActionPackData(pack.isSneaking, pack.isSprinting, pack.forward, pack.strafing, pack.actions)
        return PLAYER_ACTION_PACK_CODEC.encodeStart(NbtOps.INSTANCE, data).result().getOrNull()
    }

    private data class ActionPackData(
        val sneaking: Boolean,
        val sprinting: Boolean,
        val forward: Float,
        val strafing: Float,
        val actions: Map<ActionType, Action>
    )

    companion object {
        private val ACTION_TYPE_CODEC = ArcadeExtraCodecs.enum<ActionType>()

        private val ACTION_CODEC: Codec<Action> = RecordCodecBuilder.create { instance ->
            instance.group(
                Codec.INT.fieldOf("limit").forGetter(Action::limit),
                Codec.INT.fieldOf("interval").forGetter(Action::interval),
                Codec.INT.fieldOf("offset").forGetter(Action::offset),
                Codec.INT.fieldOf("count").forGetter { (it as ActionAccessor).count },
                Codec.INT.fieldOf("next").forGetter { (it as ActionAccessor).next },
                Codec.BOOL.fieldOf("continuous").forGetter { (it as ActionAccessor).isContinuous }
            ).apply(instance) { limit, interval, offset, count, next, continuous ->
                val action = ActionAccessor.init(limit, interval, offset, continuous)
                (action as ActionAccessor).count = count
                (action as ActionAccessor).next = next
                action
            }
        }

        private val PLAYER_ACTION_PACK_CODEC: Codec<ActionPackData> = RecordCodecBuilder.create { instance ->
            instance.group(
                Codec.BOOL.fieldOf("sneaking").forGetter(ActionPackData::sneaking),
                Codec.BOOL.fieldOf("sprinting").forGetter(ActionPackData::sprinting),
                Codec.FLOAT.fieldOf("forward").forGetter(ActionPackData::forward),
                Codec.FLOAT.fieldOf("strafing").forGetter(ActionPackData::strafing),
                Codec.unboundedMap(ACTION_TYPE_CODEC, ACTION_CODEC).fieldOf("actions").forGetter(ActionPackData::actions)
            ).apply(instance, PlayerActionPackExtension::ActionPackData)
        }

        internal fun registerEvents() {
            GlobalEventHandler.Server.register<PlayerExtensionEvent> {
                it.addExtension(::PlayerActionPackExtension)
            }
        }
    }
}