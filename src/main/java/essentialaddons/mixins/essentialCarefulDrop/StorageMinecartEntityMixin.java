package essentialaddons.mixins.essentialCarefulDrop;

import essentialaddons.EssentialUtils;
import essentialaddons.utils.Subscription;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.vehicle.StorageMinecartEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(StorageMinecartEntity.class)
public abstract class StorageMinecartEntityMixin extends Entity {
	@Shadow
	private DefaultedList<ItemStack> inventory;

	public StorageMinecartEntityMixin(EntityType<?> type, World world) {
		super(type, world);
	}

	@Inject(method = "dropItems", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/ItemScatterer;spawn(Lnet/minecraft/world/World;Lnet/minecraft/entity/Entity;Lnet/minecraft/inventory/Inventory;)V", shift = At.Shift.BEFORE))
	private void onDropItems(DamageSource damageSource, CallbackInfo ci) {
		if (EssentialUtils.hasCareful(damageSource.getAttacker(), Subscription.ESSENTIAL_CAREFUL_DROP)) {
			ServerPlayerEntity player = (ServerPlayerEntity) damageSource.getAttacker();
			for (ItemStack stack : this.inventory) {
				if (!EssentialUtils.placeItemInInventory(player, stack)) {
					ItemScatterer.spawn(this.world, this.getX(), this.getY(), this.getZ(), stack);
				}
			}
			return;
		}
		ItemScatterer.spawn(this.world, this, (Inventory) this);
	}

	@Redirect(method = "dropItems", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/ItemScatterer;spawn(Lnet/minecraft/world/World;Lnet/minecraft/entity/Entity;Lnet/minecraft/inventory/Inventory;)V"))
	private void onDropItems(World world, Entity entity, Inventory inventory) {
		// Do nothing
	}
}
