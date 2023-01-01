package essentialaddons.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import essentialaddons.EssentialAddons;
import essentialaddons.EssentialSettings;
import essentialaddons.EssentialUtils;
import essentialaddons.utils.ConfigCamera;
import essentialaddons.utils.ConfigCameraData;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.Box;
import net.minecraft.world.GameMode;

import java.util.List;
import java.util.Set;

//#if MC >= 11900
import static carpet.utils.CommandHelper.canUseCommand;
//#else
//$$import static carpet.settings.SettingsManager.canUseCommand;
//#endif
import static net.minecraft.server.command.CommandManager.literal;

public class CommandCameraMode {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        LiteralArgumentBuilder<ServerCommandSource> csCommand = literal(ConfigCamera.INSTANCE.commandName);
        csCommand.requires((p) -> canUseCommand(p, EssentialSettings.commandCameraMode)).executes(ctx -> {
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
        if (EssentialSettings.cameraModeRestoreLocation && !ConfigCameraData.INSTANCE.restorePlayer(playerEntity)) {
            EssentialAddons.LOGGER.error("Could not load previous location for " + playerEntity.getEntityName());
            EssentialUtils.sendToActionBar(playerEntity, "§c[ERROR] Unable to get previous location");
            playerEntity.changeGameMode(playerEntity.interactionManager.getPreviousGameMode());
            return 0;
        }
        EssentialUtils.sendToActionBar(playerEntity, "§6You have been put in §a" + playerEntity.interactionManager.getPreviousGameMode());
        playerEntity.changeGameMode(playerEntity.interactionManager.getPreviousGameMode());
        return 1;
    }

    private static boolean isInDanger(ServerPlayerEntity playerEntity) {
        if (playerEntity.isCreative()) {
            return false;
        }
        Set<StatusEffect> negativeStatusEffects = Set.of(
            StatusEffects.LEVITATION,
            StatusEffects.WITHER,
            StatusEffects.POISON
        );
        for (StatusEffect statusEffect : negativeStatusEffects) {
            if (playerEntity.hasStatusEffect(statusEffect)) {
                EssentialUtils.sendToActionBar(playerEntity, "§cYou cannot enter spectator because you have a negative status effect");
                return true;
            }
        }
        double x = playerEntity.getX(), y = playerEntity.getY(), z = playerEntity.getZ();
        Box nearPlayer = new Box(x - 4,y - 4,z - 4,x + 4,y + 4, z + 4);
        List<HostileEntity> list = playerEntity.world.getEntitiesByClass(HostileEntity.class, nearPlayer, hostileEntity -> true);
        String reason;
        if (!list.isEmpty()) {
            reason = "there are mobs nearby";
        } else if (playerEntity.isOnFire()) {
            reason = "you are on fire";
        } else if (playerEntity.fallDistance > 0) {
            reason = "you are falling";
        } else if (playerEntity.isFallFlying()) {
            reason = "you are flying";
        } else if (playerEntity.isSubmergedInWater()) {
            reason = "you are under water";
        } else {
            return false;
        }
        EssentialUtils.sendToActionBar(playerEntity, "§cYou cannot enter spectator because " + reason);
        return true;
    }
}
