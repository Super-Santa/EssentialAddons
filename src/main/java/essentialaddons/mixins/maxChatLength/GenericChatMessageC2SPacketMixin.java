package essentialaddons.mixins.maxChatLength;

import essentialaddons.EssentialSettings;
import net.minecraft.network.packet.c2s.play.ChatMessageC2SPacket;
//#if MC >= 11900
import net.minecraft.network.packet.c2s.play.CommandExecutionC2SPacket;
import net.minecraft.network.packet.c2s.play.RequestChatPreviewC2SPacket;
//#endif
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

//#if MC >= 11900
@Mixin({ChatMessageC2SPacket.class, RequestChatPreviewC2SPacket.class, CommandExecutionC2SPacket.class})
//#else
//$$@Mixin(ChatMessageC2SPacket.class)
//#endif
public class GenericChatMessageC2SPacketMixin {
	@ModifyConstant(method = "<init>(Lnet/minecraft/network/PacketByteBuf;)V", constant = @Constant(intValue = 256))
	private static int getMaxLength(int constant) {
		return EssentialSettings.maxChatLength;
	}
}
