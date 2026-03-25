package me.supersanta.essential_addons.mixins.feature.stackable_shulkers;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import me.supersanta.essential_addons.EssentialSettings;
import me.supersanta.essential_addons.utils.EssentialUtilsKt;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(AbstractContainerMenu.class)
public class AbstractContainerMenuMixin {
    @ModifyExpressionValue(
        method = "getRedstoneSignalFromContainer",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/Container;getMaxStackSize(Lnet/minecraft/world/item/ItemStack;)I"
        )
    )
    private static int onGetMaxStackSize(int original, @Local(name = "itemStack") ItemStack stack) {
        if (EssentialSettings.stackableShulkerComparatorOverloadFix || !EssentialUtilsKt.isShulkerBox(stack.getItem())) {
            return original;
        }
        return 1;
    }

    @ModifyExpressionValue(
        method = "sendAllDataToRemote",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/item/ItemStack;copy()Lnet/minecraft/world/item/ItemStack;"
        )
    )
    private ItemStack onCopyItemStackForRemote(ItemStack original) {
        if (EssentialSettings.stackableShulkersInPlayerInventories && EssentialUtilsKt.isShulkerBox(original.getItem())) {
            original.set(DataComponents.MAX_STACK_SIZE, 64);
        }
        return original;
    }
}
