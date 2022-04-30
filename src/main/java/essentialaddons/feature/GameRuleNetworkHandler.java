package essentialaddons.feature;

import essentialaddons.EssentialAddons;
import essentialaddons.EssentialSettings;
import essentialaddons.mixins.gameRuleSync.RuleInvoker;
import essentialaddons.utils.ducks.IRule;
import io.netty.buffer.Unpooled;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.packet.s2c.play.CustomPayloadS2CPacket;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.world.GameRules;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class GameRuleNetworkHandler {
	public static Identifier GAME_RULE_CHANNEL = new Identifier("essentialclient", "gamerule");
	public static final int
		VERSION = 1_0_0,
		HELLO = 0,
		DATA = 16;

	private static final Map<String, GameRules.Key<?>> KEY_MAP = new HashMap<>();
	private static final Set<ServerPlayerEntity> VALID_PLAYERS = new HashSet<>();

	public static void handlePacket(PacketByteBuf packetByteBuf, ServerPlayerEntity player) {
		if (packetByteBuf != null) {
			switch (packetByteBuf.readVarInt()) {
				case HELLO -> onHello(packetByteBuf, player);
				case DATA -> processData(packetByteBuf, player);
			}
		}
	}

	public static void sayHello(ServerPlayerEntity player) {
		player.networkHandler.sendPacket(new CustomPayloadS2CPacket(
			GAME_RULE_CHANNEL,
			new PacketByteBuf(Unpooled.buffer()).writeVarInt(HELLO).writeVarInt(VERSION)
		));
	}

	public static void onHello(PacketByteBuf packetByteBuf, ServerPlayerEntity player) {
		// String essentialVersion =
		packetByteBuf.readString(32767);
		if (packetByteBuf.readableBytes() != 0 && packetByteBuf.readVarInt() >= VERSION) {
			VALID_PLAYERS.add(player);
			updatePlayerStatus(player);
			if (EssentialSettings.gameRuleSync) {
				sendAllRules(player);
			}
		}
		// EssentialAddons.LOGGER.info("%s has logged in with EssentialClient %s".formatted(player.getEntityName(), essentialVersion));
	}

	public static void updatePlayerStatus(ServerPlayerEntity player) {
		PacketByteBuf byteBuf = new PacketByteBuf(Unpooled.buffer()).writeVarInt(15);
		byteBuf.writeBoolean(EssentialSettings.gameRuleNonOp || player.hasPermissionLevel(2));
		player.networkHandler.sendPacket(new CustomPayloadS2CPacket(
			GAME_RULE_CHANNEL, byteBuf
		));
	}

	public static void sendAllRules(ServerPlayerEntity player) {
		PacketByteBuf byteBuf = new PacketByteBuf(Unpooled.buffer()).writeVarInt(DATA);
		player.networkHandler.sendPacket(new CustomPayloadS2CPacket(
			GAME_RULE_CHANNEL, byteBuf.writeNbt(player.server.getGameRules().toNbt())
		));
	}

	public static void processData(PacketByteBuf packetByteBuf, ServerPlayerEntity player) {
		String ruleName = packetByteBuf.readString();
		String ruleValue = packetByteBuf.readString();
		GameRules.Key<?> gameRuleKey = KEY_MAP.get(ruleName);
		if (gameRuleKey == null) {
			EssentialAddons.LOGGER.warn("Received bad Game Rule packet from %s".formatted(player.getEntityName()));
			return;
		}
		GameRules.Rule<?> rule = player.server.getGameRules().get(gameRuleKey);
		((RuleInvoker) rule).deserialize(ruleValue);
		((IRule) rule).ruleChanged(player);
	}

	public static void onRuleChange(String ruleName, String ruleValue) {
		NbtCompound compound = new NbtCompound();
		compound.putString(ruleName, ruleValue);
		VALID_PLAYERS.forEach(player -> {
			PacketByteBuf byteBuf = new PacketByteBuf(Unpooled.buffer()).writeVarInt(DATA);
			player.networkHandler.sendPacket(new CustomPayloadS2CPacket(
				GAME_RULE_CHANNEL, byteBuf.writeNbt(compound)
			));
		});
	}

	public static Set<ServerPlayerEntity> getValidPlayers() {
		return VALID_PLAYERS;
	}

	public static void addGameRuleKey(GameRules.Key<?> gameRuleKey) {
		KEY_MAP.put(gameRuleKey.getName(), gameRuleKey);
	}
}
