package essentialaddons.commands;

import carpet.settings.SettingsManager;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import essentialaddons.EssentialSettings;
import net.minecraft.command.argument.Vec2ArgumentType;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.LiteralText;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public class CommandRegion {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(literal("region").requires((player) -> SettingsManager.canUseCommand(player, EssentialSettings.commandRegion))
            .then(literal("get")
                .then(argument("pos", Vec2ArgumentType.vec2())
                    .executes(context -> {
                        ServerPlayerEntity playerEntity = context.getSource().getPlayer();
                        playerEntity.sendMessage(new LiteralText("§6Those coordinates are in region: §a" + (int) Math.floor(Vec2ArgumentType.getVec2(context, "pos").x/512) + "." + (int) Math.floor(Vec2ArgumentType.getVec2(context, "pos").y/512)), false);
                        return 0;
                    })
                )
                .executes(context -> {
                    ServerPlayerEntity playerEntity = context.getSource().getPlayer();
                    playerEntity.sendMessage(new LiteralText("§6You are in region: §a" + (int) Math.floor(playerEntity.getX()/512) + "." + (int) Math.floor(playerEntity.getZ()/512)), false);
                    return 0;
                })
            )
            .then(literal("teleport").requires(player -> player.hasPermissionLevel(4))
                .then(argument("x", IntegerArgumentType.integer())
                    .then(argument("z", IntegerArgumentType.integer())
                        .executes(context -> {
                            ServerPlayerEntity playerEntity = context.getSource().getPlayer();
                            int x = context.getArgument("x", Integer.class) * 512 + 256;
                            int z = context.getArgument("z", Integer.class) * 512 + 256;
                            playerEntity.teleport(playerEntity.getWorld(), x, playerEntity.getY(), z, playerEntity.getYaw(), playerEntity.getPitch());
                            return 0;
                        })
                    )
                )
            )
        );
    }
}
