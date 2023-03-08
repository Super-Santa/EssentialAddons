package essentialaddons.commands;

import com.mojang.brigadier.CommandDispatcher;
import essentialaddons.EssentialSettings;
import essentialaddons.EssentialUtils;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;

import static essentialaddons.EssentialUtils.enabled;
import static net.minecraft.server.command.CommandManager.literal;

public class CommandFly {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(literal("fly").requires(enabled(() -> EssentialSettings.commandFly, "essentialaddons.command.fly"))
            .executes(context -> {
                ServerPlayerEntity playerEntity = context.getSource().getPlayerOrThrow();
                if (!playerEntity.getAbilities().allowFlying) {
                    playerEntity.getAbilities().allowFlying = true;
                    EssentialUtils.sendToActionBar(playerEntity, "§6Flying §aEnabled");
                }
                else {
                    playerEntity.getAbilities().allowFlying = false;
                    playerEntity.getAbilities().flying = false;
                    EssentialUtils.sendToActionBar(playerEntity, "§6Flying §cDisabled");
                }
                playerEntity.sendAbilitiesUpdate();
                return 1;
            })
        );
    }
}
