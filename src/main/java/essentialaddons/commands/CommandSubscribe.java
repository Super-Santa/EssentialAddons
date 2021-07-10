package essentialaddons.commands;

import carpet.settings.SettingsManager;
import com.mojang.brigadier.CommandDispatcher;
import essentialaddons.EssentialAddonsSettings;
import essentialaddons.EssentialAddonsUtils;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.UUID;

import static net.minecraft.server.command.CommandManager.literal;

public class CommandSubscribe {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(literal("subscribe").requires((player) -> SettingsManager.canUseCommand(player, EssentialAddonsSettings.essentialCarefulBreak))
                .then(literal("carefulbreak")
                    .executes(context -> {
                        ServerPlayerEntity playerEntity = context.getSource().getPlayer();
                        UUID playerUUID = playerEntity.getUuid();
                        if (isSubscribed.get(playerUUID) == null) {
                            readSubscribe(playerUUID);
                        }
                        toggleSubscribe(playerEntity, playerUUID);
                        writeSubscribe(playerUUID, playerEntity, isSubscribed.get(playerUUID));
                        return 0;
                    })));
    }
    private static void toggleSubscribe(ServerPlayerEntity playerEntity, UUID playerUUID) {
        isSubscribed.put(playerUUID, !isSubscribed.get(playerUUID));
        EssentialAddonsUtils.sendToActionBar(playerEntity, "§6Careful break is now set to §a" + isSubscribed.get(playerUUID) );
    }
    @SuppressWarnings("ResultOfMethodCallIgnored")
    private static void writeSubscribe(UUID playerUUID, ServerPlayerEntity playerEntity, boolean playerTrue) {
        try {
            EssentialAddonsUtils.directoryExists("world/playerdata/carefulbreak");
            File file = new File("world/playerdata/carefulbreak/" + playerUUID + ".careful");
            if (playerTrue)
                file.createNewFile();
            else
                file.delete();
        }
        catch (IOException e) {
            EssentialAddonsUtils.sendToActionBar(playerEntity, "§c[ERROR] Something went wrong reading a file");
            System.out.println("[ERROR] Something went wrong reading a file");
        }
    }
    private static void readSubscribe(UUID playerUUID) {
        EssentialAddonsUtils.directoryExists("world/playerdata/carefulbreak");
        File file = new File("world/playerdata/carefulbreak/" + playerUUID + ".careful");
        if (file.exists()) {
            isSubscribed.put(playerUUID, true);
        }
        else
            isSubscribed.put(playerUUID, false);
    }
    public static boolean isSubscribedToCarefulBreak(UUID playerUUID) {
        if (CommandSubscribe.isSubscribed.get(playerUUID) == null)
            CommandSubscribe.readSubscribe(playerUUID);
        return CommandSubscribe.isSubscribed.get(playerUUID);
    }
    private static final HashMap<UUID, Boolean> isSubscribed = new HashMap<>();
}
