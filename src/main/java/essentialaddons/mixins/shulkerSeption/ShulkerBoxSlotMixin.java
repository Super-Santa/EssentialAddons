package essentialaddons.mixins.shulkerSeption;

import essentialaddons.EssentialAddonsSettings;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.ShulkerBoxSlot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ShulkerBoxSlot.class)
public class ShulkerBoxSlotMixin {

    @Inject(method = "canInsert", at = @At("HEAD"), cancellable = true)
    private void canInsert(ItemStack stack, CallbackInfoReturnable<Boolean> ci) {
        if (EssentialAddonsSettings.shulkerSception) {
            ci.setReturnValue(true);
        }
    }
}
