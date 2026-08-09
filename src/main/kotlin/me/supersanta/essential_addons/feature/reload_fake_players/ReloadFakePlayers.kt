package me.supersanta.essential_addons.feature.reload_fake_players

import carpet.patches.EntityPlayerMPFake
import carpet.patches.FakeClientConnection
import com.mojang.authlib.GameProfile
import me.supersanta.essential_addons.EssentialAddons
import me.supersanta.essential_addons.EssentialSettings
import me.supersanta.essential_addons.mixins.feature.reload_fake_players.EntityPlayerMPFakeInvoker
import net.casual.arcade.events.GlobalEventHandler
import net.casual.arcade.events.server.ServerSaveEvent
import net.casual.arcade.events.server.ServerStartEvent
import net.casual.arcade.events.server.ServerStopEvent
import net.casual.arcade.events.utils.register
import net.casual.arcade.utils.player.DynamicResolvableProfile
import net.casual.arcade.utils.player.resolveProfileOrNull
import net.minecraft.core.UUIDUtil
import net.minecraft.nbt.CompoundTag
import net.minecraft.nbt.NbtIo
import net.minecraft.network.protocol.PacketFlow
import net.minecraft.network.syncher.EntityDataAccessor
import net.minecraft.server.MinecraftServer
import net.minecraft.server.level.ClientInformation
import net.minecraft.server.level.ServerLevel
import net.minecraft.server.level.ServerPlayer
import net.minecraft.server.network.CommonListenerCookie
import net.minecraft.server.players.NameAndId
import net.minecraft.util.ProblemReporter
import net.minecraft.world.entity.player.Player
import net.minecraft.world.level.storage.LevelResource
import net.minecraft.world.level.storage.TagValueInput
import java.nio.file.Path
import java.util.*
import kotlin.io.path.exists
import kotlin.jvm.optionals.getOrNull

object ReloadFakePlayers {
    internal fun registerEvents() {
        GlobalEventHandler.Server.register<ServerStartEvent> { (server) ->
            this.loadFakePlayers(server)
        }
        GlobalEventHandler.Server.register<ServerSaveEvent> { event ->
            if (event.isRoutine) {
                this.saveFakePlayers(event.server)
            }
        }
        GlobalEventHandler.Server.register<ServerStopEvent> { (server) ->
            this.saveFakePlayers(server)
        }
    }

    private fun rejoin(server: MinecraftServer, uuid: UUID) {
        val resolvable = DynamicResolvableProfile(uuid)
        resolvable.resolveProfileOrNull(server.services().profileResolver).thenApplyAsync({ profile ->
            if (profile == null) {
                return@thenApplyAsync
            }

            val level = this.loadFakePlayerLevel(server, profile)
            val player = EntityPlayerMPFake.respawnFake(server, level, profile, ClientInformation.createDefault())
            server.playerList.placeNewPlayer(
                FakeClientConnection(PacketFlow.SERVERBOUND), player,
                CommonListenerCookie(profile, 0, player.clientInformation(), false)
            )
            EntityPlayerMPFakeInvoker.invokeLoadPlayerData(player)
            player.entityData.set(PlayerAccessor.getCustomizationAccessor(), 0x7F)
        }, server).whenComplete { _, throwable ->
            if (throwable != null) {
                EssentialAddons.logger.error("Failed to rejoin fake player $uuid", throwable)
            }
        }
    }

    private fun loadFakePlayerLevel(server: MinecraftServer, profile: GameProfile): ServerLevel? {
        @Suppress("DEPRECATION")
        val saved = server.playerList.loadPlayerData(NameAndId(profile))
            .map { tag -> TagValueInput.create(ProblemReporter.DISCARDING, server.registryAccess(), tag) }
            .flatMap { input -> input.read(ServerPlayer.SavedPosition.MAP_CODEC) }
            .orElse(ServerPlayer.SavedPosition.EMPTY)!!
        val respawn = server.worldData.overworldData().respawnData
        val level = saved.dimension.map(server::getLevel).orElseGet {
            server.getLevel(respawn.dimension()) ?: server.overworld()
        }
        return level
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

    @Suppress("KotlinUnreachableCode")
    private abstract class PlayerAccessor: Player(null!!, null!!) {
        companion object {
            fun getCustomizationAccessor(): EntityDataAccessor<Byte> {
                return DATA_PLAYER_MODE_CUSTOMISATION
            }
        }
    }
}