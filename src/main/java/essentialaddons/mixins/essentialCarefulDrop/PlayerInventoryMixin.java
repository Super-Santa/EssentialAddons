package essentialaddons.mixins.essentialCarefulDrop;

import essentialaddons.EssentialUtils;
import essentialaddons.utils.Subscription;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(PlayerInventory.class)
public class PlayerInventoryMixin {
	@Redirect(method = "dropAll", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerEntity;dropItem(Lnet/minecraft/item/ItemStack;ZZ)Lnet/minecraft/entity/ItemEntity;"))
	private ItemEntity onDropItem(PlayerEntity instance, ItemStack stack, boolean throwRandomly, boolean retainOwnership) {
		if (EssentialUtils.tryCareful(instance.getAttacker(), Subscription.ESSENTIAL_CAREFUL_DROP, stack)) {
			return null;
		}
		return instance.dropItem(stack, throwRandomly, retainOwnership);
	}
}
