package essentialaddons.commands;

import carpet.settings.SettingsManager;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import essentialaddons.EssentialSettings;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.LiteralText;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public class CommandRename {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(literal("rename").requires((player) -> SettingsManager.canUseCommand(player, EssentialSettings.commandRename))
            .then(argument("name", StringArgumentType.string())
                .executes(context -> {
                    ItemStack itemStack = context.getSource().getPlayer().getMainHandStack();
                    if (itemStack.getItem() != Items.AIR) {
                        itemStack.setCustomName(new LiteralText(context.getArgument("name", String.class)));
                    }
                    return 0;
                })
            )
        );
    }
}
