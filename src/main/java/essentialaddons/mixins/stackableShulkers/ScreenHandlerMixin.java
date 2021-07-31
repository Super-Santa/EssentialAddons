package essentialaddons.mixins.stackableShulkers;

import essentialaddons.EssentialAddonsSettings;
import net.minecraft.block.ShulkerBoxBlock;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ScreenHandler.class)
public class ScreenHandlerMixin {
    @Redirect(method = "calculateComparatorOutput(Lnet/minecraft/inventory/Inventory;)I", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;getMaxCount()I"))
    private static int onGetMaxCount(ItemStack itemStack) {
        if (!EssentialAddonsSettings.stackableShulkerComparatorOverloadFix && itemStack.getItem() instanceof BlockItem && ((BlockItem) itemStack.getItem()).getBlock() instanceof ShulkerBoxBlock)
            return 1;
        return itemStack.getMaxCount();
    }
}