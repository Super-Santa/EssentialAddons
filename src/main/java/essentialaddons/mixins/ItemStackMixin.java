package essentialaddons.mixins;

import essentialaddons.EssentialAddonsSettings;
import essentialaddons.EssentialAddonsUtils;
import net.minecraft.block.ShulkerBoxBlock;
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundTag;
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
    public void getMaxCount(CallbackInfoReturnable<Integer> cir){
        if (EssentialAddonsSettings.stackableShulkersInPlayerInventories) {
            ItemStack stack = (ItemStack) (Object) this;
            if (EssentialAddonsSettings.inventoryStacking && !EssentialAddonsUtils.shulkerBoxHasItems(stack) && (((BlockItem) stack.getItem()).getBlock() instanceof ShulkerBoxBlock)) {
                cir.setReturnValue(64);
            }
        }
    }
}