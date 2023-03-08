package essentialaddons.commands;

import com.mojang.brigadier.CommandDispatcher;
import essentialaddons.EssentialSettings;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.world.GameMode;

import java.util.function.Predicate;

import static essentialaddons.EssentialUtils.enabled;
import static net.minecraft.server.command.CommandManager.literal;

public class CommandGM {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(literal("gmc").requires(enabled(() -> EssentialSettings.commandGM, "essentialaddons.command.gmc")).executes(c -> {
            c.getSource().getPlayerOrThrow().interactionManager.changeGameMode(GameMode.CREATIVE);
            return 1;
        }));
        dispatcher.register(literal("gms").requires(enabled(() -> EssentialSettings.commandGM, "essentialaddons.command.gms")).executes(c -> {
            c.getSource().getPlayerOrThrow().interactionManager.changeGameMode(GameMode.SURVIVAL);
            return 1;
        }));
        dispatcher.register(literal("gma").requires(enabled(() -> EssentialSettings.commandGM, "essentialaddons.command.gma")).executes(c -> {
            c.getSource().getPlayerOrThrow().interactionManager.changeGameMode(GameMode.ADVENTURE);
            return 1;
        }));
        dispatcher.register(literal("gmsp").requires(enabled(() -> EssentialSettings.commandGM, "essentialaddons.command.gmsp")).executes(c -> {
            c.getSource().getPlayerOrThrow().interactionManager.changeGameMode(GameMode.SPECTATOR);
            return 1;
        }));
    }
}
