package essentialaddons.commands;

import com.mojang.brigadier.CommandDispatcher;
import essentialaddons.EssentialSettings;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.screen.CraftingScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.SimpleNamedScreenHandlerFactory;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

import static essentialaddons.EssentialUtils.enabled;
import static net.minecraft.server.command.CommandManager.literal;

public class CommandWorkbench {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(literal("workbench").requires(enabled(() -> EssentialSettings.commandWorkbench, "essentialaddons.command.workbench"))
            .executes(context -> {
                ServerPlayerEntity playerEntity = context.getSource().getPlayerOrThrow();
                playerEntity.openHandledScreen(new SimpleNamedScreenHandlerFactory((syncId, playerInv, player) ->
                    new CraftingScreenHandler(syncId, playerInv, ScreenHandlerContext.create(player.getEntityWorld(), player.getBlockPos())) {
                        @Override
                        public boolean canUse(PlayerEntity player) {
                            return true;
                        }
                    },
                    Text.translatable("container.crafting"))
                );
                return 0;
            })
        );
    }
}
