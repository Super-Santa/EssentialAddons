package essentialaddons.mixins.essentialCarefulDrop;

import essentialaddons.EssentialUtils;
import essentialaddons.utils.Subscription;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.vehicle.BoatEntity;
import net.minecraft.item.ItemConvertible;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(BoatEntity.class)
public abstract class BoatEntityMixin extends Entity {
	public BoatEntityMixin(EntityType<?> type, World world) {
		super(type, world);
	}

	@Redirect(method = "dropItems", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/vehicle/BoatEntity;dropItem(Lnet/minecraft/item/ItemConvertible;)Lnet/minecraft/entity/ItemEntity;"))
	private ItemEntity onDropItem(BoatEntity instance, ItemConvertible itemConvertible, DamageSource source) {
		if (EssentialUtils.tryCareful(source.getAttacker(), Subscription.ESSENTIAL_CAREFUL_DROP, itemConvertible.asItem().getDefaultStack())) {
			return null;
		}
		return instance.dropItem(itemConvertible);
	}
}
