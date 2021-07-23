package essentialaddons.helpers;

import carpet.CarpetServer;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import essentialaddons.EssentialAddonsUtils;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.WorldSavePath;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SubscribeData {

    public static Map<UUID, SubscribeData> subscribeData = new HashMap<>();

    public static final MapCodec<SubscribeData> CODEC = RecordCodecBuilder.mapCodec(it -> it.group(
            Codec.BOOL.fieldOf("isSubscribedCarefulBreak").forGetter(b -> b.isSubscribedCarefulBreak),
            Codec.BOOL.fieldOf("isSubscribedTeleportBlacklist").forGetter(b -> b.isSubscribedTeleportBlacklist)
    ).apply(it, SubscribeData::new));

    public static final Codec<Map<UUID, SubscribeData>> MAP_CODEC = Codec.unboundedMap(Codec.STRING.xmap(UUID::fromString, UUID::toString), CODEC.codec());
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
    private static final Logger LOGGER = LogManager.getLogger("EssentialAddons");

    public final boolean isSubscribedCarefulBreak;
    public final boolean isSubscribedTeleportBlacklist;

    public SubscribeData(boolean isSubscribedCarefulBreak, boolean isSubscribedTeleportBlacklist) {
        this.isSubscribedCarefulBreak = isSubscribedCarefulBreak;
        this.isSubscribedTeleportBlacklist = isSubscribedTeleportBlacklist;
    }

    private static Path getFile() {
        if (CarpetServer.minecraft_server.isDedicated())
            return CarpetServer.minecraft_server.getSavePath(WorldSavePath.ROOT).getParent().getParent().resolve("config").resolve("subscribedata.json");
        else
            return CarpetServer.minecraft_server.getSavePath(WorldSavePath.ROOT).resolve("subscribedata.json");
    }
    public static Map<UUID, SubscribeData> readSaveFile() throws IOException {
        Path file = getFile();
        if (!Files.isRegularFile(file)) return new HashMap<>();
        try (BufferedReader reader = Files.newBufferedReader(file)) {
            return new HashMap<>(MAP_CODEC.decode(JsonOps.INSTANCE, JsonHelper.deserialize(reader))
                    .getOrThrow(false, e -> LOGGER.error("Could not read subscribe data: {}", e))
                    .getFirst());
        }
    }
    public static void writeSaveFile(Map<UUID, SubscribeData> data) throws IOException {
        Path file = getFile();
        if (data.isEmpty()) {
            Files.deleteIfExists(file);
            return;
        }
        try(BufferedWriter writer = Files.newBufferedWriter(file)) {
            MAP_CODEC.encodeStart(JsonOps.INSTANCE, data)
                    .resultOrPartial(e -> LOGGER.error("Could not write subscribe data: {}", e))
                    .ifPresent(obj -> GSON.toJson(obj, writer));
        }
    }
    public void toggleSubscribe(ServerPlayerEntity playerEntity, UUID playerUUID, String type) {
        switch (type) {
            case "teleportblacklist":
                SubscribeData.subscribeData.put(playerUUID, new SubscribeData(isSubscribedCarefulBreak, !isSubscribedTeleportBlacklist));
                EssentialAddonsUtils.sendToActionBar(playerEntity, "§6Teleport blacklist is now set to §a" + !isSubscribedTeleportBlacklist);
                break;
            case "carefulbreak":
                SubscribeData.subscribeData.put(playerUUID, new SubscribeData(!isSubscribedCarefulBreak, isSubscribedTeleportBlacklist));
                EssentialAddonsUtils.sendToActionBar(playerEntity, "§6Carefulbreak is now set to §a" + !isSubscribedCarefulBreak);
                break;
        }
        try {
            SubscribeData.writeSaveFile(SubscribeData.subscribeData);
        } catch (IOException e) {
            e.printStackTrace();
            LOGGER.error("Could not save subscribe data");
        }
    }
}