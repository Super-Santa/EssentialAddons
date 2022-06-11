package essentialaddons.mixins.essentialCarefulDrop;

import essentialaddons.EssentialUtils;
import essentialaddons.utils.Subscription;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.PiglinBrain;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.vehicle.AbstractMinecartEntity;
import net.minecraft.entity.vehicle.StorageMinecartEntity;
import net.minecraft.entity.vehicle.VehicleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(StorageMinecartEntity.class)
public abstract class StorageMinecartEntityMixin extends AbstractMinecartEntity implements VehicleInventory {

	public StorageMinecartEntityMixin(EntityType<?> type, World world) {
		super(type, world);
	}

	@Redirect(method = "dropItems", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/vehicle/AbstractMinecartEntity;dropItems(Lnet/minecraft/entity/damage/DamageSource;)V"))
	private void onSuperDropItems(AbstractMinecartEntity instance, DamageSource damageSource) {
		// Do nothing, we do not want to kill the mine cart yet
	}

	@Override
	public void onBroken(DamageSource source, World world, Entity vehicle) {
		if (world.getGameRules().getBoolean(GameRules.DO_ENTITY_DROPS)) {
			if (EssentialUtils.hasCareful(source.getAttacker(), Subscription.ESSENTIAL_CAREFUL_DROP)) {
				ServerPlayerEntity player = (ServerPlayerEntity) source.getAttacker();
				for (int i = 0; i < this.size(); ++i) {
					ItemStack stack = this.getStack(i);
					if (!EssentialUtils.placeItemInInventory(player, stack)) {
						ItemScatterer.spawn(this.world, this.getX(), this.getY(), this.getZ(), stack);
					}
				}
			}
			if (!world.isClient && source.getSource() instanceof PlayerEntity player) {
				PiglinBrain.onGuardedBlockInteracted(player, true);
			}
		}
		super.dropItems(source);
	}
}
