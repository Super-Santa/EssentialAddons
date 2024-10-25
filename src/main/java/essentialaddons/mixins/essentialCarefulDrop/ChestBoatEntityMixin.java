package essentialaddons.mixins.essentialCarefulDrop;

import essentialaddons.EssentialUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.vehicle.AbstractChestBoatEntity;
import net.minecraft.entity.vehicle.VehicleInventory;
import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(AbstractChestBoatEntity.class)
public abstract class ChestBoatEntityMixin implements VehicleInventory {
	@Redirect(
		method = "killAndDropSelf",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/entity/vehicle/AbstractChestBoatEntity;onBroken(Lnet/minecraft/entity/damage/DamageSource;Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/entity/Entity;)V"
		)
	)
	private void onBreak(AbstractChestBoatEntity instance, DamageSource source, ServerWorld world, Entity entity) {
		EssentialUtils.breakVehicleStorage(instance, source, world, entity);
	}
}
