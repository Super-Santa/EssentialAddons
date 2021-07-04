package essentialaddons.commands;

import carpet.settings.SettingsManager;
import com.mojang.brigadier.CommandDispatcher;
import essentialaddons.EssentialAddonsSettings;
import essentialaddons.EssentialAddonsUtils;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;

import static net.minecraft.server.command.CommandManager.literal;

public class CommandGod {

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(literal("god").requires((player) -> SettingsManager.canUseCommand(player, EssentialAddonsSettings.commandGod))
        .executes(context -> {
            ServerPlayerEntity playerEntity = context.getSource().getPlayer();
            if (!playerEntity.abilities.invulnerable) {
                playerEntity.abilities.invulnerable = true;
                EssentialAddonsUtils.sendToActionBar(playerEntity, "§6Invulnerability §aEnabled");

            } else {
                playerEntity.abilities.invulnerable = false;
                EssentialAddonsUtils.sendToActionBar(playerEntity, "§6Invulnerability §cDisabled");
            }
            playerEntity.sendAbilitiesUpdate();
            return 0;
        }));
    }
}
