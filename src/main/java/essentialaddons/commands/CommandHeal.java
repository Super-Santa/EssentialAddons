package essentialaddons.commands;

import com.mojang.brigadier.CommandDispatcher;
import essentialaddons.EssentialSettings;
import essentialaddons.EssentialUtils;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;

//#if MC >= 11900
import static carpet.utils.CommandHelper.canUseCommand;
//#else
//$$import static carpet.settings.SettingsManager.canUseCommand;
//#endif
import static net.minecraft.server.command.CommandManager.literal;

public class CommandHeal {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(literal("heal").requires((player) -> canUseCommand(player, EssentialSettings.commandHeal))
            .executes(context -> {
                ServerPlayerEntity playerEntity = context.getSource().getPlayerOrThrow();
                playerEntity.setHealth(playerEntity.getMaxHealth());
                playerEntity.getHungerManager().add(20, 20);
                EssentialUtils.sendToActionBar(playerEntity, "§6You have been healed and fed");
                return 0;
            })
        );
    }
}
