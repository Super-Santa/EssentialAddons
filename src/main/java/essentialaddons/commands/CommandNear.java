package essentialaddons.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import essentialaddons.EssentialSettings;
import essentialaddons.EssentialUtils;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.Box;

import java.util.List;

import static essentialaddons.EssentialUtils.enabled;
import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public class CommandNear {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(literal("near").requires(enabled(() -> EssentialSettings.commandNear, "essentialaddons.command.near"))
            .then(argument("distance", IntegerArgumentType.integer(1))
                .executes(context -> {
                    ServerPlayerEntity serverPlayerEntity = context.getSource().getPlayerOrThrow();
                    int distance = context.getArgument("distance", Integer.class);
                    Box nearPlayer = new Box(serverPlayerEntity.getX() - distance,serverPlayerEntity.getY() - distance,serverPlayerEntity.getZ() - distance,serverPlayerEntity.getX() + distance,serverPlayerEntity.getY() + distance,serverPlayerEntity.getZ() + distance);
                    List<PlayerEntity> playerEntities = serverPlayerEntity.getWorld().getEntitiesByType(EntityType.PLAYER, nearPlayer, ServerPlayerEntity -> true);
                    if (playerEntities.size() < 2) {
                        serverPlayerEntity.sendMessage(EssentialUtils.literal("§cThere are no players near you"), false);
                        return 0;
                    }
                    final String[] names = new String[playerEntities.size() - 1];
                    int i = 0;
                    for (PlayerEntity playerEntity : playerEntities) {
                        if (playerEntity.getEntityName().equals(serverPlayerEntity.getEntityName())) {
                            continue;
                        }
                        names[i] = playerEntity.getEntityName();
                        i++;
                    }
                    String formattedNames = String.join(", ", names);
                    serverPlayerEntity.sendMessage(EssentialUtils.literal("§6Players near you: §a" + formattedNames), false);
                    return 0;
                })
            )
        );
    }
}
