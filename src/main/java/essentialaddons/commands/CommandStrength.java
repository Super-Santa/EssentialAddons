package essentialaddons.commands;

import carpet.settings.SettingsManager;
import com.mojang.brigadier.CommandDispatcher;
import essentialaddons.EssentialSettings;
import essentialaddons.EssentialUtils;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;

import static net.minecraft.server.command.CommandManager.literal;

public class CommandStrength {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(literal("strength").requires((player) -> SettingsManager.canUseCommand(player, EssentialSettings.commandStrength))
            .executes(context -> {
                ServerPlayerEntity playerEntity = context.getSource().getPlayerOrThrow();
                if (!playerEntity.hasStatusEffect(StatusEffects.STRENGTH)) {
                    playerEntity.addStatusEffect(new StatusEffectInstance(StatusEffects.STRENGTH, 999999, 255, true, false));
                    EssentialUtils.sendToActionBar(playerEntity, "§6Strength has been §aenabled");
                }
                else {
                    playerEntity.removeStatusEffect(StatusEffects.STRENGTH);
                    EssentialUtils.sendToActionBar(playerEntity, "§6Strength has been §cdisabled");
                }
                return 0;
            })
        );
    }
}

