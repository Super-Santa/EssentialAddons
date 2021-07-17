package essentialaddons.commands;

import carpet.settings.SettingsManager;
import com.mojang.brigadier.CommandDispatcher;
import essentialaddons.EssentialAddonsServer;
import essentialaddons.EssentialAddonsSettings;
import essentialaddons.utils.CameraData;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.GameMode;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

import static net.minecraft.server.command.CommandManager.literal;

public class CommandCameraMode {

    private static final Logger LOGGER = LogManager.getLogger("EssentialAddons|CameraData");

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(literal("cs").requires((player) -> SettingsManager.canUseCommand(player, EssentialAddonsSettings.commandCameraMode)
                ).executes(context -> {
                    toggle(context.getSource().getPlayer());
                    return 0;
                })
        );
    }
    private static void cameraMode(ServerPlayerEntity playerEntity) {
        if (EssentialAddonsSettings.cameraModeRestoreLocation) {
            EssentialAddonsServer.cameraData.put(playerEntity.getUuid(), new CameraData(playerEntity));
            try {
                CameraData.writeSaveFile(EssentialAddonsServer.cameraData);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        playerEntity.setGameMode(GameMode.SPECTATOR);
    }
    private static void returnMode(ServerPlayerEntity playerEntity) {
        CameraData data = EssentialAddonsServer.cameraData.remove(playerEntity.getUuid());
        if (data == null) {
            try {
                EssentialAddonsServer.cameraData = CameraData.readSaveFile();
                data = EssentialAddonsServer.cameraData.remove(playerEntity.getUuid());
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (EssentialAddonsSettings.cameraModeRestoreLocation && data != null)
            data.restore(playerEntity);
        else if (EssentialAddonsSettings.cameraModeRestoreLocation)
            LOGGER.error("Could not load previous location");
        playerEntity.setGameMode(playerEntity.interactionManager.getPreviousGameMode());
    }
    private static void toggle(ServerPlayerEntity playerEntity) {
        if (playerEntity.isSpectator()) {
            returnMode(playerEntity);
        }
        else {
            cameraMode(playerEntity);
        }
    }
}
