package essentialaddons.utils;

import carpet.fakes.ServerPlayerInterface;
import carpet.helpers.EntityPlayerActionPack.Action;
import carpet.helpers.EntityPlayerActionPack.ActionType;
import carpet.patches.EntityPlayerMPFake;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import essentialaddons.EssentialAddons;
import essentialaddons.feature.ReloadFakePlayers;
import essentialaddons.mixins.reloadFakePlayers.ActionMixin;
import essentialaddons.mixins.reloadFakePlayers.EntityPlayerActionPackAccessor;
import net.minecraft.server.MinecraftServer;

import java.nio.file.Path;
import java.util.*;

public class ConfigFakePlayerData implements Config {
	public static final ConfigFakePlayerData INSTANCE = new ConfigFakePlayerData();

	private final Set<EntityPlayerMPFake> fakePlayers;

	private ConfigFakePlayerData() {
		this.fakePlayers = new HashSet<>();
	}

	public void addFakePlayer(EntityPlayerMPFake fakePlayer) {
		this.fakePlayers.add(fakePlayer);
	}

	public void removeFakePlayer(EntityPlayerMPFake fakePlayer) {
		this.fakePlayers.remove(fakePlayer);
	}

	@Override
	public String getConfigName() {
		return "FakePlayerData";
	}

	@Override
	public Path getConfigPath() {
		return this.getConfigRootPath().resolve("fakeplayerdata.json");
	}

	@Override
	public JsonArray getSaveData() {
		JsonArray totalPlayerData = new JsonArray();
		this.fakePlayers.forEach(player -> {
			EntityPlayerActionPackAccessor actionPackAccessor = (EntityPlayerActionPackAccessor) ((ServerPlayerInterface) player).getActionPack();
			JsonObject playerData = new JsonObject();
			playerData.addProperty("uuid", player.getUuid().toString());
			playerData.addProperty("username", player.getGameProfile().getName());
			playerData.addProperty("sneaking", actionPackAccessor.isSneaking());
			playerData.addProperty("sprinting", actionPackAccessor.isSprinting());
			playerData.addProperty("forward", actionPackAccessor.getForward());
			playerData.addProperty("strafing", actionPackAccessor.getStrafing());
			JsonArray playerActions = new JsonArray();
			actionPackAccessor.getActions().forEach((actionType, action) -> {
				JsonObject actionData = new JsonObject();
				ActionMixin actionAccessor = (ActionMixin) action;
				actionData.addProperty("type", actionType.toString());
				actionData.addProperty("limit", action.limit);
				actionData.addProperty("interval", action.interval);
				actionData.addProperty("offset", action.offset);
				actionData.addProperty("count", actionAccessor.getCount());
				actionData.addProperty("next", actionAccessor.getNext());
				actionData.addProperty("continuous", actionAccessor.isContinuous());
				playerActions.add(actionData);
			});
			playerData.add("actions", playerActions);
			totalPlayerData.add(playerData);
		});
		return totalPlayerData;
	}

	@Override
	public void readConfig(JsonArray configData) { }

	public void readConfig(MinecraftServer server) {
		JsonArray totalPlayerData = this.getConfigData();
		Set<UUID> loadedPlayers = new HashSet<>();
		totalPlayerData.forEach(jsonElement -> {
			JsonObject playerData = jsonElement.getAsJsonObject();
			UUID playerUUID = UUID.fromString(playerData.get("uuid").getAsString());

			if (!loadedPlayers.contains(playerUUID)) {
				String username = playerData.get("username").getAsString();
				boolean isSneaking = playerData.get("sneaking").getAsBoolean();
				boolean isSprinting = playerData.get("sprinting").getAsBoolean();
				float forward = playerData.get("forward").getAsFloat();
				float strafing = playerData.get("strafing").getAsFloat();
				Map<ActionType, Action> actionMap = new TreeMap<>();
				JsonArray playerActions = playerData.get("actions").getAsJsonArray();
				playerActions.forEach(element -> {
					JsonObject actionData = element.getAsJsonObject();
					ActionType type = ActionType.valueOf(actionData.get("type").getAsString());
					Action action = ActionMixin.init(
						actionData.get("limit").getAsInt(),
						actionData.get("interval").getAsInt(),
						actionData.get("offset").getAsInt(),
						actionData.get("continuous").getAsBoolean()
					);
					((ActionMixin) action).setCount(actionData.get("count").getAsInt());
					((ActionMixin) action).setNext(actionData.get("next").getAsInt());
					actionMap.put(type, action);
				});
				loadedPlayers.add(playerUUID);

				ReloadFakePlayers.loadPlayer(
					server,
					playerUUID,
					username,
					isSneaking,
					isSprinting,
					forward,
					strafing,
					actionMap
				);
			} else {
				EssentialAddons.LOGGER.warn("Tried to load duplicate player: {}", playerUUID);
			}
		});
	}
}
