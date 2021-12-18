package essentialaddons.commands;

import carpet.settings.SettingsManager;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.tree.LiteralCommandNode;
import essentialaddons.EssentialAddonsSettings;
import net.minecraft.inventory.EnderChestInventory;
import net.minecraft.screen.GenericContainerScreenHandler;
import net.minecraft.screen.SimpleNamedScreenHandlerFactory;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.TranslatableText;

import static net.minecraft.server.command.CommandManager.literal;

public class CommandEnderChest {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {

        // Can't use redirect here because of this: https://github.com/Mojang/brigadier/issues/46

        dispatcher.register(literal("enderchest").requires((player) -> SettingsManager.canUseCommand(player, EssentialAddonsSettings.commandEnderChest)).executes(context -> CommandEnderChest.execute(context.getSource())));
        dispatcher.register(literal("ec").requires((player) -> SettingsManager.canUseCommand(player, EssentialAddonsSettings.commandEnderChest)).executes(context -> CommandEnderChest.execute(context.getSource())));
    }

    private static int execute(ServerCommandSource source) throws CommandSyntaxException {
        ServerPlayerEntity playerEntity = source.getPlayer();
        EnderChestInventory enderChest = playerEntity.getEnderChestInventory();
        playerEntity.openHandledScreen(new SimpleNamedScreenHandlerFactory((syncId, playerInv, player) -> GenericContainerScreenHandler.createGeneric9x3(syncId, playerInv, enderChest), new TranslatableText("container.enderchest")));
    return 0;
    }
}
