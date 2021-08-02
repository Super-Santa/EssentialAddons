package essentialaddons.commands;

import carpet.settings.SettingsManager;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import essentialaddons.EssentialAddonsSettings;
import essentialaddons.EssentialAddonsUtils;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public class CommandDimensions {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(literal("overworld").requires((player) -> SettingsManager.canUseCommand(player, EssentialAddonsSettings.commandDimensions))
                .executes(context -> {
                    ServerPlayerEntity playerEntity = context.getSource().getPlayer();
                    ServerWorld world = context.getSource().getMinecraftServer().getWorld(World.OVERWORLD);
                    toDimension(playerEntity, world, "OVERWORLD");
                    return 0;
                })
                .then(argument("x", IntegerArgumentType.integer())
                        .then(argument("y", IntegerArgumentType.integer())
                                .then(argument("z", IntegerArgumentType.integer())
                                        .executes(context -> {
                                            ServerPlayerEntity playerEntity = context.getSource().getPlayer();
                                            ServerWorld world = context.getSource().getMinecraftServer().getWorld(World.OVERWORLD);
                                            toDimension(playerEntity, world, "OVERWORLD", context.getArgument("x", Integer.class), context.getArgument("y", Integer.class), context.getArgument("z", Integer.class));
                                            return 0;
                                        })
                                )
                        )
                )
        );
        dispatcher.register(literal("nether").requires((player) -> SettingsManager.canUseCommand(player, EssentialAddonsSettings.commandDimensions))
                .executes(context -> {
                    ServerPlayerEntity playerEntity = context.getSource().getPlayer();
                    ServerWorld world = context.getSource().getMinecraftServer().getWorld(World.NETHER);
                    toDimension(playerEntity, world, "NETHER");
                    return 0;
                })
                .then(argument("x", IntegerArgumentType.integer())
                        .then(argument("y", IntegerArgumentType.integer())
                                .then(argument("z", IntegerArgumentType.integer())
                                        .executes(context -> {
                                            ServerPlayerEntity playerEntity = context.getSource().getPlayer();
                                            ServerWorld world = context.getSource().getMinecraftServer().getWorld(World.NETHER);
                                            toDimension(playerEntity, world, "NETHER", context.getArgument("x", Integer.class), context.getArgument("y", Integer.class), context.getArgument("z", Integer.class));
                                            return 0;
                                        })
                                )
                        )
                )
        );
        dispatcher.register(literal("end").requires((player) -> SettingsManager.canUseCommand(player, EssentialAddonsSettings.commandDimensions))
                .executes(context -> {
                    ServerPlayerEntity playerEntity = context.getSource().getPlayer();
                    ServerWorld world = context.getSource().getMinecraftServer().getWorld(World.END);
                    toDimension(playerEntity, world, "END");
                    return 0;
                })
                .then(argument("x", IntegerArgumentType.integer())
                        .then(argument("y", IntegerArgumentType.integer())
                                .then(argument("z", IntegerArgumentType.integer())
                                        .executes(context -> {
                                            ServerPlayerEntity playerEntity = context.getSource().getPlayer();
                                            ServerWorld world = context.getSource().getMinecraftServer().getWorld(World.END);
                                            toDimension(playerEntity, world, "END", context.getArgument("x", Integer.class), context.getArgument("y", Integer.class), context.getArgument("z", Integer.class));
                                            return 0;
                                        })
                                )
                        )
                )
        );
    }
    private static void toDimension(ServerPlayerEntity playerEntity, ServerWorld world, String dimension) {
        playerEntity.teleport(world, 0, 128, 0, 0, 0);
        EssentialAddonsUtils.sendToActionBar(playerEntity, "§6You have been teleported to 0,0 in the §a" + dimension);
    }
    private static void toDimension(ServerPlayerEntity playerEntity, ServerWorld world, String dimension, int x, int y, int z) {
        playerEntity.teleport(world, x, y, z, playerEntity.yaw, playerEntity.pitch);
        EssentialAddonsUtils.sendToActionBar(playerEntity, "§6You have been teleported to " + x + " " + z + " in the §a" + dimension);
    }
}