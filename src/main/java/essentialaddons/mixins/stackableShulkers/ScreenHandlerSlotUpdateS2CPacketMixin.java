package essentialaddons.mixins.stackableShulkers;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import essentialaddons.EssentialSettings;
import essentialaddons.EssentialUtils;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.s2c.play.ScreenHandlerSlotUpdateS2CPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ScreenHandlerSlotUpdateS2CPacket.class)
public class ScreenHandlerSlotUpdateS2CPacketMixin {
	@ModifyExpressionValue(
		method = "<init>(IIILnet/minecraft/item/ItemStack;)V",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/item/ItemStack;copy()Lnet/minecraft/item/ItemStack;"
		)
	)
	private ItemStack onCopyItemStack(ItemStack original) {
		if (EssentialSettings.stackableShulkersInPlayerInventories && EssentialUtils.isItemShulkerBox(original.getItem())) {
			original.set(DataComponentTypes.MAX_STACK_SIZE, 64);
		}
		return original;
	}
}
