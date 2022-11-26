package essentialaddons.commands;

import com.mojang.brigadier.CommandDispatcher;
import essentialaddons.EssentialSettings;
import essentialaddons.EssentialUtils;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;

//#if MC >= 11900
import static carpet.utils.CommandHelper.canUseCommand;
//#else
//$$import static carpet.settings.SettingsManager.canUseCommand;
//#endif
import static net.minecraft.server.command.CommandManager.literal;

public class CommandMore {

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(literal("more").requires((player) -> canUseCommand(player, EssentialSettings.commandMore))
            .executes(context -> {
                ServerPlayerEntity playerEntity = context.getSource().getPlayerOrThrow();
                ItemStack itemStack = context.getSource().getEntityOrThrow().getHandItems().iterator().next();
                if (itemStack != null && itemStack.getItem() != Items.AIR) {
                    itemStack.setCount(itemStack.getMaxCount());
                    EssentialUtils.sendToActionBar(playerEntity, "§6You have been given a full stack");
                }
                else {
                    EssentialUtils.sendToActionBar(playerEntity, "§cYou are not holding an item");
                }
                return 0;
            })
        );
    }
}
