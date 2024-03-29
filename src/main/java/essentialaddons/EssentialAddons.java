package essentialaddons;

import carpet.CarpetExtension;
import carpet.CarpetServer;
import carpet.script.CarpetExpression;
import com.mojang.brigadier.CommandDispatcher;
import essentialaddons.commands.*;
import essentialaddons.feature.GameRuleNetworkHandler;
import essentialaddons.feature.ReloadFakePlayers;
import essentialaddons.feature.script.PacketEvent;
import essentialaddons.feature.script.ScriptPacketHandler;
import essentialaddons.utils.*;
import essentialaddons.logging.EssentialAddonsLoggerRegistry;
import net.fabricmc.api.ModInitializer;
//#if MC >= 11900
import net.minecraft.command.CommandRegistryAccess;
//#endif
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map;
import java.util.Set;

public class EssentialAddons implements CarpetExtension, ModInitializer {
    public static final Logger LOGGER;
    public static final Set<Config> CONFIG_SET;
    public static final Set<NetworkHandler> NETWORK_HANDLERS;
    public static MinecraftServer server;

    static {
        LOGGER = LogManager.getLogger("EssentialAddons");
        CONFIG_SET = Set.of(
            ConfigCameraData.INSTANCE,
            ConfigSubscribeData.INSTANCE,
            ConfigFakePlayerData.INSTANCE,
            ConfigTeamTeleportBlacklist.INSTANCE
        );
        NETWORK_HANDLERS = Set.of(
            GameRuleNetworkHandler.INSTANCE,
            ScriptPacketHandler.INSTANCE
        );
    }

    @Override
    public void onInitialize() {
        CarpetServer.manageExtension(this);
        ConfigCamera.INSTANCE.readConfig();
    }

    @Override
    public String version() {
        return "essentialaddons";
    }

    @Override
    public void onGameStarted() {
        CarpetServer.settingsManager.parseSettingsClass(EssentialSettings.class);
    }

    @Override
    public void registerLoggers() {
        EssentialAddonsLoggerRegistry.registerLoggers();
    }

    @Override
    public void scarpetApi(CarpetExpression expression) {
        ScriptPacketHandler.INSTANCE.addScarpetExpression(expression.getExpr());
    }

    @Override
    public void onServerLoaded(MinecraftServer minecaraftServer) {
        server = minecaraftServer;
        for (Config config : CONFIG_SET) {
            config.readConfig();
        }
    }

    @Override
    public void onServerLoadedWorlds(MinecraftServer server) {
        if (EssentialSettings.reloadFakePlayers) {
            ReloadFakePlayers.loadFakePlayers(server);
        }
        PacketEvent.noop();
    }

    @Override
    public void onServerClosed(MinecraftServer server) {
        for (Config config : CONFIG_SET) {
            config.saveConfig();
        }
    }

    @Override
    //#if MC >= 11900
    public void registerCommands(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess commandBuildContext) {
        //#else
        //$$public void registerCommands(CommandDispatcher<ServerCommandSource> dispatcher) {
        //#endif
        CommandRegion.register(dispatcher);
        CommandFly.register(dispatcher);
        CommandHat.register(dispatcher);
        CommandRepair.register(dispatcher);
        CommandGM.register(dispatcher);
        CommandHeal.register(dispatcher);
        CommandExtinguish.register(dispatcher);
        CommandGod.register(dispatcher);
        CommandDefuse.register(dispatcher);
        CommandMore.register(dispatcher);
        CommandStrength.register(dispatcher);
        CommandNightVision.register(dispatcher);
        CommandDimensions.register(dispatcher);
        CommandWarp.register(dispatcher);
        CommandCameraMode.register(dispatcher);
        CommandBackup.register(dispatcher);
        CommandSwitchDimensions.register(dispatcher);
        CommandEnderChest.register(dispatcher);
        CommandWorkbench.register(dispatcher);
        CommandPublicViewDistance.register(dispatcher);
        CommandSubscribe.register(dispatcher);
        CommandTop.register(dispatcher);
        CommandNear.register(dispatcher);
        CommandLagSpike.register(dispatcher);
        CommandRename.register(dispatcher);
        CommandMods.register(dispatcher);
        CommandGhostPlayer.register(dispatcher);
        CommandConfig.register(dispatcher);
        CommandTeamTeleportBlacklist.register(dispatcher);
    }

    @Override
    public void onPlayerLoggedIn(ServerPlayerEntity player) {
        NETWORK_HANDLERS.forEach(networkHandler -> networkHandler.sayHello(player));
    }

    @Override
    public Map<String, String> canHasTranslations(String lang) {
        return EssentialUtils.getTranslations(lang);
    }
}
