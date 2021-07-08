package essentialaddons.mixins;

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
        if (EssentialAddonsSettings.stackableShulkersInPlayerInventories && EssentialAddonsSettings.inventoryStacking) {
            if (this.getItem().toString().contains("shulker_box")) {
                ItemStack stack = (ItemStack) (Object) this;
                if (!InventoryHelper.shulkerBoxHasItems(stack)) {
                    stack.removeSubTag("BlockEntityTag");
                    cir.setReturnValue(64);
                }
                else if (EssentialAddonsSettings.stackableShulkersWithItems)
                    cir.setReturnValue(64);
            }
            /* This is the old method:
            Item[] shulkers = {Items.SHULKER_BOX, Items.WHITE_SHULKER_BOX, Items.ORANGE_SHULKER_BOX, Items.MAGENTA_SHULKER_BOX,Items.LIGHT_BLUE_SHULKER_BOX, Items.YELLOW_SHULKER_BOX, Items.LIME_SHULKER_BOX, Items.PINK_SHULKER_BOX, Items.LIGHT_GRAY_SHULKER_BOX, Items.GRAY_SHULKER_BOX, Items.CYAN_SHULKER_BOX, Items.PURPLE_SHULKER_BOX, Items.BLUE_SHULKER_BOX, Items.BROWN_SHULKER_BOX, Items.GREEN_SHULKER_BOX, Items.RED_SHULKER_BOX, Items.BLACK_SHULKER_BOX};
            for (Item shulker : shulkers) {
                if (this.getItem() == shulker) {
                    String x = this.getItem().toString();
                    System.out.println(x);
                    ItemStack stack = (ItemStack) (Object) this;
                    if (!InventoryHelper.shulkerBoxHasItems(stack)) {
                        stack.removeSubTag("BlockEntityTag");
                        cir.setReturnValue(64);
                    }
                    break;
                }
            }
             */
        }
    }
}