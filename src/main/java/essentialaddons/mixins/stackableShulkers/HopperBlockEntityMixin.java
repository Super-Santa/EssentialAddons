package essentialaddons.mixins.stackableShulkers;

import essentialaddons.EssentialSettings;
import essentialaddons.EssentialUtils;
import net.minecraft.block.entity.HopperBlockEntity;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(HopperBlockEntity.class)
public class HopperBlockEntityMixin {
    @Inject(method = "canMergeItems", at = @At("HEAD"), cancellable = true, require = 0)
    private static void canMergeItems(ItemStack first, ItemStack second, CallbackInfoReturnable<Boolean> cir) {
        if (EssentialSettings.stackableShulkersInPlayerInventories && EssentialUtils.isItemShulkerBox(first.getItem())) {
            cir.setReturnValue(false);
        }
    }
}
