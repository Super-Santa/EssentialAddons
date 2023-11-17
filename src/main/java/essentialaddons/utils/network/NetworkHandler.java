package essentialaddons.utils.network;

import essentialaddons.EssentialAddons;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.PacketByteBuf;
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

	private final Identifier channel;

	public NetworkHandler(Identifier channel) {
		this.channel = channel;
		ServerPlayNetworking.registerGlobalReceiver(
			channel,
			(s, p, handler, buf, r) -> s.execute(() -> this.handlePacket(buf, handler))
		);
	}

	public abstract int getVersion();

	public final Identifier getNetworkChannel() {
		return this.channel;
	}

	public final Set<ServerPlayerEntity> getValidPlayers() {
		return this.validPlayers.stream().map(h -> h.player).collect(Collectors.toSet());
	}

	public final void handlePacket(PacketByteBuf packetByteBuf, ServerPlayNetworkHandler handler) {
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

	public final void sayHello(ServerPlayerEntity player) {
		this.sendPacketTo(player, buf -> buf.writeVarInt(HELLO).writeVarInt(this.getVersion()));
	}

	protected final void sendDataPacketToAll(PacketWriter writer) {
		PacketByteBuf buf = PacketByteBufs.create();
		writer.write(buf);
		for (ServerPlayerEntity player : this.getValidPlayers()) {
			if (player.networkHandler != null) {
				ServerPlayNetworking.send(player, this.getNetworkChannel(), buf);
			}
		}
	}

	protected final void sendDataPacketTo(ServerPlayerEntity player, PacketWriter writer) {
		this.sendPacketTo(player, buf -> {
			buf.writeVarInt(DATA);
			writer.write(buf);
		});
	}

	protected final void sendPacketTo(ServerPlayerEntity player, PacketWriter writer) {
		if (player.networkHandler != null) {
			PacketByteBuf buf = PacketByteBufs.create();
			writer.write(buf);
			ServerPlayNetworking.send(player, this.getNetworkChannel(), buf);
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
