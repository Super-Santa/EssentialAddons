package essentialaddons.mixins.lithium;

import essentialaddons.EssentialAddonsSettings;
import me.jellysquid.mods.lithium.common.hopper.HopperHelper;
import net.minecraft.block.ShulkerBoxBlock;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SidedInventory;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Direction;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(value = HopperHelper.class, remap = false)
public class HopperHelperMixin {
    /**
     * @author Willow and Sensei
     * This method is to fix an issue with lithium stacking shulkers with hoppers when using `stackableShulkersInPlayerInventories`
     *
     * Using an @Overwrite because was having issues with mapping using @Redirect
     * Was previously Redirecting `isOf` method however the server couldn't find said method for whatever reason
     * Feel free to make a PR and change if you get it working without @Overwrite
     * Added lines 38-39s
     */
    @Overwrite
    public static boolean tryMoveSingleItem(Inventory to, @Nullable SidedInventory toSided, ItemStack transferStack, int targetSlot, @Nullable Direction fromDirection) {
        ItemStack toStack = to.getStack(targetSlot);
        //Assumption: no mods depend on the the stack size of transferStack in isValid or canInsert, like vanilla doesn't
        if (to.isValid(targetSlot, transferStack) && (toSided == null || toSided.canInsert(targetSlot, transferStack, fromDirection))) {
            int toCount;
            if (toStack.isEmpty()) {
                ItemStack copy = transferStack.copy();
                copy.setCount(1);
                transferStack.decrement(1);
                to.setStack(targetSlot, copy);
                return true; //caller needs to call to.markDirty()
            } else if (EssentialAddonsSettings.stackableShulkersInPlayerInventories && toStack.getItem() instanceof BlockItem && ((BlockItem) toStack.getItem()).getBlock() instanceof ShulkerBoxBlock) {
                return false;
            } else if (toStack.isOf(transferStack.getItem()) && toStack.getMaxCount() > (toCount = toStack.getCount()) && to.getMaxCountPerStack() > toCount && ItemStack.areNbtEqual(toStack, transferStack)) {
                transferStack.decrement(1);
                toStack.increment(1);
                return true; //caller needs to call to.markDirty()
            }
        }
        return false;
    }
}