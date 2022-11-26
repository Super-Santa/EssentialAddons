package essentialaddons.commands;

import com.mojang.brigadier.CommandDispatcher;
import essentialaddons.EssentialSettings;
import essentialaddons.EssentialUtils;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;

//#if MC >= 11900
import static carpet.utils.CommandHelper.canUseCommand;
//#else
// import static carpet.settings.SettingsManager.canUseCommand;
//#endif
import static net.minecraft.server.command.CommandManager.literal;

public class CommandExtinguish {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(literal("extinguish").requires((player) -> canUseCommand(player, EssentialSettings.commandExtinguish))
            .executes(context -> {
                ServerPlayerEntity playerEntity = context.getSource().getPlayerOrThrow();
                if (playerEntity.isOnFire()) {
                    playerEntity.extinguish();
                    EssentialUtils.sendToActionBar(playerEntity, "§6You have been extinguished");
                }
                else {
                    EssentialUtils.sendToActionBar(playerEntity, "§cYou are not on fire");
                }
                return 0;
            })
        );
    }
}
