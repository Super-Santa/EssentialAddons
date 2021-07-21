package essentialaddons.commands;

import carpet.settings.SettingsManager;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import essentialaddons.EssentialAddonsSettings;
import essentialaddons.helpers.EnumArgumentType;
import essentialaddons.helpers.LagSpikeHelper;
import net.minecraft.command.argument.DimensionArgumentType;
import net.minecraft.server.command.ServerCommandSource;

import static net.minecraft.server.command.CommandManager.literal;
import static net.minecraft.server.command.CommandManager.argument;

public class CommandLagSpike { //Ported from carpetmod112

    private static final int MAX_LAG_TIME = 60000;

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(literal("lagspike").requires((player) -> SettingsManager.canUseCommand(player, EssentialAddonsSettings.commandLagSpike))
                .then(argument("milliSeconds", IntegerArgumentType.integer(1,MAX_LAG_TIME))
                        .then(argument("tickPhase", EnumArgumentType.enumeration(LagSpikeHelper.TickPhase.class))
                                .then(argument("subPhase", EnumArgumentType.enumeration(LagSpikeHelper.PrePostSubPhase.class))
                                    //.then(argument("dimension", DimensionArgumentType.dimension())
                                        .executes((commandContext) ->
                                            LagSpikeHelper.addLagSpike(
                                                    EnumArgumentType.getEnumeration(commandContext,"tickPhase",LagSpikeHelper.TickPhase.class),
                                                    EnumArgumentType.getEnumeration(commandContext,"subPhase",LagSpikeHelper.PrePostSubPhase.class),
                                                    IntegerArgumentType.getInteger(commandContext,"milliSeconds")))))));
    }

}
