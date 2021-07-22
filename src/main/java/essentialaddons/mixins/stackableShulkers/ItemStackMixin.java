package essentialaddons.mixins.stackableShulkers;

import essentialaddons.EssentialAddonsSettings;
import carpet.helpers.InventoryHelper;
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
        if ((EssentialAddonsSettings.stackableShulkersInPlayerInventories || EssentialAddonsSettings.betterStackableShulkers) && EssentialAddonsSettings.inventoryStacking && this.getItem().toString().contains("shulker_box")) {
            ItemStack stack = (ItemStack) (Object) this;
            if (!InventoryHelper.shulkerBoxHasItems(stack)) {
                    stack.removeSubTag("BlockEntityTag");
                    cir.setReturnValue(64);
            }
            else if (EssentialAddonsSettings.stackableShulkersWithItems)
                    cir.setReturnValue(64);
        }
    }
}