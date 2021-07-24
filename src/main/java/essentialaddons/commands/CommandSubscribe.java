package essentialaddons.commands;

import carpet.settings.SettingsManager;
import com.mojang.brigadier.CommandDispatcher;
import essentialaddons.EssentialAddonsSettings;
import essentialaddons.EssentialAddonsUtils;
import essentialaddons.helpers.SubscribeData;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.UUID;

import static net.minecraft.server.command.CommandManager.literal;

public class CommandSubscribe {

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(literal("subscribe").requires(player -> SettingsManager.canUseCommand(player, EssentialAddonsSettings.essentialCarefulBreak || EssentialAddonsSettings.cameraModeTeleportBlacklist))
                .then(literal("carefulbreak").requires(player -> SettingsManager.canUseCommand(player, EssentialAddonsSettings.essentialCarefulBreak))
                        .executes(context -> {
                            ServerPlayerEntity playerEntity = context.getSource().getPlayer();
                            UUID playerUUID = playerEntity.getUuid();
                            SubscribeData data = SubscribeData.subscribeData.remove(playerUUID);
                            if (data == null) {
                                SubscribeData.subscribeData.put(playerUUID, new SubscribeData(false, false));
                                data = SubscribeData.subscribeData.remove(playerUUID);
                            }
                            data.toggleSubscribe(playerEntity, playerUUID, "carefulbreak");
                            return 0;
                        })
                )
                .then(literal("teleportblacklist").requires(player -> SettingsManager.canUseCommand(player, EssentialAddonsSettings.cameraModeTeleportBlacklist))
                        .executes(context -> {
                            ServerPlayerEntity playerEntity = context.getSource().getPlayer();
                            UUID playerUUID = playerEntity.getUuid();
                            SubscribeData data = SubscribeData.subscribeData.remove(playerUUID);
                            if (data == null) {
                                SubscribeData.subscribeData.put(playerUUID, new SubscribeData(false, false));
                                data = SubscribeData.subscribeData.remove(playerUUID);
                            }
                            data.toggleSubscribe(playerEntity, playerUUID, "teleportblacklist");
                            return 0;
                        })
                )
        );
    }
}
