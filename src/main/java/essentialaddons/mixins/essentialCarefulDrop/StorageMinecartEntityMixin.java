package essentialaddons.mixins.essentialCarefulDrop;

import essentialaddons.EssentialUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.vehicle.AbstractMinecartEntity;
import net.minecraft.entity.vehicle.StorageMinecartEntity;
import net.minecraft.entity.vehicle.VehicleInventory;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(StorageMinecartEntity.class)
public abstract class StorageMinecartEntityMixin extends AbstractMinecartEntity implements VehicleInventory {
	public StorageMinecartEntityMixin(EntityType<?> type, World world) {
		super(type, world);
	}

	@Redirect(
		method = "killAndDropSelf",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/entity/vehicle/StorageMinecartEntity;onBroken(Lnet/minecraft/entity/damage/DamageSource;Lnet/minecraft/world/World;Lnet/minecraft/entity/Entity;)V"
		)
	)
	private void onBroken(StorageMinecartEntity instance, DamageSource source, World world, Entity entity) {
		EssentialUtils.breakVehicleStorage(instance, source, world, entity);
	}
}
