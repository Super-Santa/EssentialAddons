package essentialaddons.mixins.essentialCarefulDrop;

import essentialaddons.EssentialUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.vehicle.BoatEntity;
import net.minecraft.entity.vehicle.ChestBoatEntity;
import net.minecraft.entity.vehicle.VehicleInventory;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ChestBoatEntity.class)
public abstract class ChestBoatEntityMixin extends BoatEntity implements VehicleInventory {
	public ChestBoatEntityMixin(EntityType<? extends BoatEntity> entityType, World world) {
		super(entityType, world);
	}

	@Redirect(
		method = "killAndDropSelf",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/entity/vehicle/ChestBoatEntity;onBroken(Lnet/minecraft/entity/damage/DamageSource;Lnet/minecraft/world/World;Lnet/minecraft/entity/Entity;)V"
		)
	)
	private void onBreak(ChestBoatEntity instance, DamageSource source, World world, Entity entity) {
		EssentialUtils.breakVehicleStorage(instance, source, world, entity);
	}
}
