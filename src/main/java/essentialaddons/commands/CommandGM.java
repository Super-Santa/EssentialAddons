package essentialaddons.commands;

import com.mojang.brigadier.CommandDispatcher;
import essentialaddons.EssentialSettings;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.world.GameMode;

import java.util.function.Predicate;

//#if MC >= 11900
import static carpet.utils.CommandHelper.canUseCommand;
//#else
//$$import static carpet.settings.SettingsManager.canUseCommand;
//#endif
import static net.minecraft.server.command.CommandManager.literal;

public class CommandGM {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        Predicate<ServerCommandSource> canUse = (p) -> canUseCommand(p, EssentialSettings.commandGM);
        dispatcher.register(literal("gmc").requires(canUse).executes(c -> {
            c.getSource().getPlayerOrThrow().interactionManager.changeGameMode(GameMode.CREATIVE);
            return 1;
        }));
        dispatcher.register(literal("gms").requires(canUse).executes(c -> {
            c.getSource().getPlayerOrThrow().interactionManager.changeGameMode(GameMode.SURVIVAL);
            return 1;
        }));
        dispatcher.register(literal("gma").requires(canUse).executes(c -> {
            c.getSource().getPlayerOrThrow().interactionManager.changeGameMode(GameMode.ADVENTURE);
            return 1;
        }));
        dispatcher.register(literal("gmsp").requires(canUse).executes(c -> {
            c.getSource().getPlayerOrThrow().interactionManager.changeGameMode(GameMode.SPECTATOR);
            return 1;
        }));
    }
}
