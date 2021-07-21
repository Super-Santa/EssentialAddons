package essentialaddons.commands;

import carpet.settings.SettingsManager;
import com.mojang.brigadier.CommandDispatcher;
import essentialaddons.EssentialAddonsServer;
import essentialaddons.EssentialAddonsSettings;
import essentialaddons.EssentialAddonsUtils;
import essentialaddons.helpers.CameraData;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.Box;
import net.minecraft.world.GameMode;

import java.util.List;

import static net.minecraft.server.command.CommandManager.literal;

public class CommandCameraMode {

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(literal("cs").requires((player) -> SettingsManager.canUseCommand(player, EssentialAddonsSettings.commandCameraMode)
                ).executes(context -> {
                    toggle(context.getSource().getPlayer());
                    return 0;
                }
                ).then(literal("clear").requires((player) -> player.hasPermissionLevel(4)
                ).executes(context -> {
                    CameraData.cameraData.remove(context.getSource().getPlayer().getUuid());
                    EssentialAddonsServer.LOGGER.info("Player " + context.getSource().getPlayer().getEntityName() + " has cleared their cs HashMap");
                    return 0;
                }))
        );
    }
    private static void cameraMode(ServerPlayerEntity playerEntity) {
        if (EssentialAddonsSettings.cameraModeSurvivalRestrictions && isInDanger(playerEntity))
            return;
        if (EssentialAddonsSettings.cameraModeRestoreLocation) {
            CameraData.cameraData.put(playerEntity.getUuid(), new CameraData(playerEntity));
        }
        playerEntity.setGameMode(GameMode.SPECTATOR);
        EssentialAddonsUtils.sendToActionBar(playerEntity, "§6You have been put in §aSPECTATOR");

    }
    private static void returnMode(ServerPlayerEntity playerEntity) {
        CameraData data = CameraData.cameraData.remove(playerEntity.getUuid());
        if (EssentialAddonsSettings.cameraModeRestoreLocation && data != null)
            data.restore(playerEntity);
        else if (EssentialAddonsSettings.cameraModeRestoreLocation) {
            EssentialAddonsServer.LOGGER.error("Could not load previous location for " + playerEntity.getEntityName());
            EssentialAddonsUtils.sendToActionBar(playerEntity, "§c[ERROR] Unable to get previous location");
            playerEntity.setGameMode(GameMode.SURVIVAL);
            return;
        }
        EssentialAddonsUtils.sendToActionBar(playerEntity, "§6You have been put in §a" + playerEntity.interactionManager.getPreviousGameMode());
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
    private static boolean isInDanger(ServerPlayerEntity playerEntity) {
        Box nearPlayer = new Box(playerEntity.getX()-4,playerEntity.getY()-4,playerEntity.getZ()-4,playerEntity.getX()+4,playerEntity.getY()+4,playerEntity.getZ()+4);
        List<HostileEntity> list = playerEntity.world.getEntitiesByClass(HostileEntity.class, nearPlayer, hostileEntity -> true);
        String reason;
        if (playerEntity.isOnFire())
            reason = "you are on fire";
        else if (playerEntity.hasStatusEffect(StatusEffects.POISON) || playerEntity.hasStatusEffect(StatusEffects.WITHER) || playerEntity.hasStatusEffect(StatusEffects.LEVITATION))
            reason = "you have a negative status effect";
        else if (playerEntity.fallDistance > 0)
            reason = "you are falling";
        else if (playerEntity.isFallFlying())
            reason = "you are flying";
        else if (!list.isEmpty())
            reason = "there are mobs nearby";
        else
            return false;
        EssentialAddonsUtils.sendToActionBar(playerEntity, "§cYou cannot enter spectator because " + reason);
        return true;
    }
}
