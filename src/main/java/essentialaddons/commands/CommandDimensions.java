package essentialaddons.commands;

import carpet.settings.SettingsManager;
import com.mojang.brigadier.CommandDispatcher;
import essentialaddons.EssentialAddonsSettings;
import essentialaddons.EssentialAddonsUtils;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;

import java.util.UUID;

import static net.minecraft.server.command.CommandManager.literal;

public class CommandDimensions {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(literal("overworld")
                .requires((player) -> SettingsManager.canUseCommand(player, EssentialAddonsSettings.commandDimensions))
                .executes(context -> {
                    ServerPlayerEntity playerEntity = context.getSource().getPlayer();
                    UUID playerUUID = playerEntity.getUuid();
                    ServerWorld overworld = context.getSource().getMinecraftServer().getWorld(World.OVERWORLD);
                    ServerWorld nether = context.getSource().getMinecraftServer().getWorld(World.NETHER);
                    if (playerEntity.getServerWorld() == overworld) {
                        EssentialAddonsUtils.sendToActionBar(playerEntity, "§cYou are already in the overworld");
                    } else {
                        playerEntity.teleport(overworld, 0, 100, 0, 0, 0);
                        EssentialAddonsUtils.sendToActionBar(playerEntity, "§6You have been teleported to 0,0 in the overworld");
                    }
                    return 0;
                }));
        dispatcher.register(literal("nether").requires((player) -> SettingsManager.canUseCommand(player, EssentialAddonsSettings.commandDimensions))
                .executes(context -> {
                    ServerPlayerEntity playerEntity = context.getSource().getPlayer();
                    UUID playerUUID = playerEntity.getUuid();
                    ServerWorld overworld = context.getSource().getMinecraftServer().getWorld(World.OVERWORLD);
                    ServerWorld nether = context.getSource().getMinecraftServer().getWorld(World.NETHER);
                    if (playerEntity.getServerWorld() == nether) {
                        EssentialAddonsUtils.sendToActionBar(playerEntity, "§6You are already in the nether");
                    } else {
                        playerEntity.teleport(nether, 0, 128, 0, 0, 0);
                        EssentialAddonsUtils.sendToActionBar(playerEntity, "§6You have been teleported to 0,0 in the nether");
                    }
                    return 0;
                }));
        dispatcher.register(literal("end").requires((player) -> SettingsManager.canUseCommand(player, EssentialAddonsSettings.commandDimensions))
                .executes(context -> {
                    ServerPlayerEntity playerEntity = context.getSource().getPlayer();
                    UUID playerUUID = playerEntity.getUuid();
                    ServerWorld end = context.getSource().getMinecraftServer().getWorld(World.END);
                    if (playerEntity.getServerWorld() == end) {
                        EssentialAddonsUtils.sendToActionBar(playerEntity, "§cYou are already in the end");
                    } else {
                        playerEntity.teleport(end, 0, 100, 0, 0, 0);
                        EssentialAddonsUtils.sendToActionBar(playerEntity, "§6You have been teleported to 0,0 in the end");
                    }
                    return 0;
                }));

        dispatcher.register(literal("tpoverworld").requires((player) -> SettingsManager.canUseCommand(player, EssentialAddonsSettings.commandDimensions))
                .executes(context -> {
                    ServerPlayerEntity playerEntity = context.getSource().getPlayer();
                    ServerWorld overworld = context.getSource().getMinecraftServer().getWorld(World.OVERWORLD);
                    ServerWorld nether = context.getSource().getMinecraftServer().getWorld(World.NETHER);
                    UUID playerUUID = playerEntity.getUuid();

                    if (playerEntity.getServerWorld() == nether) {
                        double x = playerEntity.getX() * 8;
                        double y = playerEntity.getY();
                        double z = playerEntity.getZ() * 8;
                        float yaw = playerEntity.getYaw(1);
                        float pitch = playerEntity.getPitch(1);
                        playerEntity.teleport(overworld, x, y, z, yaw, pitch);
                        EssentialAddonsUtils.sendToActionBar(playerEntity, "§6You have been teleported to the overworld equivalent of your nether coords");
                    } else {
                        EssentialAddonsUtils.sendToActionBar(playerEntity, "§cYou are not in the nether");
                    }

                    return 0;
                }));

        dispatcher.register(literal("tpnether").requires((player) -> SettingsManager.canUseCommand(player, EssentialAddonsSettings.commandDimensions))
                .executes(context -> {
                    ServerPlayerEntity playerEntity = context.getSource().getPlayer();
                    ServerWorld overworld = context.getSource().getMinecraftServer().getWorld(World.OVERWORLD);
                    ServerWorld nether = context.getSource().getMinecraftServer().getWorld(World.NETHER);
                    UUID playerUUID = playerEntity.getUuid();

                    if (playerEntity.getServerWorld() == overworld) {
                        double x = playerEntity.getX() / 8;
                        double y = playerEntity.getY() > 128 ? playerEntity.getY() : 128;
                        double z = playerEntity.getZ() / 8;
                        float yaw = playerEntity.getYaw(1);
                        float pitch = playerEntity.getPitch(1);
                        playerEntity.teleport(nether, x, y, z, yaw, pitch);
                        EssentialAddonsUtils.sendToActionBar(playerEntity, "§6You have been teleported to the nether equivalent of your overworld coords");
                    } else {
                        EssentialAddonsUtils.sendToActionBar(playerEntity, "§cYou are not in the overworld");
                    }

                    return 0;
                }));
    }
}