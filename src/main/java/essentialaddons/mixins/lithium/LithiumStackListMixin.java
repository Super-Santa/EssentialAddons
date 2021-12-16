package essentialaddons.mixins.lithium;

import essentialaddons.EssentialAddonsSettings;
import net.minecraft.block.ShulkerBoxBlock;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Pseudo
@Mixin(targets = "me.jellysquid.mods.lithium.common.hopper.LithiumStackList", remap = false)
public class LithiumStackListMixin{
    @SuppressWarnings("UnresolvedMixinReference")
    @Redirect(method = "calculateSignalStrength", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;getMaxCount()I"))
    private int onGetMaxCount(ItemStack itemStack) {
        if (!EssentialAddonsSettings.stackableShulkerComparatorOverloadFix && itemStack.getItem() instanceof BlockItem && ((BlockItem) itemStack.getItem()).getBlock() instanceof ShulkerBoxBlock)
            return 1;
        return itemStack.getMaxCount();
    }
}
