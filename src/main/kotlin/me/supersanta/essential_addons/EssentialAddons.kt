package me.supersanta.essential_addons

import carpet.CarpetExtension
import carpet.CarpetServer
import com.mojang.serialization.Codec
import kotlinx.io.IOException
import me.supersanta.essential_addons.commands.*
import me.supersanta.essential_addons.feature.careful_break.CarefulBreak
import me.supersanta.essential_addons.feature.extensions.*
import me.supersanta.essential_addons.feature.logging.AutoSaveLogger
import me.supersanta.essential_addons.feature.reload_fake_players.ReloadFakePlayers
import me.supersanta.essential_addons.feature.remove_after_threshold.RemoveAfterThreshold
import me.supersanta.essential_addons.feature.teleport_blacklist.TeleportBlacklist
import me.supersanta.essential_addons.utils.EssentialRegistries
import net.casual.arcade.commands.manager.CommandManager
import net.casual.arcade.commands.manager.GlobalCommandManager
import net.casual.arcade.utils.Identifier
import net.casual.arcade.utils.JsonUtils
import net.fabricmc.api.ModInitializer
import net.fabricmc.loader.api.FabricLoader
import net.minecraft.resources.Identifier
import net.minecraft.server.MinecraftServer
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger

object EssentialAddons: ModInitializer, CarpetExtension {
    const val MOD_ID = "essential-addons"

    private var commands: CommandManager? = null

    val container = FabricLoader.getInstance().getModContainer(MOD_ID).get()

    val logger: Logger = LogManager.getLogger("EssentialAddons")

    override fun onInitialize() {
        CarpetServer.manageExtension(this)

        EssentialRegistries.load()

        CarefulBreak.registerEvents()
        ReloadFakePlayers.registerEvents()
        RemoveAfterThreshold.registerEvents()
        TeleportBlacklist.registerEvents()

        PlayerActionPackExtension.registerEvents()
        PlayerCameraModeExtension.registerEvents()
        PlayerSubscriptionsExtension.registerEvents()
        PlayerWarpsExtension.registerEvents()
        TeamTeleportBlacklistExtension.registerEvents()
    }

    override fun onGameStarted() {
        CarpetServer.settingsManager.parseSettingsClass(EssentialSettings::class.java)
    }

    override fun onServerLoaded(server: MinecraftServer) {
        this.registerCommands(server)
    }

    override fun registerLoggers() {
        AutoSaveLogger.register()
    }

    override fun version(): String {
        return MOD_ID
    }

    override fun canHasTranslations(lang: String): Map<String, String> {
        val path = this.container.findPath("assets/essential-addons/lang/$lang.json")
        if (path.isEmpty) {
            return mapOf()
        }
        val codec = Codec.unboundedMap(Codec.STRING, Codec.STRING)
        try {
            val result = JsonUtils.decodeWith(codec, path.get())
            return result.result().orElseGet { mapOf() }
        } catch (_: IOException) {
            return mapOf()
        }
    }

    fun id(path: String): Identifier {
        return Identifier(MOD_ID, path)
    }

    @JvmStatic
    fun permission(permission: String): String {
        return "${MOD_ID}.${permission}"
    }

    fun registerCommands(server: MinecraftServer) {
        val manager = CommandManager(server)
        manager.register(BackupCommand)
        manager.register(CameraModeCommand)
        manager.register(DefuseCommand)
        manager.register(EnderchestCommand)
        manager.register(ExtinguishCommand)
        manager.register(FlyCommand)
        manager.register(GMCommand)
        manager.register(GodCommand)
        manager.register(HatCommand)
        manager.register(HealCommand)
        manager.register(LagSpikeCommand)
        manager.register(ModsCommand)
        manager.register(MoreCommand)
        manager.register(NearCommand)
        manager.register(NightVisionCommand)
        manager.register(RenameCommand)
        manager.register(RepairCommand)
        manager.register(StrengthCommand)
        manager.register(SubscribeCommand)
        manager.register(TeamTeleportBlacklistCommand)
        manager.register(TopCommand)
        manager.register(ViewDistanceCommand)
        manager.register(WarpCommand)
        manager.register(WorkbenchCommand)

        val old = this.commands
        if (old != null) {
            GlobalCommandManager.removeManager(old)
        }
        GlobalCommandManager.addManager(manager)
        this.commands = manager
    }
}