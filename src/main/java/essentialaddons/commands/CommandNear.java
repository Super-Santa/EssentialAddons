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
import java.util.stream.Collectors;

import static essentialaddons.EssentialUtils.enabled;
import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public class CommandNear {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(literal("near").requires(enabled(() -> EssentialSettings.commandNear, "essentialaddons.command.near"))
            .then(argument("distance", IntegerArgumentType.integer(1))
                .executes(context -> {
                    ServerPlayerEntity player = context.getSource().getPlayerOrThrow();
                    int distance = context.getArgument("distance", Integer.class);
                    Box nearPlayer = new Box(player.getX() - distance,player.getY() - distance,player.getZ() - distance,player.getX() + distance,player.getY() + distance,player.getZ() + distance);
                    List<PlayerEntity> nearby = player.getWorld().getEntitiesByType(EntityType.PLAYER, nearPlayer, p -> p != player);
                    if (nearby.size() < 2) {
                        player.sendMessage(EssentialUtils.literal("§cThere are no players near you"), false);
                        return 0;
                    }

                    String names = nearby.stream().map(PlayerEntity::getEntityName).collect(Collectors.joining(", "));
                    player.sendMessage(EssentialUtils.literal("§6Players near you: §a" + names), false);
                    return 0;
                })
            )
        );
    }
}
