package essentialaddons.mixins.stackableShulkers;

import essentialaddons.EssentialAddonsSettings;
import carpet.helpers.InventoryHelper;
import net.minecraft.block.ShulkerBoxBlock;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin {
    @Shadow
    public abstract Item getItem();

    @Inject(method = "getMaxCount", at=@At("RETURN"), cancellable = true)
    public void getMaxCount(CallbackInfoReturnable<Integer> cir) {
        if (EssentialAddonsSettings.stackableShulkersInPlayerInventories && this.getItem() instanceof BlockItem && ((BlockItem) this.getItem()).getBlock() instanceof ShulkerBoxBlock) {
            ItemStack stack = (ItemStack) (Object) this;
            if (!InventoryHelper.shulkerBoxHasItems(stack)) {
                stack.removeSubNbt("BlockEntityTag");
                cir.setReturnValue(64);
            }
            else if (EssentialAddonsSettings.stackableShulkersWithItems)
                cir.setReturnValue(64);
        }
    }
}