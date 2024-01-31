package essentialaddons.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import essentialaddons.EssentialAddons;
import essentialaddons.EssentialSettings;
import essentialaddons.EssentialUtils;
import essentialaddons.utils.ConfigCamera;
import essentialaddons.utils.ConfigCameraData;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.Box;
import net.minecraft.world.GameMode;

import java.util.List;

import static essentialaddons.EssentialUtils.enabled;
import static net.minecraft.server.command.CommandManager.literal;

public class CommandCameraMode {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        LiteralArgumentBuilder<ServerCommandSource> csCommand = literal(ConfigCamera.INSTANCE.commandName);
        csCommand.requires(enabled(() -> EssentialSettings.commandCameraMode, "essentialaddons.command.cs")).executes(ctx -> {
            return toggle(ctx.getSource().getPlayerOrThrow());
        });
        dispatcher.register(csCommand);
    }

    private static int toggle(ServerPlayerEntity playerEntity) {
        return playerEntity.isSpectator() ? returnMode(playerEntity) : cameraMode(playerEntity);
    }

    private static int cameraMode(ServerPlayerEntity playerEntity) {
        if (EssentialSettings.cameraModeSurvivalRestrictions && isInDanger(playerEntity)) {
            return 0;
        }
        if (EssentialSettings.cameraModeRestoreLocation) {
            ConfigCameraData.INSTANCE.addPlayer(playerEntity);
        }
        playerEntity.changeGameMode(GameMode.SPECTATOR);
        EssentialUtils.sendToActionBar(playerEntity, "§6You have been put in §aSPECTATOR");
        return 1;
    }

    private static int returnMode(ServerPlayerEntity playerEntity) {
        GameMode previous = playerEntity.interactionManager.getPreviousGameMode();
        // The id is -1 if not set in previous versions of MC
        if (previous == null || previous.getId() < 0 || previous == GameMode.SPECTATOR) {
            // In the edge case, you do some funky stuff and your previous game mode
            // is also the same as your current game mode. Otherwise, you will get stuck in spectator.
            previous = GameMode.SURVIVAL;
        }
        if (EssentialSettings.cameraModeRestoreLocation && !ConfigCameraData.INSTANCE.restorePlayer(playerEntity)) {
            EssentialAddons.LOGGER.error("Could not load previous location for " + playerEntity.getNameForScoreboard());
            EssentialUtils.sendToActionBar(playerEntity, "§c[ERROR] Unable to get previous location");
            playerEntity.changeGameMode(previous);
            return 0;
        }
        EssentialUtils.sendToActionBar(playerEntity, "§6You have been put in §a" + previous);
        playerEntity.changeGameMode(previous);
        return 1;
    }

    private static boolean isInDanger(ServerPlayerEntity player) {
        if (player.isCreative()) {
            return false;
        }

        for (StatusEffectInstance effect : player.getStatusEffects()) {
            if (effect.getEffectType().getCategory() == StatusEffectCategory.HARMFUL) {
                EssentialUtils.sendToActionBar(player, "§cYou cannot enter spectator because you have a negative status effect");
                return true;
            }
        }
        double x = player.getX(), y = player.getY(), z = player.getZ();
        Box nearPlayer = new Box(x - 4,y - 4,z - 4,x + 4,y + 4, z + 4);
        List<HostileEntity> list = player.getServerWorld().getEntitiesByClass(HostileEntity.class, nearPlayer, e -> true);
        String reason;
        if (!list.isEmpty()) {
            reason = "there are mobs nearby";
        } else if (player.isOnFire()) {
            reason = "you are on fire";
        } else if (player.fallDistance > 0) {
            reason = "you are falling";
        } else if (player.isFallFlying()) {
            reason = "you are flying";
        } else if (player.isSubmergedInWater()) {
            reason = "you are under water";
        } else {
            return false;
        }
        EssentialUtils.sendToActionBar(player, "§cYou cannot enter spectator because " + reason);
        return true;
    }
}
