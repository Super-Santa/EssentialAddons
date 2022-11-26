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

public class CommandGod {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(literal("god").requires((player) -> canUseCommand(player, EssentialSettings.commandGod))
            .executes(context -> {
                ServerPlayerEntity playerEntity = context.getSource().getPlayerOrThrow();
                if (!playerEntity.getAbilities().invulnerable) {
                    playerEntity.getAbilities().invulnerable = true;
                    EssentialUtils.sendToActionBar(playerEntity, "§6Invulnerability §aEnabled");

                }
                else {
                    playerEntity.getAbilities().invulnerable = false;
                    EssentialUtils.sendToActionBar(playerEntity, "§6Invulnerability §cDisabled");
                }
                playerEntity.sendAbilitiesUpdate();
                return 0;
            })
        );
    }
}
