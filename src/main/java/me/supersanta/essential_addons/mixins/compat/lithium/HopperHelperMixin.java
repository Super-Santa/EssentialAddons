package me.supersanta.essential_addons.mixins.compat.lithium;

import me.supersanta.essential_addons.EssentialSettings;
import me.supersanta.essential_addons.utils.EssentialUtilsKt;
import net.caffeinemc.mods.lithium.common.hopper.HopperHelper;
import net.minecraft.core.Direction;
import net.minecraft.world.Container;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(HopperHelper.class)
public class HopperHelperMixin {
    @Inject(
        method = "tryMoveSingleItem(Lnet/minecraft/world/Container;Lnet/minecraft/world/WorldlyContainer;Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/item/ItemStack;ILnet/minecraft/core/Direction;)Z",
        at = @At("HEAD"),
        cancellable = true
    )
    private static void onTryMoveItem(
        Container to,
        WorldlyContainer toSided,
        ItemStack transferStack,
        ItemStack transferChecker,
        int targetSlot,
        Direction fromDirection,
        CallbackInfoReturnable<Boolean> cir
    ) {
        if (EssentialSettings.stackableShulkersInPlayerInventories) {
            ItemStack stack = to.getItem(targetSlot);
            if (EssentialUtilsKt.isShulkerBox(stack)) {
                cir.setReturnValue(false);
            }
        }
    }
}
