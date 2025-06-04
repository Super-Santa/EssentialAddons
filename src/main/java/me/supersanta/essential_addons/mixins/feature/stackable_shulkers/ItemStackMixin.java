package me.supersanta.essential_addons.mixins.feature.stackable_shulkers;

import me.supersanta.essential_addons.EssentialSettings;
import me.supersanta.essential_addons.utils.EssentialUtilsKt;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemContainerContents;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

// Mixin before Tweakeroo
@Mixin(value = ItemStack.class, priority = 900)
public abstract class ItemStackMixin {
    @Shadow public abstract Item getItem();

    @Inject(
        method = "getMaxStackSize",
        at = @At("HEAD"),
        cancellable = true
    )
    private void onGetMaxStackSize(CallbackInfoReturnable<Integer> cir) {
        if (EssentialSettings.stackableShulkersInPlayerInventories) {
            ItemStack stack = (ItemStack) (Object) this;
            if (EssentialUtilsKt.isShulkerBox(stack)) {
                ItemContainerContents contents = stack.get(DataComponents.CONTAINER);
                if (contents == null || contents.nonEmptyStream().findAny().isEmpty()) {
                    cir.setReturnValue(64);
                } else if (EssentialSettings.stackableShulkersWithItems) {
                    cir.setReturnValue(64);
                }
            }
        }
    }
}
