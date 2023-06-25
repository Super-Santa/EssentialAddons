package essentialaddons.mixins.essentialCarefulDrop;

//#if MC >= 11900
import essentialaddons.EssentialUtils;
import essentialaddons.utils.Subscription;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.PiglinBrain;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.vehicle.ChestBoatEntity;
import net.minecraft.entity.vehicle.VehicleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.ItemScatterer;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
//#endif

import net.minecraft.entity.vehicle.BoatEntity;
import org.spongepowered.asm.mixin.Mixin;

//#if MC >= 11900
@Mixin(ChestBoatEntity.class)
//#else
//$$@Mixin(BoatEntity.class)
//#endif
public abstract class ChestBoatEntityMixin
	//#if MC >= 11900
	extends BoatEntity implements VehicleInventory
	//#endif
{

	//#if MC >= 11900
	public ChestBoatEntityMixin(EntityType<? extends BoatEntity> entityType, World world) {
		super(entityType, world);
	}

	@Override
	public void onBroken(DamageSource source, World world, Entity vehicle) {
		if (world.getGameRules().getBoolean(GameRules.DO_ENTITY_DROPS)) {
			if (EssentialUtils.hasCareful(source.getAttacker(), Subscription.ESSENTIAL_CAREFUL_DROP)) {
				ServerPlayerEntity player = (ServerPlayerEntity) source.getAttacker();
				for (int i = 0; i < this.size(); ++i) {
					ItemStack stack = this.getStack(i);
					if (!EssentialUtils.placeItemInInventory(player, stack)) {
						ItemScatterer.spawn(EssentialUtils.getWorld(this), this.getX(), this.getY(), this.getZ(), stack);
					}
				}
				return;
			}
			if (!world.isClient && source.getSource() instanceof PlayerEntity player) {
				PiglinBrain.onGuardedBlockInteracted(player, true);
			}
		}
	}
	//#endif
}
