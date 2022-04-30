package essentialaddons.mixins.essentialCarefulDrop;

import essentialaddons.EssentialSettings;
import essentialaddons.EssentialUtils;
import essentialaddons.utils.Subscription;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.PiglinBrain;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.vehicle.StorageMinecartEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(StorageMinecartEntity.class)
public abstract class StorageMinecartEntityMixin extends Entity {
	@Shadow
	private DefaultedList<ItemStack> inventory;

	public StorageMinecartEntityMixin(EntityType<?> type, World world) {
		super(type, world);
	}

	@Inject(method = "dropItems", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/ItemScatterer;spawn(Lnet/minecraft/world/World;Lnet/minecraft/entity/Entity;Lnet/minecraft/inventory/Inventory;)V", shift = At.Shift.BEFORE), cancellable = true)
	private void onDropItems(DamageSource damageSource, CallbackInfo ci) {
		ci.cancel();
		if (EssentialSettings.essentialCarefulDrop && damageSource.getSource() instanceof ServerPlayerEntity player && (player.isInSneakingPose() || Subscription.ALWAYS_CAREFUL.hasPlayer(player)) && Subscription.ESSENTIAL_CAREFUL_DROP.hasPlayer(player)) {
			for (ItemStack stack : this.inventory) {
				if (!EssentialUtils.placeItemInInventory(player, stack)) {
					ItemScatterer.spawn(this.world, this.getX(), this.getY(), this.getZ(), stack);
				}
			}
		}
		else {
			ItemScatterer.spawn(this.world, this, (Inventory) this);
		}
		if (!this.world.isClient) {
			Entity entity = damageSource.getSource();
			if (entity != null && entity.getType() == EntityType.PLAYER) {
				PiglinBrain.onGuardedBlockInteracted((PlayerEntity)entity, true);
			}
		}
	}
}
