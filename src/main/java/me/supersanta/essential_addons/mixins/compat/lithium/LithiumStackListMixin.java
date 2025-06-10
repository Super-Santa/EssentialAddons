package me.supersanta.essential_addons.mixins.compat.lithium;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import me.supersanta.essential_addons.EssentialSettings;
import me.supersanta.essential_addons.utils.EssentialUtilsKt;
import net.caffeinemc.mods.lithium.common.hopper.LithiumStackList;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(LithiumStackList.class)
public class LithiumStackListMixin {
    @WrapOperation(
        method = {
            "<init>(Lnet/minecraft/core/NonNullList;I)V",
            "changedALot",
            "lithium$notifyCount(Lnet/minecraft/world/item/ItemStack;II)V",
            "set(ILnet/minecraft/world/item/ItemStack;)Lnet/minecraft/world/item/ItemStack;"
        },
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/item/ItemStack;getMaxStackSize()I"
        )
    )
    private int onGetMaxStackSize(
        ItemStack stack,
        Operation<Integer> original
    ) {
        if (!EssentialSettings.stackableShulkersInPlayerInventories || !EssentialUtilsKt.isShulkerBox(stack)) {
            return original.call(stack);
        }
        return 1;
    }

    @ModifyExpressionValue(
        method = "calculateSignalStrength",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/item/ItemStack;getMaxStackSize()I"
        )
    )
    private int onGetMaxStackSize(int original, @Local ItemStack stack) {
        if (EssentialSettings.stackableShulkerComparatorOverloadFix || !EssentialUtilsKt.isShulkerBox(stack)) {
            return original;
        }
        return 1;
    }
}
