package essentialaddons.helpers;

import carpet.CarpetServer;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.entity.Entity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.WorldSavePath;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.World;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nonnull;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class CameraData {

    public static Map<UUID, CameraData> cameraData = new HashMap<>();

    public static final MapCodec<CameraData> CODEC = RecordCodecBuilder.mapCodec(it -> it.group(
            Identifier.CODEC.fieldOf("dimension").forGetter(d -> d.dimension.getValue()),
            Codec.DOUBLE.fieldOf("x").forGetter(d -> d.position.x),
            Codec.DOUBLE.fieldOf("y").forGetter(d -> d.position.y),
            Codec.DOUBLE.fieldOf("z").forGetter(d -> d.position.z),
            Codec.FLOAT.fieldOf("yaw").forGetter(d -> d.yaw),
            Codec.FLOAT.fieldOf("pitch").forGetter(d -> d.pitch)
    ).apply(it, (dim, x, y, z, yaw, pitch) -> new CameraData(RegistryKey.of(Registry.DIMENSION, dim), new Vec3d(x, y, z), yaw, pitch)));

    public static final Codec<Map<UUID, CameraData>> MAP_CODEC = Codec.unboundedMap(Codec.STRING.xmap(UUID::fromString, UUID::toString), CODEC.codec());
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
    private static final Logger LOGGER = LogManager.getLogger("EssentialAddons");

    public final @Nonnull RegistryKey<World> dimension;
    public final @Nonnull Vec3d position;
    public final float yaw;
    public final float pitch;

    public CameraData(@Nonnull RegistryKey<World> dimension, @Nonnull Vec3d position, float yaw, float pitch) {
        this.dimension = dimension;
        this.position = position;
        this.yaw = yaw;
        this.pitch = pitch;
    }

    public CameraData(Entity entity) {
        this(entity.world.getRegistryKey(), entity.getPos(), entity.getYaw(1), entity.getPitch(1));
    }

    public void restore(ServerPlayerEntity playerEntity) {
        MinecraftServer server = playerEntity.world.getServer();
        if (server == null)
            return;
        ServerWorld world = server.getWorld(dimension);
        playerEntity.teleport(world, position.x, position.y, position.z, yaw, pitch);
    }

    private static Path getFile() {
        if (CarpetServer.minecraft_server.isDedicated())
            return CarpetServer.minecraft_server.getSavePath(WorldSavePath.ROOT).getParent().getParent().resolve("config").resolve("cameradata.json");
        else
            return CarpetServer.minecraft_server.getSavePath(WorldSavePath.ROOT).resolve("cameradata.json");
    }
    public static Map<UUID, CameraData> readSaveFile() throws IOException {
        Path file = getFile();
        if (!Files.isRegularFile(file)) return new HashMap<>();
        try (BufferedReader reader = Files.newBufferedReader(file)) {
            return new HashMap<>(MAP_CODEC.decode(JsonOps.INSTANCE, JsonHelper.deserialize(reader))
                    .getOrThrow(false, e -> LOGGER.error("Could not read camera data: {}", e))
                    .getFirst());
        }
    }
    public static void writeSaveFile(Map<UUID, CameraData> data) throws IOException {
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
}