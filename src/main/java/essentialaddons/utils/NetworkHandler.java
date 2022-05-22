package essentialaddons.utils;

import essentialaddons.EssentialAddons;
import io.netty.buffer.Unpooled;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.packet.s2c.play.CustomPayloadS2CPacket;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

import java.util.HashSet;
import java.util.Set;

public abstract class NetworkHandler {
	public static final int
		HELLO = 0,
		DATA = 16;

	private final Set<ServerPlayerEntity> validPlayers = new HashSet<>();

	public abstract Identifier getNetworkChannel();

	public abstract int getVersion();

	public Set<ServerPlayerEntity> getValidPlayers() {
		return this.validPlayers;
	}

	public void handlePacket(PacketByteBuf packetByteBuf, ServerPlayerEntity player) {
		if (packetByteBuf != null) {
			switch (packetByteBuf.readVarInt()) {
				case HELLO -> this.onHello(packetByteBuf, player);
				case DATA -> {
					if (this.validPlayers.contains(player)) {
						this.processData(packetByteBuf, player);
						break;
					}
					EssentialAddons.LOGGER.warn("{} tried to send data without saying hello", player);
				}
			}
		}
	}

	public void sayHello(ServerPlayerEntity player) {
		player.networkHandler.sendPacket(new CustomPayloadS2CPacket(
			this.getNetworkChannel(),
			new PacketByteBuf(Unpooled.buffer()).writeVarInt(HELLO).writeVarInt(this.getVersion())
		));
	}

	public final void onHello(PacketByteBuf packetByteBuf, ServerPlayerEntity player) {
		packetByteBuf.readString(32767);
		if (packetByteBuf.readableBytes() == 0 || packetByteBuf.readVarInt() < this.getVersion()) {
			this.onHelloFail(player);
			return;
		}
		this.validPlayers.add(player);
		this.onHelloSuccess(player);
	}

	protected void onHelloSuccess(ServerPlayerEntity player) { }

	protected void onHelloFail(ServerPlayerEntity player) { }

	protected abstract void processData(PacketByteBuf packetByteBuf, ServerPlayerEntity player);

}
