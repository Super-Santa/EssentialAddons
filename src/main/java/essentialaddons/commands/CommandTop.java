package essentialaddons.commands;

import com.mojang.brigadier.CommandDispatcher;
import essentialaddons.EssentialSettings;
import essentialaddons.EssentialUtils;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;

//#if MC >= 11900
import static carpet.utils.CommandHelper.canUseCommand;
//#else
//$$import static carpet.settings.SettingsManager.canUseCommand;
//#endif
import static net.minecraft.server.command.CommandManager.literal;

public class CommandTop {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(literal("top").requires((player) -> canUseCommand(player, EssentialSettings.commandTop))
            .executes(context -> {
                ServerPlayerEntity playerEntity = context.getSource().getPlayerOrThrow();
                BlockPos blockPos = new BlockPos(playerEntity.getX(), playerEntity.world.getHeight(), playerEntity.getZ());
                while (playerEntity.world.getBlockState(blockPos).isAir()) {
                    blockPos = blockPos.down();
                    if (blockPos.getY() == 0) {
                        EssentialUtils.sendToActionBar(playerEntity, "§6There is no top most block");
                        return 0;
                    }
                }
                playerEntity.teleport(playerEntity.getWorld(), blockPos.getX() + 0.5, blockPos.getY() + 1, blockPos.getZ() + 0.5, playerEntity.getYaw(), playerEntity.getPitch());
                EssentialUtils.sendToActionBar(playerEntity, "§6You have been teleported to the top most block");
                return 0;
            })
        );
    }
}
