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
                ServerPlayerEntity player = context.getSource().getPlayerOrThrow();
                if (!player.getAbilities().allowFlying) {
                    player.getAbilities().allowFlying = true;
                    EssentialUtils.sendToActionBar(player, "§6Flying §aEnabled");
                } else {
                    player.getAbilities().allowFlying = false;
                    player.getAbilities().flying = false;
                    EssentialUtils.sendToActionBar(player, "§6Flying §cDisabled");
                }
                player.sendAbilitiesUpdate();
                return 1;
            })
        );
    }
}
