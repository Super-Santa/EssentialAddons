package essentialaddons.commands;

import com.mojang.brigadier.CommandDispatcher;
import essentialaddons.EssentialSettings;
import essentialaddons.EssentialUtils;
import net.minecraft.item.ItemStack;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;

import static essentialaddons.EssentialUtils.enabled;
import static net.minecraft.server.command.CommandManager.literal;

public class CommandMore {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(literal("more").requires(enabled(() -> EssentialSettings.commandMore, "essentialaddons.command.more"))
            .executes(context -> {
                ServerPlayerEntity player = context.getSource().getPlayerOrThrow();
                ItemStack stack = player.getMainHandStack();
                if (stack.isEmpty()) {
                    stack = player.getOffHandStack();
                }
                if (!stack.isEmpty()) {
                    stack.setCount(stack.getMaxCount());
                    EssentialUtils.sendToActionBar(player, "§6You have been given a full stack");
                } else {
                    EssentialUtils.sendToActionBar(player, "§cYou are not holding an item");
                }
                return 0;
            })
        );
    }
}
