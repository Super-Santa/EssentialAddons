package essentialaddons.mixins.lithium;

import essentialaddons.EssentialSettings;
import essentialaddons.EssentialUtils;
import me.jellysquid.mods.lithium.common.hopper.LithiumStackList;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(LithiumStackList.class)
public class LithiumStackListMixin {
    @Redirect(method = "calculateSignalStrength", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;getMaxCount()I"))
    private int onGetMaxCount(ItemStack itemStack) {
        if (!EssentialSettings.stackableShulkerComparatorOverloadFix && EssentialUtils.isItemShulkerBox(itemStack.getItem())) {
            return 1;
        }
        return itemStack.getMaxCount();
    }
}
