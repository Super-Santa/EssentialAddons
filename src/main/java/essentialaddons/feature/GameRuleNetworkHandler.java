package essentialaddons.feature;

import essentialaddons.EssentialAddons;
import essentialaddons.EssentialSettings;
import essentialaddons.mixins.gameRuleSync.RuleInvoker;
import essentialaddons.utils.NetworkHandler;
import essentialaddons.utils.ducks.IRule;
import io.netty.buffer.Unpooled;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.packet.s2c.play.CustomPayloadS2CPacket;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.world.GameRules;

import java.util.HashMap;
import java.util.Map;

public class GameRuleNetworkHandler extends NetworkHandler {
	public static final GameRuleNetworkHandler INSTANCE = new GameRuleNetworkHandler();

	public static Identifier GAME_RULE_CHANNEL = new Identifier("essentialclient", "gamerule");

	private final Map<String, GameRules.Key<?>> keyMap = new HashMap<>();

	private GameRuleNetworkHandler() { }

	public void onHelloSuccess(ServerPlayerEntity player) {
		this.updatePlayerStatus(player);
		if (EssentialSettings.gameRuleSync) {
			this.sendAllRules(player);
		}
	}

	public void updatePlayerStatus(ServerPlayerEntity player) {
		PacketByteBuf byteBuf = new PacketByteBuf(Unpooled.buffer()).writeVarInt(15);
		byteBuf.writeBoolean(EssentialSettings.gameRuleSync && (EssentialSettings.gameRuleNonOp || player.hasPermissionLevel(2)));
		player.networkHandler.sendPacket(new CustomPayloadS2CPacket(GAME_RULE_CHANNEL, byteBuf));
	}

	public void sendAllRules(ServerPlayerEntity player) {
		PacketByteBuf byteBuf = new PacketByteBuf(Unpooled.buffer()).writeVarInt(DATA);
		player.networkHandler.sendPacket(new CustomPayloadS2CPacket(
			GAME_RULE_CHANNEL, byteBuf.writeNbt(player.server.getGameRules().toNbt())
		));
	}

	public void processData(PacketByteBuf packetByteBuf, ServerPlayerEntity player) {
		if (EssentialSettings.gameRuleSync) {
			String ruleName = packetByteBuf.readString();
			String ruleValue = packetByteBuf.readString();
			GameRules.Key<?> gameRuleKey = this.keyMap.get(ruleName);
			if (gameRuleKey == null) {
				EssentialAddons.LOGGER.warn("Received bad Game Rule packet from %s".formatted(player.getEntityName()));
				return;
			}
			GameRules.Rule<?> rule = player.server.getGameRules().get(gameRuleKey);
			((RuleInvoker) rule).deserialize(ruleValue);
			((IRule) rule).ruleChanged(player);
		}
	}

	public void onRuleChange(String ruleName, String ruleValue) {
		NbtCompound compound = new NbtCompound();
		compound.putString(ruleName, ruleValue);
		this.getValidPlayers().forEach(player -> {
			PacketByteBuf byteBuf = new PacketByteBuf(Unpooled.buffer()).writeVarInt(DATA);
			player.networkHandler.sendPacket(new CustomPayloadS2CPacket(
				GAME_RULE_CHANNEL, byteBuf.writeNbt(compound)
			));
		});
	}

	public void addGameRuleKey(GameRules.Key<?> gameRuleKey) {
		this.keyMap.put(gameRuleKey.getName(), gameRuleKey);
	}

	@Override
	public Identifier getNetworkChannel() {
		return GAME_RULE_CHANNEL;
	}

	@Override
	public int getVersion() {
		return 1_0_0;
	}
}
