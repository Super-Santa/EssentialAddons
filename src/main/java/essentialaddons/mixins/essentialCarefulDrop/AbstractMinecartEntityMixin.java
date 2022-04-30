package essentialaddons.mixins.essentialCarefulDrop;

import essentialaddons.EssentialSettings;
import essentialaddons.EssentialUtils;
import essentialaddons.utils.Subscription;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.vehicle.AbstractMinecartEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractMinecartEntity.class)
public abstract class AbstractMinecartEntityMixin extends Entity {
	public AbstractMinecartEntityMixin(EntityType<?> type, World world) {
		super(type, world);
	}

	@Inject(method = "dropItems", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;<init>(Lnet/minecraft/item/ItemConvertible;)V", shift = At.Shift.BEFORE), cancellable = true)
	private void onDropItems(DamageSource damageSource, CallbackInfo ci) {
		ItemStack itemStack = new ItemStack(Items.MINECART);
		if (this.hasCustomName()) {
			itemStack.setCustomName(this.getCustomName());
		}
		ci.cancel();
		if (EssentialSettings.essentialCarefulDrop && damageSource.getSource() instanceof ServerPlayerEntity player && (player.isInSneakingPose() || Subscription.ALWAYS_CAREFUL.hasPlayer(player))) {
			if (Subscription.ESSENTIAL_CAREFUL_DROP.hasPlayer(player) && EssentialUtils.placeItemInInventory(player, itemStack)) {
				return;
			}
		}
		this.dropStack(itemStack);
	}
}
