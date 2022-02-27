package essentialaddons.commands;

import carpet.settings.SettingsManager;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import essentialaddons.EssentialSettings;
import net.minecraft.command.argument.TextArgumentType;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public class CommandRename {
    private static final SimpleCommandExceptionType ITEM_IS_AIR = new SimpleCommandExceptionType(new LiteralText("Cannot rename air!"));

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(literal("rename").requires((player) -> SettingsManager.canUseCommand(player, EssentialSettings.commandRename))
            .then(argument("name", TextArgumentType.text())
                .executes(context -> {
                    ItemStack itemStack = context.getSource().getPlayer().getMainHandStack();
                    if (itemStack.getItem() != Items.AIR) {
                        Text text = TextArgumentType.getTextArgument(context, "name");
                        context.getSource().sendFeedback(new LiteralText("Item name set to: ").append(text), false);
                        itemStack.setCustomName(text);
                        return 0;
                    }
                    throw ITEM_IS_AIR.create();
                })
            )
        );
    }
}
