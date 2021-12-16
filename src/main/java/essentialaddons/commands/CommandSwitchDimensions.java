package essentialaddons.commands;

import carpet.settings.SettingsManager;
import com.mojang.brigadier.CommandDispatcher;
import essentialaddons.EssentialAddonsSettings;
import essentialaddons.EssentialAddonsUtils;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;

import static net.minecraft.server.command.CommandManager.literal;

public class CommandSwitchDimensions {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(literal("switchdimensions").requires((player) -> SettingsManager.canUseCommand(player, EssentialAddonsSettings.commandSwitchDimensions)
        ).executes(context -> {
            //variables
            ServerPlayerEntity playerEntity = context.getSource().getPlayer();
            ServerWorld overworld = context.getSource().getServer().getWorld(World.OVERWORLD);
            ServerWorld nether = context.getSource().getServer().getWorld(World.NETHER);
            if (playerEntity.getWorld() == overworld)
                toNether(playerEntity, nether);
            else if (playerEntity.getWorld() == nether)
                toOverworld(playerEntity, overworld);
            else
                EssentialAddonsUtils.sendToActionBar(playerEntity, "§cYou are not in the Overworld nor the Nether");
            return 0;
        }));
    }
    private static void toOverworld(ServerPlayerEntity playerEntity, ServerWorld overworld) {
        playerEntity.teleport(overworld, playerEntity.getX() * 8, playerEntity.getY(), playerEntity.getZ() * 8, playerEntity.getYaw(1), playerEntity.getPitch(1));
        EssentialAddonsUtils.sendToActionBar(playerEntity, "§6You have been teleported to the §aOVERWORLD §6from your nether coords");
    }
    private static void toNether(ServerPlayerEntity playerEntity, ServerWorld nether) {
        playerEntity.teleport(nether, playerEntity.getX()/8, playerEntity.getY() > 128 ? playerEntity.getY() : 128, playerEntity.getZ()/8, playerEntity.getYaw(1), playerEntity.getPitch(1));
        EssentialAddonsUtils.sendToActionBar(playerEntity, "§6You have been teleported to the §aNETHER §6from your overworld coords");
    }


}
