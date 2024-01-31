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
                ServerPlayerEntity player = context.getSource().getPlayerOrThrow();
                BlockPos blockPos = new BlockPos(MathHelper.floor(player.getX()), player.getWorld().getHeight(), MathHelper.floor(player.getZ()));
                while (player.getWorld().getBlockState(blockPos).isAir()) {
                    blockPos = blockPos.down();
                    if (blockPos.getY() == 0) {
                        EssentialUtils.sendToActionBar(player, "§6There is no top most block");
                        return 0;
                    }
                }
                player.teleport(player.getServerWorld(), blockPos.getX() + 0.5, blockPos.getY() + 1, blockPos.getZ() + 0.5, player.getYaw(), player.getPitch());
                EssentialUtils.sendToActionBar(player, "§6You have been teleported to the top most block");
                return 0;
            })
        );
    }
}
