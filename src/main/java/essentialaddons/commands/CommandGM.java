package essentialaddons.commands;

import carpet.settings.SettingsManager;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.tree.CommandNode;
import com.mojang.brigadier.tree.LiteralCommandNode;
import essentialaddons.EssentialSettings;
import net.minecraft.server.command.ServerCommandSource;

import java.util.function.Predicate;

import static net.minecraft.server.command.CommandManager.literal;

public class CommandGM {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        CommandNode<ServerCommandSource> gamemode = dispatcher.getRoot().getChild("gamemode");
        Predicate<ServerCommandSource> canUse = (p) -> SettingsManager.canUseCommand(p, EssentialSettings.commandGM);
        dispatcher.register(setLiteral(((LiteralCommandNode<ServerCommandSource>) gamemode.getChild("creative")).createBuilder(), "gmc").requires(canUse));
        dispatcher.register(setLiteral(((LiteralCommandNode<ServerCommandSource>) gamemode.getChild("survival")).createBuilder(), "gms").requires(canUse));
        dispatcher.register(setLiteral(((LiteralCommandNode<ServerCommandSource>) gamemode.getChild("adventure")).createBuilder(), "gma").requires(canUse));
        dispatcher.register(setLiteral(((LiteralCommandNode<ServerCommandSource>) gamemode.getChild("spectator")).createBuilder(), "gmsp").requires(canUse));
    }

    private static LiteralArgumentBuilder<ServerCommandSource> setLiteral(LiteralArgumentBuilder<ServerCommandSource> builder, String literal) {
        return literal(literal).requires(builder.getRequirement()).forward(builder.getRedirect(), builder.getRedirectModifier(), builder.isFork()).executes(builder.getCommand());
    }
}
