package essentialaddons.commands;

import com.mojang.brigadier.CommandDispatcher;
import essentialaddons.EssentialSettings;
import essentialaddons.EssentialUtils;
import net.minecraft.registry.RegistryKey;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;

import static essentialaddons.EssentialUtils.enabled;
import static net.minecraft.server.command.CommandManager.literal;

public class CommandSwitchDimensions {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(literal("switchdimensions").requires(enabled(() -> EssentialSettings.commandSwitchDimensions, "essentialaddons.command.switchdimensions"))
            .executes(context -> {
                ServerPlayerEntity player = context.getSource().getPlayerOrThrow();
                RegistryKey<World> dimension = player.getWorld().getRegistryKey();
                if (dimension == World.OVERWORLD) {
                    toNether(player, player.server.getWorld(World.NETHER));
                } else if (dimension == World.NETHER) {
                    toOverworld(player, player.server.getOverworld());
                }
                else {
                    EssentialUtils.sendToActionBar(player, "§cYou are not in the Overworld nor the Nether");
                }
                return 0;
            })
        );
    }

    private static void toOverworld(ServerPlayerEntity playerEntity, ServerWorld overworld) {
        playerEntity.teleport(overworld, playerEntity.getX() * 8, playerEntity.getY(), playerEntity.getZ() * 8, playerEntity.getYaw(), playerEntity.getPitch());
        EssentialUtils.sendToActionBar(playerEntity, "§6You have been teleported to the §aOVERWORLD §6from your nether coords");
    }

    private static void toNether(ServerPlayerEntity playerEntity, ServerWorld nether) {
        playerEntity.teleport(nether, playerEntity.getX() / 8, Math.max(playerEntity.getY(), 128), playerEntity.getZ() / 8, playerEntity.getYaw(), playerEntity.getPitch());
        EssentialUtils.sendToActionBar(playerEntity, "§6You have been teleported to the §aNETHER §6from your overworld coords");
    }
}
