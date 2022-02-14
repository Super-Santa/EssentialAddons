package essentialaddons.utils;

import carpet.CarpetServer;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.WorldSavePath;

import java.nio.file.Path;
import java.util.*;

public class ConfigSubscribeData implements Config {
	public static final ConfigSubscribeData INSTANCE = new ConfigSubscribeData();

	private final Map<UUID, Set<Subscription>> playerSubscriptionMap;

	private ConfigSubscribeData() {
		this.playerSubscriptionMap = new HashMap<>();
	}

	protected void addPlayerSubscription(ServerPlayerEntity player, Subscription subscription) {
		Set<Subscription> subscriptionSet = this.playerSubscriptionMap.getOrDefault(player.getUuid(), new HashSet<>());
		subscriptionSet.add(subscription);
		this.playerSubscriptionMap.putIfAbsent(player.getUuid(), subscriptionSet);
	}

	protected void removePlayerSubscription(ServerPlayerEntity player, Subscription subscription) {
		Set<Subscription> subscriptionSet = this.playerSubscriptionMap.get(player.getUuid());
		if (subscriptionSet != null) {
			subscriptionSet.remove(subscription);
		}
	}

	protected boolean isPlayerSubscribedTo(ServerPlayerEntity player, Subscription subscription) {
		Set<Subscription> subscriptionSet = this.playerSubscriptionMap.get(player.getUuid());
		return subscriptionSet != null && subscriptionSet.contains(subscription);
	}

	@Override
	public String getConfigName() {
		return "SubscribeData";
	}

	@Override
	public Path getConfigPath() {
		return this.getConfigRootPath().resolve("subscribedata.json");
	}

	@Override
	public JsonArray getSaveData() {
		JsonArray playerSubscriptions = new JsonArray(this.playerSubscriptionMap.size());
		this.playerSubscriptionMap.forEach((uuid, subscriptionSet) -> {
			JsonObject playerData = new JsonObject();
			JsonArray jsonArray = new JsonArray();
			subscriptionSet.forEach(subscription -> jsonArray.add(subscription.getName()));
			playerData.addProperty("uuid", uuid.toString());
			playerData.add("subscriptions", jsonArray);
			playerSubscriptions.add(playerData);
		});
		return playerSubscriptions;
	}

	@Override
	public void readConfig() {
		JsonArray playerSubscriptions = this.getConfigData();
		playerSubscriptions.forEach(jsonElement -> {
			JsonObject playerData = jsonElement.getAsJsonObject();
			UUID playerUUID = UUID.fromString(playerData.get("uuid").getAsString());
			JsonArray jsonArray = playerData.get("subscriptions").getAsJsonArray();
			Set<Subscription> subscriptions = new HashSet<>();
			jsonArray.forEach(element -> {
				Subscription subscription = Subscription.fromString(element.getAsString());
				if (subscription != null) {
					subscriptions.add(subscription);
				}
			});
			this.playerSubscriptionMap.put(playerUUID, subscriptions);
		});
	}
}
