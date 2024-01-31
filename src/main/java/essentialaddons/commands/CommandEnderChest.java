package essentialaddons.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import essentialaddons.EssentialSettings;
import net.minecraft.inventory.EnderChestInventory;
import net.minecraft.screen.GenericContainerScreenHandler;
import net.minecraft.screen.SimpleNamedScreenHandlerFactory;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

import static essentialaddons.EssentialUtils.enabled;
import static net.minecraft.server.command.CommandManager.literal;

public class CommandEnderChest {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(literal("enderchest").requires(enabled(() -> EssentialSettings.commandEnderChest, "essentialaddons.command.enderchest")).executes(context -> CommandEnderChest.execute(context.getSource())));
        dispatcher.register(literal("ec").requires(enabled(() -> EssentialSettings.commandEnderChest, "essentialaddons.command.ec")).executes(context -> CommandEnderChest.execute(context.getSource())));
    }

    private static int execute(ServerCommandSource source) throws CommandSyntaxException {
        ServerPlayerEntity playerEntity = source.getPlayerOrThrow();
        EnderChestInventory enderChest = playerEntity.getEnderChestInventory();
        playerEntity.openHandledScreen(new SimpleNamedScreenHandlerFactory((syncId, playerInv, player) -> {
            return GenericContainerScreenHandler.createGeneric9x3(syncId, playerInv, enderChest);
        }, Text.translatable("container.enderchest")));
        return 0;
    }
}