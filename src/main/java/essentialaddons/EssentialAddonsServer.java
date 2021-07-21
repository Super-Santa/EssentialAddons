package essentialaddons;

import carpet.CarpetExtension;
import carpet.CarpetServer;
import com.mojang.brigadier.CommandDispatcher;
import essentialaddons.commands.*;
import essentialaddons.utils.CameraData;
import essentialaddons.utils.SubscribeData;
import net.fabricmc.api.ModInitializer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.ServerCommandSource;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

public class EssentialAddonsServer implements CarpetExtension, ModInitializer {

    public static Thread watchdogThread;

    public static final Logger LOGGER = LogManager.getLogger("EssentialAddons");

    @Override
    public String version() { return "essentialaddons"; }

    @Override
    public void onInitialize() { CarpetServer.manageExtension(new EssentialAddonsServer()); }

    @Override
    public void onGameStarted() { CarpetServer.settingsManager.parseSettingsClass(EssentialAddonsSettings.class); }

    @Override
    public void onServerLoaded(MinecraftServer server) {
        CarpetExtension.super.onServerLoaded(server);
        String dataResult;
        try {
            CameraData.cameraData = CameraData.readSaveFile();
            dataResult = "Successfully read camera data file";
        }
        catch (IOException e) {
            dataResult = "Failed to read camera data file";
        }
        if (EssentialAddonsSettings.commandCameraMode && EssentialAddonsSettings.cameraModeRestoreLocation)
            LOGGER.info(dataResult);
        try {
            SubscribeData.subscribeData = SubscribeData.readSaveFile();
            dataResult = "Successfully read subscribe data file";
        }
        catch (IOException e) {
            dataResult = "Failed to read subscribe data file";
        }
        if (EssentialAddonsSettings.essentialCarefulBreak)
            LOGGER.info(dataResult);
    }

    @Override
    public void onServerClosed(MinecraftServer server) {
        CarpetExtension.super.onServerClosed(server);
        try {
            CameraData.writeSaveFile(CameraData.cameraData);
        }
        catch (IOException e) {
            e.printStackTrace();
            LOGGER.error("Failed to write camera data file ");
        }
        LOGGER.info("Successfully wrote to camera data file ");
    }

    @Override
    public void registerCommands(CommandDispatcher<ServerCommandSource> dispatcher) {
        CommandFly.register(dispatcher);
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
        CommandSwitchDimensions.register(dispatcher);
        CommandEnderChest.register(dispatcher);
        CommandPublicViewDistance.register(dispatcher);
        CommandSubscribe.register(dispatcher);
        CommandLagSpike.register(dispatcher);
    }
}
