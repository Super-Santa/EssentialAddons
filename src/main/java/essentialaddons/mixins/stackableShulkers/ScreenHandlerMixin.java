package essentialaddons.mixins.stackableShulkers;

import essentialaddons.EssentialSettings;
import essentialaddons.EssentialUtils;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ScreenHandler.class)
public class ScreenHandlerMixin {
    @Redirect(method = "calculateComparatorOutput(Lnet/minecraft/inventory/Inventory;)I", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;getMaxCount()I"), require = 0)
    private static int onGetMaxCount(ItemStack itemStack) {
        if (!EssentialSettings.stackableShulkerComparatorOverloadFix && EssentialUtils.isItemShulkerBox(itemStack.getItem())) {
            return 1;
        }
        return itemStack.getMaxCount();
    }
}