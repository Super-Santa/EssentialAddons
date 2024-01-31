package essentialaddons.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import essentialaddons.EssentialUtils;
import essentialaddons.utils.Subscription;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;

import static net.minecraft.server.command.CommandManager.literal;

public class CommandSubscribe {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        LiteralArgumentBuilder<ServerCommandSource> subscribe = literal("subscribe");
        subscribe.requires(source -> Subscription.canUseSubscribeCommand());
        for (Subscription subscription : Subscription.values()) {
            String subscriptionName = subscription.getName();
            subscribe.then(literal(subscriptionName).requires(source -> subscription.getRequirement().get()).executes(ctx -> {
                ServerPlayerEntity playerEntity = ctx.getSource().getPlayerOrThrow();
                String message = subMessage(subscription.togglePlayer(playerEntity));
                EssentialUtils.sendToActionBar(playerEntity, message + " " + subscriptionName);
                return 1;
            }));
        }
        dispatcher.register(subscribe);
    }

    private static String subMessage(boolean bool) {
        return bool ? "§aSUBSCRIBED §6to" : "§cUNSUBSCRIBED §6from";
    }
}
