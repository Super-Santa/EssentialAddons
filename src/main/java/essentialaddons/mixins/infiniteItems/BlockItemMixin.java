package essentialaddons.mixins.infiniteItems;

import essentialaddons.EssentialSettings;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.s2c.play.ScreenHandlerSlotUpdateS2CPacket;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(BlockItem.class)
public class BlockItemMixin {
    @Redirect(method = "place(Lnet/minecraft/item/ItemPlacementContext;)Lnet/minecraft/util/ActionResult;", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;decrement(I)V"), require = 0)
    private void onDecrement(ItemStack itemStack, int amount, ItemPlacementContext context) {
        if (EssentialSettings.infiniteItems) {
            if (context.getPlayer() instanceof ServerPlayerEntity playerEntity) {
                int slot = playerEntity.getInventory().selectedSlot + 36;
                playerEntity.networkHandler.sendPacket(new ScreenHandlerSlotUpdateS2CPacket(0, 0, slot, itemStack));
            }
        } else {
            itemStack.decrement(1);
        }
    }
}
