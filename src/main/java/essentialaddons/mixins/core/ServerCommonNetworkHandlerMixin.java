package essentialaddons.mixins.core;

import essentialaddons.utils.network.HandlerPayload;
import net.minecraft.network.NetworkThreadUtils;
import net.minecraft.network.listener.ServerCommonPacketListener;
import net.minecraft.network.packet.c2s.common.CustomPayloadC2SPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerCommonNetworkHandler;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerCommonNetworkHandler.class)
public class ServerCommonNetworkHandlerMixin {
	@Shadow @Final protected MinecraftServer server;

	@SuppressWarnings("ConstantValue")
	@Inject(method = "onCustomPayload", at = @At("HEAD"), cancellable = true)
	private void onCustomPayload(CustomPayloadC2SPacket packet, CallbackInfo ci) {
		if ((Object) this instanceof ServerPlayNetworkHandler network && packet.payload() instanceof HandlerPayload handler) {
			NetworkThreadUtils.forceMainThread(packet, (ServerCommonPacketListener) this, this.server);
			handler.handle(network);
			ci.cancel();
		}
	}
}
