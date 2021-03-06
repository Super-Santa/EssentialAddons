package essentialaddons.mixins.essentialCarefulDrop;

import essentialaddons.EssentialUtils;
import essentialaddons.utils.Subscription;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.vehicle.HopperMinecartEntity;
import net.minecraft.item.ItemConvertible;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(HopperMinecartEntity.class)
public abstract class HopperMinecartEntityMixin {
	@Redirect(method = "dropItems", at = @At(value = "INVOKE", target = "net/minecraft/entity/vehicle/HopperMinecartEntity.dropItem(Lnet/minecraft/item/ItemConvertible;)Lnet/minecraft/entity/ItemEntity;"))
	private ItemEntity onDropStack(HopperMinecartEntity e, ItemConvertible item, DamageSource damageSource) {
		if (EssentialUtils.tryCareful(damageSource.getAttacker(), Subscription.ESSENTIAL_CAREFUL_DROP, item.asItem().getDefaultStack())) {
			return null;
		}
		return e.dropItem(item);
	}
}
