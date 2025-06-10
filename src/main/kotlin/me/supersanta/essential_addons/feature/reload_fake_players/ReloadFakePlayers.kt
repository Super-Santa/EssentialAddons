package me.supersanta.essential_addons.feature.reload_fake_players

import carpet.patches.EntityPlayerMPFake
import carpet.patches.FakeClientConnection
import com.mojang.authlib.properties.PropertyMap
import me.supersanta.essential_addons.EssentialAddons
import me.supersanta.essential_addons.EssentialSettings
import net.casual.arcade.events.GlobalEventHandler
import net.casual.arcade.events.ListenerRegistry.Companion.register
import net.casual.arcade.events.server.ServerLoadedEvent
import net.casual.arcade.events.server.ServerSaveEvent
import net.casual.arcade.events.server.ServerStoppingEvent
import net.minecraft.core.UUIDUtil
import net.minecraft.nbt.CompoundTag
import net.minecraft.nbt.NbtIo
import net.minecraft.network.protocol.PacketFlow
import net.minecraft.network.syncher.EntityDataAccessor
import net.minecraft.server.MinecraftServer
import net.minecraft.server.level.ClientInformation
import net.minecraft.server.network.CommonListenerCookie
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.component.ResolvableProfile
import net.minecraft.world.level.storage.LevelResource
import java.nio.file.Path
import java.util.*
import kotlin.io.path.exists
import kotlin.jvm.optionals.getOrNull

object ReloadFakePlayers {
    internal fun registerEvents() {
        GlobalEventHandler.Server.register<ServerLoadedEvent> { (server) ->
            this.loadFakePlayers(server)
        }
        GlobalEventHandler.Server.register<ServerSaveEvent> { (server, stopping) ->
            if (!stopping) {
                this.saveFakePlayers(server)
            }
        }
        GlobalEventHandler.Server.register<ServerStoppingEvent> { (server) ->
            this.saveFakePlayers(server)
        }
    }

    private fun rejoin(server: MinecraftServer, uuid: UUID) {
        val resolvable = ResolvableProfile(Optional.empty(), Optional.of(uuid), PropertyMap())
        resolvable.resolve().thenApplyAsync({ resolved ->
            if (resolved.isResolved) resolved.gameProfile else null
        }, server).thenApply { profile ->
            if (profile == null) {
                return@thenApply
            }

            val player = EntityPlayerMPFake.respawnFake(
                server, server.overworld(), profile, ClientInformation.createDefault()
            )
            player.entityData.set(PlayerAccessor.getCustomizationAccessor(), 0x7F)
            server.playerList.placeNewPlayer(
                FakeClientConnection(PacketFlow.SERVERBOUND), player,
                CommonListenerCookie(profile, 0, player.clientInformation(), false)
            )
        }
    }

    private fun loadFakePlayers(server: MinecraftServer) {
        val path = this.getFakePlayerDat(server)
        if (!EssentialSettings.reloadFakePlayers || !path.exists()) {
            return
        }

        try {
            val wrapper = NbtIo.read(path) ?: return
            val players = wrapper.read("players", UUIDUtil.STRING_CODEC.listOf()).getOrNull()
            if (players != null) {
                for (player in players) {
                    this.rejoin(server, player)
                }
            }
        } catch (e: Exception) {
            EssentialAddons.logger.error("Failed to load fake players", e)
        }
    }

    private fun saveFakePlayers(server: MinecraftServer) {
        val players = ArrayList<UUID>()
        for (player in server.playerList.players) {
            if (player !is EntityPlayerMPFake) {
                continue
            }
            players.add(player.uuid)
        }

        val wrapper = CompoundTag()
        wrapper.store("players", UUIDUtil.STRING_CODEC.listOf(), players)
        try {
            NbtIo.write(wrapper, this.getFakePlayerDat(server))
        } catch (e: Exception) {
            EssentialAddons.logger.error("Failed to save fake players", e)
        }
    }

    private fun getFakePlayerDat(server: MinecraftServer): Path {
        return server.getWorldPath(LevelResource.ROOT).resolve("reload-fake-players.dat")
    }

    private abstract class PlayerAccessor: Player(null!!, null!!, 0.0F, null!!) {
        companion object {
            fun getCustomizationAccessor(): EntityDataAccessor<Byte> {
                return DATA_PLAYER_MODE_CUSTOMISATION
            }
        }
    }
}