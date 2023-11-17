package essentialaddons.feature;

import essentialaddons.EssentialAddons;
import essentialaddons.EssentialSettings;
import essentialaddons.mixins.gameRuleSync.RuleInvoker;
import essentialaddons.utils.ducks.IRule;
import essentialaddons.utils.network.NetworkHandler;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayNetworkHandler;
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

	public void onHelloSuccess(ServerPlayNetworkHandler handler) {
		this.updatePlayerStatus(handler.player);
		if (EssentialSettings.gameRuleSync) {
			this.sendAllRules(handler.player);
		}
	}

	public void updatePlayerStatus(ServerPlayerEntity player) {
		this.sendPacketTo(player, buf -> {
			boolean allowed = EssentialSettings.gameRuleSync && (EssentialSettings.gameRuleNonOp || player.hasPermissionLevel(2));
			buf.writeVarInt(15).writeBoolean(allowed);
		});
	}

	public void sendAllRules(ServerPlayerEntity player) {
		this.sendDataPacketTo(player, buf -> buf.writeNbt(player.getWorld().getGameRules().toNbt()));
	}

	public void processData(PacketByteBuf packetByteBuf, ServerPlayNetworkHandler handler) {
		if (EssentialSettings.gameRuleSync) {
			ServerPlayerEntity player = handler.player;
			String ruleName = packetByteBuf.readString();
			String ruleValue = packetByteBuf.readString();
			GameRules.Key<?> gameRuleKey = this.keyMap.get(ruleName);
			if (gameRuleKey == null) {
				EssentialAddons.LOGGER.warn("Received bad Game Rule packet from " + player.getEntityName());
				return;
			}
			GameRules.Rule<?> rule = player.server.getGameRules().get(gameRuleKey);
			((RuleInvoker) rule).deserialize(ruleValue);
			((IRule) rule).essentialaddons$ruleChanged(player);
		}
	}

	public void onRuleChange(String ruleName, String ruleValue) {
		NbtCompound compound = new NbtCompound();
		compound.putString(ruleName, ruleValue);
		this.sendDataPacketToAll(buf -> {
			buf.writeNbt(compound);
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
