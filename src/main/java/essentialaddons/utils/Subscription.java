package essentialaddons.utils;

import essentialaddons.EssentialSettings;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.function.Supplier;

public enum Subscription {
	TELEPORT_BLACKLIST("teleport_blacklist", () -> EssentialSettings.cameraModeTeleportBlacklist),
	ESSENTIAL_CAREFUL_BREAK("essential_careful_break", () -> EssentialSettings.essentialCarefulBreak),
	ESSENTIAL_CAREFUL_DROP("essential_careful_drop", () -> EssentialSettings.essentialCarefulDrop),
	ALWAYS_CAREFUL("always_careful", () -> EssentialSettings.essentialCarefulBreak || EssentialSettings.essentialCarefulDrop),
	;

	private final String prettyName;
	private final Supplier<Boolean> requirement;

	Subscription(String prettyName, Supplier<Boolean> requirement) {
		this.prettyName = prettyName;
		this.requirement = requirement;
	}

	public String getName() {
		return this.prettyName;
	}

	public Supplier<Boolean> getRequirement() {
		return this.requirement;
	}

	public boolean hasPlayer(ServerPlayerEntity player) {
		return ConfigSubscribeData.INSTANCE.isPlayerSubscribedTo(player, this);
	}

	public boolean togglePlayer(ServerPlayerEntity player) {
		boolean shouldDisable = this.hasPlayer(player);
		if (shouldDisable) {
			this.removePlayer(player);
		}
		else {
			this.addPlayer(player);
		}
		return !shouldDisable;
	}

	public void addPlayer(ServerPlayerEntity player) {
		ConfigSubscribeData.INSTANCE.addPlayerSubscription(player, this);
	}

	public void removePlayer(ServerPlayerEntity player) {
		ConfigSubscribeData.INSTANCE.removePlayerSubscription(player, this);
	}

	public static Subscription fromString(String name) {
		for (Subscription subscription : Subscription.values()) {
			if (subscription.prettyName.equals(name)) {
				return subscription;
			}
		}
		return null;
	}

	public static boolean canUseSubscribeCommand() {
		for (Subscription subscription : Subscription.values()) {
			if (subscription.getRequirement().get()) {
				return true;
			}
		}
		return false;
	}
}
