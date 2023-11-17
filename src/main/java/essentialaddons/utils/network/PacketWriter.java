package essentialaddons.utils.network;

import net.minecraft.network.PacketByteBuf;

@FunctionalInterface
public interface PacketWriter {
	void write(PacketByteBuf buf);
}
