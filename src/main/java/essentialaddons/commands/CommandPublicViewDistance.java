package essentialaddons.commands;

import carpet.CarpetSettings;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import essentialaddons.EssentialSettings;
import essentialaddons.EssentialUtils;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;

import static essentialaddons.EssentialUtils.enabled;
import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public class CommandPublicViewDistance {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(literal("viewdistance").requires(enabled(() -> EssentialSettings.commandPublicViewDistance, "essentialaddons.command.viewdistance"))
            .then(argument("distance", IntegerArgumentType.integer(1))
                .executes(context -> viewDistance(context, context.getArgument("distance", Integer.class)))
            )
            .executes(context -> {
                ServerPlayerEntity player = context.getSource().getPlayerOrThrow();
                MinecraftServer server = context.getSource().getServer();
                EssentialUtils.sendToActionBar(player, "§6View distance is currently §a" + server.getPlayerManager().getViewDistance());
                return 0;
            })
        );
    }

    // Mostly code from Carpet
    private static int viewDistance(CommandContext<ServerCommandSource> context, int range) throws CommandSyntaxException {
        ServerPlayerEntity playerEntity = context.getSource().getPlayerOrThrow();
        if (range < 10 || range > 32) {
            EssentialUtils.sendToActionBar(playerEntity, "§cView distance must be between 10 and 32");
            return 0;
        }
        MinecraftServer server = context.getSource().getServer();
        if (server.isDedicated()) {
            if (range != server.getPlayerManager().getViewDistance()) {
                server.getPlayerManager().setViewDistance(range);
                CarpetSettings.viewDistance = range;
            }
            EssentialUtils.sendRawFeedback(context.getSource(), true, "View distance has changed to: " + range);
            EssentialUtils.sendToActionBar(playerEntity, "§6View distance has been changed to: §a" + range);
        } else {
            EssentialUtils.sendToActionBar(playerEntity, "§cView distance can only be changed on a server");
        }
        return 1;
    }
}
