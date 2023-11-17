package essentialaddons.utils.network;

import essentialaddons.EssentialAddons;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.network.packet.c2s.common.CustomPayloadC2SPacket;
import net.minecraft.network.packet.s2c.common.CustomPayloadS2CPacket;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public abstract class NetworkHandler {
	public static final int
		HELLO = 0,
		DATA = 16;

	private final Set<ServerPlayNetworkHandler> validPlayers = new HashSet<>();

	public abstract Identifier getNetworkChannel();

	public abstract int getVersion();

	public Set<ServerPlayerEntity> getValidPlayers() {
		return this.validPlayers.stream().map(h -> h.player).collect(Collectors.toSet());
	}

	public void handlePacket(PacketByteBuf packetByteBuf, ServerPlayNetworkHandler handler) {
		if (packetByteBuf != null) {
			switch (packetByteBuf.readVarInt()) {
				case HELLO -> this.onHello(packetByteBuf, handler);
				case DATA -> {
					if (this.validPlayers.contains(handler)) {
						this.processData(packetByteBuf, handler);
						break;
					}
					EssentialAddons.LOGGER.warn("{} tried to send data without saying hello", handler);
				}
			}
		}
	}

	public void sayHello(ServerPlayerEntity player) {
		this.sendPacketTo(player, buf -> buf.writeVarInt(HELLO).writeVarInt(this.getVersion()));
	}

	protected void sendDataPacketToAll(PacketWriter writer) {
		CustomPayload payload = new CustomPacketWriter(this.getNetworkChannel(), writer);
		CustomPayloadS2CPacket packet = new CustomPayloadS2CPacket(payload);
		for (ServerPlayerEntity player : this.getValidPlayers()) {
			if (player.networkHandler != null) {
				player.networkHandler.sendPacket(packet);
			}
		}
	}

	protected void sendDataPacketTo(ServerPlayerEntity player, PacketWriter writer) {
		this.sendPacketTo(player, buf -> {
			buf.writeVarInt(DATA);
			writer.write(buf);
		});
	}

	protected void sendPacketTo(ServerPlayerEntity player, PacketWriter writer) {
		if (player.networkHandler != null) {
			player.networkHandler.sendPacket(new CustomPayloadC2SPacket(
				new CustomPacketWriter(this.getNetworkChannel(), writer)
			));
		}
	}

	public final void onHello(PacketByteBuf packetByteBuf, ServerPlayNetworkHandler handler) {
		packetByteBuf.readString(32767);
		if (packetByteBuf.readableBytes() == 0 || packetByteBuf.readVarInt() < this.getVersion()) {
			this.onHelloFail(handler);
			return;
		}
		this.validPlayers.add(handler);
		this.onHelloSuccess(handler);
	}

	protected void onHelloSuccess(ServerPlayNetworkHandler handler) { }

	protected void onHelloFail(ServerPlayNetworkHandler handler) { }

	protected abstract void processData(PacketByteBuf packetByteBuf, ServerPlayNetworkHandler handler);
}
