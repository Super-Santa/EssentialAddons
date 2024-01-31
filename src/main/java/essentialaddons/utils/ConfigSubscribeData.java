package essentialaddons.utils;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.server.network.ServerPlayerEntity;

import java.nio.file.Path;
import java.util.*;

public class ConfigSubscribeData implements Config {
	public static final ConfigSubscribeData INSTANCE = new ConfigSubscribeData();

	private final Map<UUID, Set<Subscription>> subscriptions;

	private ConfigSubscribeData() {
		this.subscriptions = new HashMap<>();
	}

	protected void addPlayerSubscription(ServerPlayerEntity player, Subscription subscription) {
		this.subscriptions.computeIfAbsent(player.getUuid(), id -> EnumSet.noneOf(Subscription.class)).add(subscription);
	}

	protected void removePlayerSubscription(ServerPlayerEntity player, Subscription subscription) {
		Set<Subscription> subscriptions = this.subscriptions.get(player.getUuid());
		if (subscriptions != null) {
			subscriptions.remove(subscription);
		}
	}

	protected boolean isPlayerSubscribedTo(ServerPlayerEntity player, Subscription subscription) {
		Set<Subscription> subscriptions = this.subscriptions.get(player.getUuid());
		return subscriptions != null && subscriptions.contains(subscription);
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
		JsonArray playerSubscriptions = new JsonArray();
		this.subscriptions.forEach((uuid, subscriptionSet) -> {
			JsonObject playerData = new JsonObject();
			JsonArray array = new JsonArray();
			subscriptionSet.forEach(subscription -> array.add(subscription.getName()));
			playerData.addProperty("uuid", uuid.toString());
			playerData.add("subscriptions", array);
			playerSubscriptions.add(playerData);
		});
		return playerSubscriptions;
	}

	@Override
	public void readConfig(JsonArray playerSubscriptions) {
		playerSubscriptions.forEach(jsonElement -> {
			JsonObject playerData = jsonElement.getAsJsonObject();
			UUID playerUUID = UUID.fromString(playerData.get("uuid").getAsString());
			JsonArray jsonArray = playerData.getAsJsonArray("subscriptions");
			Set<Subscription> subscriptions = new HashSet<>();
			jsonArray.forEach(element -> {
				Subscription subscription = Subscription.fromString(element.getAsString());
				if (subscription != null) {
					subscriptions.add(subscription);
				}
			});
			this.subscriptions.put(playerUUID, subscriptions);
		});
	}
}
