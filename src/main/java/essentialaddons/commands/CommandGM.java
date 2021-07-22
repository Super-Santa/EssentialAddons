package essentialaddons.commands;

import carpet.settings.SettingsManager;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.tree.CommandNode;
import com.mojang.brigadier.tree.LiteralCommandNode;
import essentialaddons.EssentialAddonsSettings;
import net.minecraft.server.command.ServerCommandSource;

import static net.minecraft.server.command.CommandManager.literal;


public class CommandGM {

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        CommandNode<ServerCommandSource> gamemode = dispatcher.getRoot().getChild("gamemode");
            dispatcher.register(setLiteral(((LiteralCommandNode<ServerCommandSource>) gamemode.getChild("creative")).createBuilder(), "gmc").requires((player) -> SettingsManager.canUseCommand(player, EssentialAddonsSettings.commandGM)));
            dispatcher.register(setLiteral(((LiteralCommandNode<ServerCommandSource>) gamemode.getChild("survival")).createBuilder(), "gms").requires((player) -> SettingsManager.canUseCommand(player, EssentialAddonsSettings.commandGM)));
            dispatcher.register(setLiteral(((LiteralCommandNode<ServerCommandSource>) gamemode.getChild("adventure")).createBuilder(), "gma").requires((player) -> SettingsManager.canUseCommand(player, EssentialAddonsSettings.commandGM)));
            dispatcher.register(setLiteral(((LiteralCommandNode<ServerCommandSource>) gamemode.getChild("spectator")).createBuilder(), "gmsp").requires((player) -> SettingsManager.canUseCommand(player, EssentialAddonsSettings.commandGM)));
    }
    private static LiteralArgumentBuilder<ServerCommandSource> setLiteral(LiteralArgumentBuilder<ServerCommandSource> builder, String literal) {
        return literal(literal).requires(builder.getRequirement()).forward(builder.getRedirect(), builder.getRedirectModifier(), builder.isFork()).executes(builder.getCommand());
    }
}
