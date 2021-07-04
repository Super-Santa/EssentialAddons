package essentialaddons.commands;

import carpet.settings.SettingsManager;
import com.mojang.brigadier.CommandDispatcher;
import essentialaddons.EssentialAddonsSettings;
import essentialaddons.EssentialAddonsUtils;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;

import java.util.HashMap;
import java.util.UUID;

import static net.minecraft.server.command.CommandManager.literal;

public class CommandWarp {

    private static final HashMap<UUID, ServerWorld> dimension = new HashMap<>();
    private static final HashMap<UUID, Double> x = new HashMap<>();
    private static final HashMap<UUID, Double> y = new HashMap<>();
    private static final HashMap<UUID, Double> z = new HashMap<>();
    private static final HashMap<UUID, Float> yaw = new HashMap<>();
    private static final HashMap<UUID, Float> pitch = new HashMap<>();

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(literal("setwarp").requires((player) -> SettingsManager.canUseCommand(player, EssentialAddonsSettings.commandWarp))
                .executes(context -> {
                    ServerPlayerEntity playerEntity = context.getSource().getPlayer();
                    UUID playerUUID = playerEntity.getUuid();
                    dimension.put(playerUUID,playerEntity.getServerWorld());
                    x.put(playerUUID,playerEntity.getX());
                    y.put(playerUUID,playerEntity.getY());
                    z.put(playerUUID,playerEntity.getZ());
                    yaw.put(playerUUID,playerEntity.getYaw(1));
                    pitch.put(playerUUID,playerEntity.getPitch(1));
                    EssentialAddonsUtils.sendToActionBar(playerEntity, "§6Warp has been set");
                    return 0;
                }));
        dispatcher.register(literal("warp").requires((player) -> SettingsManager.canUseCommand(player, EssentialAddonsSettings.commandWarp))
                .executes(context -> {
                    ServerPlayerEntity playerEntity = context.getSource().getPlayer();
                    UUID playerUUID = playerEntity.getUuid();
                    if(x.get(playerUUID) != null) {
                        playerEntity.teleport(dimension.get(playerUUID),x.get(playerUUID),y.get(playerUUID),z.get(playerUUID),yaw.get(playerUUID),pitch.get(playerUUID));
                        EssentialAddonsUtils.sendToActionBar(playerEntity, "§6You have been warped");
                    }
                    else {
                        EssentialAddonsUtils.sendToActionBar(playerEntity, "§6You have not set a warp yet");
                    }
                    return 0;
                }));
    }
}