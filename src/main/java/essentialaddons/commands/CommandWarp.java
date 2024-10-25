package essentialaddons.commands;

import com.mojang.brigadier.CommandDispatcher;
import essentialaddons.EssentialSettings;
import essentialaddons.EssentialUtils;
import essentialaddons.utils.Location;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import static essentialaddons.EssentialUtils.enabled;
import static net.minecraft.server.command.CommandManager.literal;

public class CommandWarp {
    public static Map<UUID, Location> warpData = new HashMap<>();

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(literal("setwarp").requires(enabled(() -> EssentialSettings.commandWarp, "essentialaddons.command.setwarp"))
            .executes(context -> {
                ServerPlayerEntity playerEntity = context.getSource().getPlayerOrThrow();
                UUID playerUUID = playerEntity.getUuid();
                CommandWarp.warpData.remove(playerUUID);
                CommandWarp.warpData.put(playerUUID, new Location(playerEntity));
                EssentialUtils.sendToActionBar(playerEntity, "§6Warp has been set");
                return 0;
            }));
        dispatcher.register(literal("warp").requires(enabled(() -> EssentialSettings.commandWarp, "essentialaddons.command.warp"))
            .executes(context -> {
                ServerPlayerEntity playerEntity = context.getSource().getPlayerOrThrow();
                UUID playerUUID = playerEntity.getUuid();
                Location location = CommandWarp.warpData.get(playerUUID);
                if (location != null) {
                    ServerWorld world = context.getSource().getServer().getWorld(location.worldRegistry());
                    playerEntity.teleport(
                        world,
                        location.position().x, location.position().y, location.position().z,
                        Set.of(),
                        location.yaw(), location.pitch(),
                        true
                    );
                    EssentialUtils.sendToActionBar(playerEntity, "§6You have been warped");
                }
                else {
                    EssentialUtils.sendToActionBar(playerEntity, "§6You have not set a warp yet");
                }
                return 0;
            })
        );
    }
}