package essentialaddons.mixins.infiniteItems;

import essentialaddons.EssentialSettings;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.s2c.play.ScreenHandlerSlotUpdateS2CPacket;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemStack.class)
public class ItemStackMixin {
	@Inject(
		method = "decrementUnlessCreative",
		at = @At("HEAD"),
		cancellable = true
	)
	private void onDecrementUnlessCreative(int amount, LivingEntity entity, CallbackInfo ci) {
		if (entity instanceof ServerPlayerEntity player && !player.isInCreativeMode() && EssentialSettings.infiniteItems) {
			int slot = player.getInventory().selectedSlot + 36;
			player.networkHandler.sendPacket(new ScreenHandlerSlotUpdateS2CPacket(0, 0, slot, (ItemStack) (Object) this));
			ci.cancel();
		}
	}
}
