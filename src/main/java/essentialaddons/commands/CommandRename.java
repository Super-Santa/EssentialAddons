package essentialaddons.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import essentialaddons.EssentialSettings;
import essentialaddons.EssentialUtils;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.command.argument.TextArgumentType;
import net.minecraft.item.ItemStack;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;

import static essentialaddons.EssentialUtils.enabled;
import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public class CommandRename {
    private static final SimpleCommandExceptionType ITEM_IS_AIR = new SimpleCommandExceptionType(EssentialUtils.literal("Cannot rename air!"));

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        // We let them handle /rename command
        if (FabricLoader.getInstance().isModLoaded("itemrename")) {
            return;
        }

        dispatcher.register(literal("rename").requires(enabled(() -> EssentialSettings.commandRename, "essentialaddons.command.rename"))
            .then(argument("name", TextArgumentType.text())
                .executes(context -> {
                    ItemStack itemStack = context.getSource().getPlayerOrThrow().getMainHandStack();
                    if (!itemStack.isEmpty()) {
                        Text text = TextArgumentType.getTextArgument(context, "name");
                        context.getSource().sendFeedback(() -> {
                            return Text.literal("Item name set to: ").append(text);
                        }, false);
                        itemStack.setCustomName(text);
                        return 0;
                    }
                    throw ITEM_IS_AIR.create();
                })
            )
        );
    }
}
