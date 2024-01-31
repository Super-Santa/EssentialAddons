package essentialaddons.utils;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ConfigCameraData implements Config {
	public static final ConfigCameraData INSTANCE = new ConfigCameraData();

	private final Map<UUID, Location> playerLocationMap;

	private ConfigCameraData() {
		this.playerLocationMap = new HashMap<>();
	}

	public void addPlayer(ServerPlayerEntity player) {
		this.playerLocationMap.put(player.getUuid(), new Location(player));
	}

	public boolean hasPlayerLocation(ServerPlayerEntity player) {
		return this.playerLocationMap.containsKey(player.getUuid());
	}

	public boolean restorePlayer(ServerPlayerEntity player) {
		Location playerLocation = this.playerLocationMap.remove(player.getUuid());
		if (playerLocation == null) {
			return false;
		}
		ServerWorld world = player.server.getWorld(playerLocation.worldRegistry());
		player.teleport(world, playerLocation.position().x, playerLocation.position().y, playerLocation.position().z, playerLocation.yaw(), playerLocation.pitch());
		return true;
	}

	@Override
	public String getConfigName() {
		return "CameraData";
	}

	@Override
	public Path getConfigPath() {
		return this.getConfigRootPath().resolve("cameradata.json");
	}

	@Override
	public JsonArray getSaveData() {
		JsonArray playerLocations = new JsonArray();
		this.playerLocationMap.forEach((uuid, location) -> {
			JsonObject playerData = new JsonObject();
			playerData.addProperty("uuid", uuid.toString());
			playerData.addProperty("dimension", location.worldRegistry().getValue().toString());
			playerData.addProperty("x", location.position().x);
			playerData.addProperty("y", location.position().y);
			playerData.addProperty("z", location.position().z);
			playerData.addProperty("yaw", location.yaw());
			playerData.addProperty("pitch", location.pitch());
			playerLocations.add(playerData);
		});
		return playerLocations;
	}

	@Override
	public void readConfig(JsonArray playerLocations) {
		playerLocations.forEach(jsonElement -> {
			JsonObject playerData = jsonElement.getAsJsonObject();
			UUID playerUUID = UUID.fromString(playerData.get("uuid").getAsString());
			RegistryKey<World> worldRegistry = RegistryKey.of(RegistryKeys.WORLD, Identifier.tryParse(playerData.get("dimension").getAsString()));
			Vec3d position = new Vec3d(playerData.get("x").getAsDouble(), playerData.get("y").getAsDouble(), playerData.get("z").getAsDouble());
			float yaw = playerData.get("yaw").getAsFloat();
			float pitch = playerData.get("pitch").getAsFloat();
			Location playerLocation = new Location(worldRegistry, position, yaw, pitch);
			this.playerLocationMap.put(playerUUID, playerLocation);
		});
	}
}
