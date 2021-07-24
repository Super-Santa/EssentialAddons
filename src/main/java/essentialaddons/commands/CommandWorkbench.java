package essentialaddons.commands;

import carpet.settings.SettingsManager;
import com.mojang.brigadier.CommandDispatcher;
import essentialaddons.EssentialAddonsSettings;
import net.minecraft.screen.CraftingScreenHandler;
import net.minecraft.screen.SimpleNamedScreenHandlerFactory;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.TranslatableText;

import static net.minecraft.server.command.CommandManager.literal;

public class CommandWorkbench {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(literal("workbench").requires((player) -> SettingsManager.canUseCommand(player, EssentialAddonsSettings.commandWorkbench))
                .executes(context -> {
                    ServerPlayerEntity playerEntity = context.getSource().getPlayer();
                    playerEntity.openHandledScreen(new SimpleNamedScreenHandlerFactory((syncId, playerInv, player) -> new CraftingScreenHandler(syncId,playerInv), new TranslatableText("container.crafting")));
                    return 0;
                }));
    }
}
