package essentialaddons.mixins.stackableShulkers;

import essentialaddons.EssentialSettings;
import net.minecraft.block.ShulkerBoxBlock;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ScreenHandler.class)
public class ScreenHandlerMixin {
    @Redirect(method = "calculateComparatorOutput(Lnet/minecraft/inventory/Inventory;)I", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;getMaxCount()I"), require = 0)
    private static int onGetMaxCount(ItemStack itemStack) {
        if (!EssentialSettings.stackableShulkerComparatorOverloadFix && itemStack.getItem() instanceof BlockItem blockItem && blockItem.getBlock() instanceof ShulkerBoxBlock) {
            return 1;
        }
        return itemStack.getMaxCount();
    }
}