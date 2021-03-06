package essentialaddons.commands;

import carpet.settings.SettingsManager;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import essentialaddons.EssentialSettings;
import essentialaddons.feature.LagSpike.PrePostSubPhase;
import essentialaddons.feature.LagSpike.TickPhase;
import essentialaddons.utils.EnumArgumentType;
import essentialaddons.feature.LagSpike;
import net.minecraft.command.CommandSource;
import net.minecraft.server.command.ServerCommandSource;

import java.util.Arrays;
import java.util.List;

import static net.minecraft.server.command.CommandManager.literal;
import static net.minecraft.server.command.CommandManager.argument;

public class CommandLagSpike {
    private static final int MAX_LAG_TIME = 60000;

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(literal("lagspike").requires((player) -> SettingsManager.canUseCommand(player, EssentialSettings.commandLagSpike))
            .then(argument("milliSeconds", IntegerArgumentType.integer(1, MAX_LAG_TIME))
                .suggests((c, b) -> CommandSource.suggestMatching(List.of("600", "1200", "6000"), b))
                .then(argument("tickPhase", EnumArgumentType.enumeration(TickPhase.class))
                    .suggests((c, b) -> CommandSource.suggestMatching(Arrays.stream(TickPhase.values()).map(Enum::toString).toList(), b))
                    .then(argument("subPhase", EnumArgumentType.enumeration(PrePostSubPhase.class))
                        .suggests((c, b) -> CommandSource.suggestMatching(Arrays.stream(PrePostSubPhase.values()).map(Enum::toString).toList(), b))
                        .executes((commandContext) -> {
                            return LagSpike.addLagSpike(
                                EnumArgumentType.getEnumeration(commandContext,"tickPhase", TickPhase.class),
                                EnumArgumentType.getEnumeration(commandContext,"subPhase", PrePostSubPhase.class),
                                IntegerArgumentType.getInteger(commandContext,"milliSeconds")
                            );
                        })
                    )
                )
            )
        );
    }

}
