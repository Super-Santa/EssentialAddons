package essentialaddons.mixins.core;

import essentialaddons.EssentialAddons;
import essentialaddons.utils.network.HandlerPayload;
import essentialaddons.utils.network.NetworkHandler;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.network.packet.c2s.common.CustomPayloadC2SPacket;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(CustomPayloadC2SPacket.class)
public class CustomPayloadC2SPacketMixin {
	@Inject(
		method = "readPayload",
		at = @At("HEAD"),
		cancellable = true
	)
	private static void onReadPayload(
		Identifier id,
		PacketByteBuf buf,
		CallbackInfoReturnable<CustomPayload> cir
	) {
		for (NetworkHandler handler : EssentialAddons.NETWORK_HANDLERS) {
			if (id.equals(handler.getNetworkChannel())) {
				cir.setReturnValue(new HandlerPayload(handler, id, buf));
			}
		}
	}
}
