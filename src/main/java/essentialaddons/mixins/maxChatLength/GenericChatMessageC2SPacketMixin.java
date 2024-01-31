package essentialaddons.mixins.maxChatLength;

import essentialaddons.EssentialSettings;
import net.minecraft.network.packet.c2s.play.ChatMessageC2SPacket;
import net.minecraft.network.packet.c2s.play.CommandExecutionC2SPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin({ChatMessageC2SPacket.class, CommandExecutionC2SPacket.class})
public class GenericChatMessageC2SPacketMixin {
	@ModifyConstant(method = "<init>(Lnet/minecraft/network/PacketByteBuf;)V", constant = @Constant(intValue = 256))
	private static int getMaxLength(int constant) {
		return EssentialSettings.maxChatLength;
	}
}
