package essentialaddons.commands;

import carpet.settings.SettingsManager;
import com.mojang.brigadier.CommandDispatcher;
import essentialaddons.EssentialAddonsSettings;
import essentialaddons.EssentialAddonsUtils;
import net.minecraft.entity.Entity;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.World;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static net.minecraft.server.command.CommandManager.literal;

public class CommandWarp {

    public static Map<UUID, CommandWarp> warpData = new HashMap<>();

    public final RegistryKey<World> dimension;
    public final Vec3d position;
    public final float yaw;
    public final float pitch;

    public CommandWarp(RegistryKey<World> dimension, Vec3d position, float yaw, float pitch) {
        this.dimension = dimension;
        this.position = position;
        this.yaw = yaw;
        this.pitch = pitch;
    }

    public CommandWarp(Entity entity) {
        this(entity.world.getRegistryKey(), entity.getPos(), entity.getYaw(), entity.getPitch());
    }

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(literal("setwarp").requires((player) -> SettingsManager.canUseCommand(player, EssentialAddonsSettings.commandWarp))
                .executes(context -> {
                    ServerPlayerEntity playerEntity = context.getSource().getPlayer();
                    UUID playerUUID = playerEntity.getUuid();
                    CommandWarp.warpData.remove(playerUUID);
                    CommandWarp.warpData.put(playerUUID, new CommandWarp(playerEntity));
                    EssentialAddonsUtils.sendToActionBar(playerEntity, "§6Warp has been set");
                    return 0;
                }));
        dispatcher.register(literal("warp").requires((player) -> SettingsManager.canUseCommand(player, EssentialAddonsSettings.commandWarp))
                .executes(context -> {
                    ServerPlayerEntity playerEntity = context.getSource().getPlayer();
                    UUID playerUUID = playerEntity.getUuid();
                    CommandWarp data = CommandWarp.warpData.get(playerUUID);
                    if(data != null) {
                        ServerWorld world = context.getSource().getServer().getWorld(data.dimension);
                        playerEntity.teleport(world, data.position.x, data.position.y, data.position.z, data.yaw, data.pitch);
                        EssentialAddonsUtils.sendToActionBar(playerEntity, "§6You have been warped");
                    }
                    else
                        EssentialAddonsUtils.sendToActionBar(playerEntity, "§6You have not set a warp yet");
                    return 0;
                })
        );
    }
}