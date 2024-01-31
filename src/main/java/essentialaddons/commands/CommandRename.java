package essentialaddons.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import essentialaddons.EssentialSettings;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.command.argument.TextArgumentType;
import net.minecraft.item.ItemStack;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;

import static essentialaddons.EssentialUtils.enabled;
import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public class CommandRename {
    private static final SimpleCommandExceptionType ITEM_IS_AIR = new SimpleCommandExceptionType(Text.literal("Cannot rename air!"));

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        // We let them handle /rename command
        if (FabricLoader.getInstance().isModLoaded("itemrename")) {
            return;
        }

        dispatcher.register(literal("rename").requires(enabled(() -> EssentialSettings.commandRename, "essentialaddons.command.rename"))
            .then(literal("json")
                .then(argument("name", TextArgumentType.text())
                    .executes(context -> {
                        ItemStack stack = context.getSource().getPlayerOrThrow().getMainHandStack();
                        if (!stack.isEmpty()) {
                            Text text = TextArgumentType.getTextArgument(context, "name");
                            context.getSource().sendFeedback(() -> {
                                return Text.literal("Item name set to: ").append(text);
                            }, false);
                            stack.setCustomName(text);
                            return 0;
                        }
                        throw ITEM_IS_AIR.create();
                    })
                )
            )
            .then(literal("literal")
                .then(argument("name", StringArgumentType.greedyString())
                    .executes(context -> {
                        ItemStack itemStack = context.getSource().getPlayerOrThrow().getMainHandStack();
                        if (!itemStack.isEmpty()) {
                            String name = StringArgumentType.getString(context, "name");
                            context.getSource().sendFeedback(() -> {
                                return Text.literal("Item name set to: ").append(name);
                            }, false);
                            itemStack.setCustomName(Text.literal(name).styled(s -> s.withItalic(false)));
                            return 0;
                        }
                        throw ITEM_IS_AIR.create();
                    })
                )
            )
        );
    }
}
