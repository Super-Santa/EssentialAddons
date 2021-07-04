package essentialaddons.commands;

import carpet.settings.SettingsManager;
import com.mojang.brigadier.CommandDispatcher;
import essentialaddons.EssentialAddonsSettings;
import essentialaddons.EssentialAddonsUtils;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;

import static net.minecraft.server.command.CommandManager.literal;

public class CommandExtinguish {

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(literal("extinguish").requires((player) -> SettingsManager.canUseCommand(player, EssentialAddonsSettings.commandExtinguish))
        .executes(context -> {
            ServerPlayerEntity playerEntity = context.getSource().getPlayer();
            if (playerEntity.isOnFire()) {
                playerEntity.extinguish();
                EssentialAddonsUtils.sendToActionBar(playerEntity, "§6You have been extinguished");
            } else { EssentialAddonsUtils.sendToActionBar(playerEntity, "§cYou are not on fire"); }
            return 0;
        }));
    }
}
