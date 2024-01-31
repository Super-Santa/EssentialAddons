package essentialaddons.mixins.essentialCarefulDrop;

import essentialaddons.EssentialUtils;
import essentialaddons.utils.Subscription;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.vehicle.VehicleEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(VehicleEntity.class)
public abstract class VehicleEntityMixin extends Entity {
	public VehicleEntityMixin(EntityType<?> type, World world) {
		super(type, world);
	}

	@Redirect(
		method = "killAndDropSelf",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/entity/vehicle/VehicleEntity;killAndDropItem(Lnet/minecraft/item/Item;)V"
		)
	)
	private void onKillAndDropSelf(VehicleEntity instance, Item selfAsItem, DamageSource source) {
		boolean drops = this.getWorld().getGameRules().getBoolean(GameRules.DO_ENTITY_DROPS);
		if (drops) {
			Entity attacker = source.getAttacker();
			ItemStack stack = selfAsItem.getDefaultStack();
			if (EssentialUtils.tryCareful(attacker, Subscription.ESSENTIAL_CAREFUL_DROP, stack)) {
				this.kill();
				return;
			}
		}
		instance.killAndDropItem(selfAsItem);
	}
}
