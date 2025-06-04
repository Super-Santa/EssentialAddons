package me.supersanta.essential_addons.mixins.feature.stackable_shulkers;

import me.supersanta.essential_addons.EssentialSettings;
import me.supersanta.essential_addons.utils.EssentialUtilsKt;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.HopperBlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(HopperBlockEntity.class)
public class HopperBlockEntityMixin {
    @Inject(
        method = "canMergeItems",
        at = @At("HEAD"),
        cancellable = true
    )
    private static void onCanMergeItems(
        ItemStack first,
        ItemStack second,
        CallbackInfoReturnable<Boolean> cir
    ) {
        if (EssentialSettings.stackableShulkersInPlayerInventories) {
            if (EssentialUtilsKt.isShulkerBox(first)) {
                cir.setReturnValue(false);
            }
        }
    }
}
