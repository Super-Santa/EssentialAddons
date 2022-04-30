package essentialaddons.mixins.essentialCarefulDrop;

import essentialaddons.EssentialSettings;
import essentialaddons.EssentialUtils;
import essentialaddons.utils.Subscription;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.vehicle.BoatEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BoatEntity.class)
public abstract class BoatEntityMixin extends Entity {
	public BoatEntityMixin(EntityType<?> type, World world) {
		super(type, world);
	}

	@Shadow
	public abstract Item asItem();

	@Redirect(method = "damage", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/vehicle/BoatEntity;dropItem(Lnet/minecraft/item/ItemConvertible;)Lnet/minecraft/entity/ItemEntity;"))
	private ItemEntity onDropItem(BoatEntity instance, ItemConvertible itemConvertible) {
		return null;
	}

	@Inject(method = "damage", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/vehicle/BoatEntity;dropItem(Lnet/minecraft/item/ItemConvertible;)Lnet/minecraft/entity/ItemEntity;", shift = At.Shift.BEFORE))
	private void onDropItem(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
		if (EssentialSettings.essentialCarefulDrop && source.getSource() instanceof ServerPlayerEntity player && (player.isInSneakingPose() || Subscription.ALWAYS_CAREFUL.hasPlayer(player))) {
			if (Subscription.ESSENTIAL_CAREFUL_DROP.hasPlayer(player) && EssentialUtils.placeItemInInventory(player, this.asItem().getDefaultStack())) {
				return;
			}
		}
		this.dropItem(this.asItem());
	}

}
