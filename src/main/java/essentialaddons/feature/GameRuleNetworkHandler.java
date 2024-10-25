package essentialaddons.feature;

import essentialaddons.EssentialAddons;
import essentialaddons.EssentialSettings;
import essentialaddons.mixins.gameRuleSync.RuleInvoker;
import essentialaddons.utils.ducks.IRule;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.network.packet.s2c.common.CustomPayloadS2CPacket;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.world.GameRules;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class GameRuleNetworkHandler {
	private static final int VERSION = 1_0_1;

	public static final GameRuleNetworkHandler INSTANCE = new GameRuleNetworkHandler();

	private final Set<ServerPlayNetworkHandler> validPlayers = new HashSet<>();
	private final Map<String, GameRules.Key<?>> keyMap = new HashMap<>();

	private GameRuleNetworkHandler() {

	}

	public void sayHello(ServerPlayerEntity player) {
		CustomPayloadS2CPacket packet = new CustomPayloadS2CPacket(new GameRuleHelloPayload("essential_addons", VERSION));
		player.networkHandler.sendPacket(packet);
	}

	public void updateAllPlayerStatuses() {
		for (ServerPlayNetworkHandler handler : this.validPlayers) {
			this.updatePlayerStatus(handler.player);
		}
	}

	public void updatePlayerStatus(ServerPlayerEntity player) {
		boolean canUpdateGamerules = EssentialSettings.gameRuleSync && (EssentialSettings.gameRuleNonOp || player.hasPermissionLevel(2));
		CustomPayloadS2CPacket packet = new CustomPayloadS2CPacket(new GameRulePermissionsPayload(canUpdateGamerules));
		player.networkHandler.sendPacket(packet);
	}

	public void sendAllRules() {
		Map<ServerWorld, CustomPayloadS2CPacket> cache = new HashMap<>();
		for (ServerPlayNetworkHandler handler : this.validPlayers) {
			CustomPayloadS2CPacket packet = cache.computeIfAbsent(handler.player.getServerWorld(), (world) -> {
				NbtCompound gamerules = world.getGameRules().toNbt();
				return new CustomPayloadS2CPacket(new GameRulesChangedPayload(gamerules));
			});
			handler.sendPacket(packet);
		}
	}

	public void sendAllRules(ServerPlayerEntity player) {
		NbtCompound gamerules = player.getServerWorld().getGameRules().toNbt();
		CustomPayloadS2CPacket packet = new CustomPayloadS2CPacket(new GameRulesChangedPayload(gamerules));
		player.networkHandler.sendPacket(packet);
	}

	public void onRuleChange(String ruleName, String ruleValue) {
		NbtCompound gamerules = new NbtCompound();
		gamerules.putString(ruleName, ruleValue);
		CustomPayloadS2CPacket packet = new CustomPayloadS2CPacket(new GameRulesChangedPayload(gamerules));
		for (ServerPlayNetworkHandler handler : this.validPlayers) {
			handler.sendPacket(packet);
		}
	}

	public void addGameRuleKey(GameRules.Key<?> gameRuleKey) {
		this.keyMap.put(gameRuleKey.getName(), gameRuleKey);
	}

	public void registerGameRulePayloads() {
		PayloadTypeRegistry.playC2S().register(GameRuleHelloPayload.ID, GameRuleHelloPayload.CODEC);
		PayloadTypeRegistry.playC2S().register(SetGameRulePayload.ID, SetGameRulePayload.CODEC);

		PayloadTypeRegistry.playS2C().register(GameRuleHelloPayload.ID, GameRuleHelloPayload.CODEC);
		PayloadTypeRegistry.playS2C().register(GameRulePermissionsPayload.ID, GameRulePermissionsPayload.CODEC);
		PayloadTypeRegistry.playS2C().register(GameRulesChangedPayload.ID, GameRulesChangedPayload.CODEC);

		ServerPlayNetworking.registerGlobalReceiver(GameRuleHelloPayload.ID, (payload, context) -> {
			this.handleHello(context.player().networkHandler, payload);
		});
		ServerPlayNetworking.registerGlobalReceiver(SetGameRulePayload.ID, (payload, context) -> {
			this.handleGameRuleSet(context.player().networkHandler, payload);
		});
	}

	private void handleHello(ServerPlayNetworkHandler handler, GameRuleHelloPayload payload) {
		if (VERSION >= payload.version) {
			this.validPlayers.add(handler);
			this.updatePlayerStatus(handler.player);
			if (EssentialSettings.gameRuleSync) {
				this.sendAllRules(handler.player);
			}
		}
	}

	private void handleGameRuleSet(ServerPlayNetworkHandler handler, SetGameRulePayload payload) {
		ServerPlayerEntity player = handler.player;
		String playerName = player.getNameForScoreboard();
		if (!this.validPlayers.contains(handler)) {
			EssentialAddons.LOGGER.warn("{} tried to send data without saying hello!", playerName);
			return;
		}

		if (!EssentialSettings.gameRuleSync) {
			EssentialAddons.LOGGER.warn("{} tried to set gamerules without it being enabled!", playerName);
			return;
		}

		GameRules.Key<?> gameRuleKey = this.keyMap.get(payload.name);
		if (gameRuleKey == null) {
			EssentialAddons.LOGGER.warn("Received bad Game Rule packet from {}!", playerName);
			return;
		}
		GameRules.Rule<?> rule = player.server.getGameRules().get(gameRuleKey);
		((RuleInvoker) rule).deserialize(payload.value);
		((IRule) rule).essentialaddons$ruleChanged(player);
	}

	public record GameRuleHelloPayload(String brand, int version) implements CustomPayload {
		public static final Id<GameRuleHelloPayload> ID = new CustomPayload.Id<>(Identifier.of("essential:game_rule_hello"));
		public static final PacketCodec<PacketByteBuf, GameRuleHelloPayload> CODEC = PacketCodec.of(
			(payload, buf) -> buf.writeString(payload.brand).writeInt(payload.version),
			(buf) -> new GameRuleHelloPayload(buf.readString(), buf.readInt())
		);

		@Override
		public Id<? extends CustomPayload> getId() {
			return ID;
		}
	}

	public record SetGameRulePayload(String name, String value) implements CustomPayload {
		public static final Id<SetGameRulePayload> ID = new CustomPayload.Id<>(Identifier.of("essential:set_game_rule"));
		public static final PacketCodec<PacketByteBuf, SetGameRulePayload> CODEC = PacketCodec.of(
			(payload, buf) -> buf.writeString(payload.name).writeString(payload.value),
			(buf) -> new SetGameRulePayload(buf.readString(), buf.readString())
		);

		@Override
		public Id<? extends CustomPayload> getId() {
			return ID;
		}
	}

	public record GameRulesChangedPayload(NbtCompound compound) implements CustomPayload {
		public static final Id<GameRulesChangedPayload> ID = new CustomPayload.Id<>(Identifier.of("essential:game_rules_changed"));
		public static final PacketCodec<PacketByteBuf, GameRulesChangedPayload> CODEC = PacketCodec.of(
			(payload, buf) -> buf.writeNbt(payload.compound),
			(buf) -> new GameRulesChangedPayload(buf.readNbt())
		);

		@Override
		public Id<? extends CustomPayload> getId() {
			return ID;
		}
	}

	public record GameRulePermissionsPayload(boolean canUpdateGamerules) implements CustomPayload {
		public static final Id<GameRulePermissionsPayload> ID = new CustomPayload.Id<>(Identifier.of("essential:game_rule_permissions"));
		public static final PacketCodec<PacketByteBuf, GameRulePermissionsPayload> CODEC = PacketCodec.of(
			(payload, buf) -> buf.writeBoolean(payload.canUpdateGamerules),
			(buf) -> new GameRulePermissionsPayload(buf.readBoolean())
		);

		@Override
		public Id<? extends CustomPayload> getId() {
			return ID;
		}
	}
}
