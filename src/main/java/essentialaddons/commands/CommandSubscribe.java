package essentialaddons.commands;

import carpet.settings.SettingsManager;
import com.mojang.brigadier.CommandDispatcher;
import essentialaddons.EssentialAddonsSettings;
import essentialaddons.EssentialAddonsUtils;
import essentialaddons.utils.SubscribeData;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.UUID;

import static net.minecraft.server.command.CommandManager.literal;

public class CommandSubscribe {

    private static final Logger LOGGER = LogManager.getLogger("EssentialAddons");

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(literal("subscribe").requires((player) -> SettingsManager.canUseCommand(player, EssentialAddonsSettings.essentialCarefulBreak))
                .then(literal("carefulbreak")
                    .executes(context -> {
                        ServerPlayerEntity playerEntity = context.getSource().getPlayer();
                        UUID playerUUID = playerEntity.getUuid();
                        toggleSubscribe(playerEntity, playerUUID);
                        return 0;
                    })));
    }

    private static void toggleSubscribe(ServerPlayerEntity playerEntity, UUID playerUUID) {
        SubscribeData data = SubscribeData.subscribeData.remove(playerUUID);
        if (data == null) {
            try {
                SubscribeData.subscribeData = SubscribeData.readSaveFile();
                data = SubscribeData.subscribeData.remove(playerUUID);
            }
            catch (IOException e) {
                e.printStackTrace();
                LOGGER.error("Could not read subscribe data");
            }
            LOGGER.info("Successfully read file for + " + playerEntity.getName());
        }
        if (data == null || !data.isSubscribedCarefulBreak)
            SubscribeData.subscribeData.put(playerUUID, new SubscribeData(true));
        else
            SubscribeData.subscribeData.put(playerUUID, new SubscribeData(false));
        EssentialAddonsUtils.sendToActionBar(playerEntity, "§6Carefulbreak is now set to §a" + SubscribeData.isSubscibedCarfulBreak(playerUUID));
        try {
            SubscribeData.writeSaveFile(SubscribeData.subscribeData);
        }
        catch (IOException e) {
            e.printStackTrace();
            LOGGER.error("Could not save subscribe data");
        }
    }
}
