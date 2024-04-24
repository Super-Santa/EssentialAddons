package essentialaddons.mixins.stackableShulkers;

import essentialaddons.EssentialSettings;
import essentialaddons.EssentialUtils;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.ContainerComponent;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

// We need to mixin before Tweakeroo
@Mixin(value = ItemStack.class, priority = 900)
public abstract class ItemStackMixin {
    @Shadow
    public abstract Item getItem();

    @Inject(method = "getMaxCount", at = @At("HEAD"), cancellable = true)
    public void getMaxCount(CallbackInfoReturnable<Integer> cir) {
        if (EssentialSettings.stackableShulkersInPlayerInventories && EssentialUtils.isItemShulkerBox(this.getItem())) {
            ItemStack stack = (ItemStack) (Object) this;
            ContainerComponent container = stack.get(DataComponentTypes.CONTAINER);
            if (container == null || container.streamNonEmpty().findAny().isEmpty()) {
                cir.setReturnValue(64);
            } else if (EssentialSettings.stackableShulkersWithItems) {
                cir.setReturnValue(64);
            }
        }
    }
}