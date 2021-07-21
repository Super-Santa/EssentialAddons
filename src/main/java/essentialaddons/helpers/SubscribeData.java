package essentialaddons.helpers;

import carpet.CarpetServer;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
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
            Codec.BOOL.fieldOf("isSubscribedCarefulBreak").forGetter(b -> b.isSubscribedCarefulBreak)
    ).apply(it, SubscribeData::new));

    public static final Codec<Map<UUID, SubscribeData>> MAP_CODEC = Codec.unboundedMap(Codec.STRING.xmap(UUID::fromString, UUID::toString), CODEC.codec());
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
    private static final Logger LOGGER = LogManager.getLogger("EssentialAddons");

    public final boolean isSubscribedCarefulBreak;

    public SubscribeData(boolean isSubscribedCarefulBreak) {
        this.isSubscribedCarefulBreak = isSubscribedCarefulBreak;
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
                    .getOrThrow(false, e -> LOGGER.error("Could not read camera data: {}", e))
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
                    .resultOrPartial(e -> LOGGER.error("Could not write camera data: {}", e))
                    .ifPresent(obj -> GSON.toJson(obj, writer));
        }
    }
    public static boolean isSubscibedCarfulBreak (UUID uuid) {
        SubscribeData data = SubscribeData.subscribeData.get(uuid);
        return data != null && data.isSubscribedCarefulBreak;
    }
}
