package essentialaddons.commands;

import com.mojang.brigadier.CommandDispatcher;
import essentialaddons.EssentialSettings;
import essentialaddons.EssentialUtils;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;

import static essentialaddons.EssentialUtils.enabled;
import static net.minecraft.server.command.CommandManager.literal;

public class CommandTop {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(literal("top").requires(enabled(() -> EssentialSettings.commandTop, "essentialaddons.command.top"))
            .executes(context -> {
                ServerPlayerEntity playerEntity = context.getSource().getPlayerOrThrow();
                BlockPos blockPos = new BlockPos(MathHelper.floor(playerEntity.getX()), playerEntity.getWorld().getHeight(), MathHelper.floor(playerEntity.getZ()));
                while (playerEntity.getWorld().getBlockState(blockPos).isAir()) {
                    blockPos = blockPos.down();
                    if (blockPos.getY() == 0) {
                        EssentialUtils.sendToActionBar(playerEntity, "§6There is no top most block");
                        return 0;
                    }
                }
                //#if MC >= 12000
                playerEntity.teleport(playerEntity.getServerWorld(), blockPos.getX() + 0.5, blockPos.getY() + 1, blockPos.getZ() + 0.5, playerEntity.getYaw(), playerEntity.getPitch());
                //#else
                //$$playerEntity.teleport(playerEntity.getWorld(), blockPos.getX() + 0.5, blockPos.getY() + 1, blockPos.getZ() + 0.5, playerEntity.getYaw(), playerEntity.getPitch());
                //#endif
                EssentialUtils.sendToActionBar(playerEntity, "§6You have been teleported to the top most block");
                return 0;
            })
        );
    }
}
