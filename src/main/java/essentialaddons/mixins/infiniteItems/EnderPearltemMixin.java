package essentialaddons.mixins.infiniteItems;

import essentialaddons.EssentialSettings;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.EnderPearlItem;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.s2c.play.ScreenHandlerSlotUpdateS2CPacket;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(EnderPearlItem.class)
public class EnderPearltemMixin {
    @Redirect(method = "use", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;decrement(I)V"), require = 0)
    private void onDecrement(ItemStack itemStack, int amount, World world, PlayerEntity user) {
        if (EssentialSettings.infiniteItems) {
            if (user instanceof ServerPlayerEntity playerEntity) {
                int slot = playerEntity.getInventory().selectedSlot + 36;
                playerEntity.networkHandler.sendPacket(new ScreenHandlerSlotUpdateS2CPacket(0, 0, slot, itemStack));
            }
        }
        else {
            itemStack.decrement(1);
        }
    }
}
