package essentialaddons.commands;

import carpet.settings.SettingsManager;
import com.mojang.brigadier.CommandDispatcher;
import essentialaddons.EssentialAddonsSettings;
import essentialaddons.EssentialAddonsUtils;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;

import static net.minecraft.server.command.CommandManager.literal;

public class CommandFly {

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(literal("fly").requires((player) -> SettingsManager.canUseCommand(player, EssentialAddonsSettings.commandFly))
                .executes(context -> {

                    ServerPlayerEntity playerEntity = context.getSource().getPlayer();

                    if (!playerEntity.abilities.allowFlying) {
                        playerEntity.abilities.allowFlying = true;
                        EssentialAddonsUtils.sendToActionBar(playerEntity, "§6Flying §aEnabled");

                    } else {
                        playerEntity.abilities.allowFlying = false;
                        playerEntity.abilities.flying = false;
                        EssentialAddonsUtils.sendToActionBar(playerEntity, "§6Flying §cDisabled");
                    }

                    playerEntity.sendAbilitiesUpdate();

                    return 1;
                }));
    }
}
