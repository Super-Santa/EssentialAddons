package essentialaddons.commands;

import carpet.settings.SettingsManager;
import com.mojang.brigadier.CommandDispatcher;
import essentialaddons.EssentialAddonsSettings;
import essentialaddons.EssentialAddonsUtils;
import net.minecraft.item.ItemStack;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;

import static net.minecraft.server.command.CommandManager.literal;

public class CommandRepair {

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(literal("repair").requires((player) -> SettingsManager.canUseCommand(player, EssentialAddonsSettings.commandRepair))
                .executes(context -> {
                    ServerPlayerEntity playerEntity = context.getSource().getPlayer();
                    ItemStack itemStack = playerEntity.getMainHandStack();
                    if (itemStack.isDamaged()) {
                        itemStack.setDamage(0);
                        EssentialAddonsUtils.sendToActionBar(playerEntity, "§6Item was repaired");
                    } else {
                        EssentialAddonsUtils.sendToActionBar(playerEntity, "§cItem could not be repaired");
                    }
                    return 1;
        }));
    }
}
