package essentialaddons.commands;

import com.mojang.brigadier.CommandDispatcher;
import essentialaddons.EssentialSettings;
import essentialaddons.EssentialUtils;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;

//#if MC >= 11900
import static carpet.utils.CommandHelper.canUseCommand;
//#else
// import static carpet.settings.SettingsManager.canUseCommand;
//#endif
import static net.minecraft.server.command.CommandManager.literal;

public class CommandSwitchDimensions {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(literal("switchdimensions").requires((player) -> canUseCommand(player, EssentialSettings.commandSwitchDimensions))
            .executes(context -> {
                ServerPlayerEntity playerEntity = context.getSource().getPlayerOrThrow();
                ServerWorld overworld = context.getSource().getServer().getWorld(World.OVERWORLD);
                ServerWorld nether = context.getSource().getServer().getWorld(World.NETHER);
                if (playerEntity.getWorld() == overworld) {
                    toNether(playerEntity, nether);
                }
                else if (playerEntity.getWorld() == nether) {
                    toOverworld(playerEntity, overworld);
                }
                else {
                    EssentialUtils.sendToActionBar(playerEntity, "§cYou are not in the Overworld nor the Nether");
                }
                return 0;
            })
        );
    }
    private static void toOverworld(ServerPlayerEntity playerEntity, ServerWorld overworld) {
        playerEntity.teleport(overworld, playerEntity.getX() * 8, playerEntity.getY(), playerEntity.getZ() * 8, playerEntity.getYaw(1), playerEntity.getPitch(1));
        EssentialUtils.sendToActionBar(playerEntity, "§6You have been teleported to the §aOVERWORLD §6from your nether coords");
    }
    private static void toNether(ServerPlayerEntity playerEntity, ServerWorld nether) {
        playerEntity.teleport(nether, playerEntity.getX()/8, playerEntity.getY() > 128 ? playerEntity.getY() : 128, playerEntity.getZ()/8, playerEntity.getYaw(1), playerEntity.getPitch(1));
        EssentialUtils.sendToActionBar(playerEntity, "§6You have been teleported to the §aNETHER §6from your overworld coords");
    }


}
