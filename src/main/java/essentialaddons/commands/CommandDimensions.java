package essentialaddons.commands;

import carpet.utils.CommandHelper;
import com.mojang.brigadier.CommandDispatcher;
import essentialaddons.EssentialSettings;
import essentialaddons.EssentialUtils;
import net.minecraft.command.argument.Vec3ArgumentType;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public class CommandDimensions {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(literal("overworld").requires((player) -> CommandHelper.canUseCommand(player, EssentialSettings.commandDimensions))
            .executes(context -> {
                ServerPlayerEntity playerEntity = context.getSource().getPlayerOrThrow();
                ServerWorld world = context.getSource().getServer().getWorld(World.OVERWORLD);
                toDimension(playerEntity, world, "OVERWORLD");
                return 0;
            })
            .then(argument("pos", Vec3ArgumentType.vec3())
                .executes(context -> {
                    ServerPlayerEntity playerEntity = context.getSource().getPlayerOrThrow();
                    ServerWorld world = context.getSource().getServer().getWorld(World.OVERWORLD);
                    Vec3d pos = Vec3ArgumentType.getVec3(context, "pos");
                    toDimension(playerEntity, world, "OVERWORLD", pos.x, pos.y, pos.z);
                    return 0;
                })
            )
        );
        dispatcher.register(literal("nether").requires((player) -> CommandHelper.canUseCommand(player, EssentialSettings.commandDimensions))
            .executes(context -> {
                ServerPlayerEntity playerEntity = context.getSource().getPlayerOrThrow();
                ServerWorld world = context.getSource().getServer().getWorld(World.NETHER);
                toDimension(playerEntity, world, "NETHER");
                return 0;
            })
            .then(argument("pos", Vec3ArgumentType.vec3())
                .executes(context -> {
                    ServerPlayerEntity playerEntity = context.getSource().getPlayerOrThrow();
                    ServerWorld world = context.getSource().getServer().getWorld(World.NETHER);
                    Vec3d pos = Vec3ArgumentType.getVec3(context, "pos");
                    toDimension(playerEntity, world, "NETHER", pos.x, pos.y, pos.z);
                    return 0;
                })
            )
        );
        dispatcher.register(literal("end").requires((player) -> CommandHelper.canUseCommand(player, EssentialSettings.commandDimensions))
            .executes(context -> {
                ServerPlayerEntity playerEntity = context.getSource().getPlayerOrThrow();
                ServerWorld world = context.getSource().getServer().getWorld(World.END);
                toDimension(playerEntity, world, "END");
                return 0;
            })
            .then(argument("pos", Vec3ArgumentType.vec3())
                .executes(context -> {
                    ServerPlayerEntity playerEntity = context.getSource().getPlayerOrThrow();
                    ServerWorld world = context.getSource().getServer().getWorld(World.END);
                    Vec3d pos = Vec3ArgumentType.getVec3(context, "pos");
                    toDimension(playerEntity, world, "END", pos.x, pos.y, pos.z);
                    return 0;
                })
            )
        );
    }

    private static void toDimension(ServerPlayerEntity playerEntity, ServerWorld world, String dimension) {
        playerEntity.teleport(world, 0, 128, 0, 0, 0);
        EssentialUtils.sendToActionBar(playerEntity, "§6You have been teleported to 0,0 in the §a" + dimension);
    }

    private static void toDimension(ServerPlayerEntity playerEntity, ServerWorld world, String dimension, double x, double y, double z) {
        playerEntity.teleport(world, x, y, z, playerEntity.getYaw(), playerEntity.getPitch());
        EssentialUtils.sendToActionBar(playerEntity, "§6You have been teleported to " + x + " " + z + " in the §a" + dimension);
    }
}