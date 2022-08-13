package essentialaddons.feature.script;

import carpet.script.CarpetEventServer;
import carpet.script.value.Value;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.List;

public class PacketEvent extends CarpetEventServer.Event {
	public static final PacketEvent EVENT = new PacketEvent();

	private PacketEvent() {
		super("script_packet", 2, true);
	}

	public void onScriptPacket(ServerPlayerEntity player, List<Value> values) {
		this.handler.call(() -> values, player::getCommandSource);
	}
}