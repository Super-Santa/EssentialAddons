package essentialaddons.mixins.essentialCarefulDrop;

import essentialaddons.EssentialUtils;
import essentialaddons.utils.Subscription;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.loot.LootTable;
import org.spongepowered.asm.mixin.Shadow;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.loot.context.LootContextTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

//#if MC >= 12000
import net.minecraft.loot.context.LootContextParameterSet;
//#else
//$$import net.minecraft.loot.context.LootContext;
//#endif

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {
	public LivingEntityMixin(EntityType<?> type, World world) {
		super(type, world);
	}

	@Shadow
	public abstract Identifier getLootTable();

	//#if MC >= 12000
	@Shadow public abstract long getLootTableSeed();
	//#else
	//$$@Shadow protected abstract LootContext.Builder getLootContextBuilder(boolean causedByPlayer, DamageSource source);
	//#endif

	@Inject(method = "dropLoot", at = @At("HEAD"), cancellable = true)
	private void onDropLoot(DamageSource source, boolean causedByPlayer, CallbackInfo ci) {
		if (EssentialUtils.hasCareful(source.getAttacker(), Subscription.ESSENTIAL_CAREFUL_DROP)) {
			ServerPlayerEntity player = (ServerPlayerEntity) source.getAttacker();

			Identifier identifier = this.getLootTable();
			//#if MC >= 12000
			LootTable lootTable = EssentialUtils.getWorld(player).getServer().getLootManager().getLootTable(identifier);
			//#else
			//$$LootTable lootTable = EssentialUtils.getWorld(player).getServer().getLootManager().getTable(identifier);
			//#endif
			ServerWorld world = (ServerWorld) EssentialUtils.getWorld(this);

			//#if MC >= 12000
			LootContextParameterSet.Builder builder = new LootContextParameterSet.Builder(world)
				.add(LootContextParameters.THIS_ENTITY, this)
				.add(LootContextParameters.ORIGIN, this.getPos())
				.add(LootContextParameters.DAMAGE_SOURCE, this.getDamageSources().playerAttack(player))
				.addOptional(LootContextParameters.KILLER_ENTITY, player)
				.addOptional(LootContextParameters.DIRECT_KILLER_ENTITY, player)
				.add(LootContextParameters.LAST_DAMAGE_PLAYER, player)
				.luck(player.getLuck());
			LootContextParameterSet lootContextParameterSet = builder.build(LootContextTypes.ENTITY);
			//#else
			//$$LootContext.Builder builder = this.getLootContextBuilder(causedByPlayer, source);
			//$$LootContext lootContextParameterSet = builder.build(LootContextTypes.ENTITY);
			//#endif

			lootTable.generateLoot(
				lootContextParameterSet,
				//#if MC >=12000
				this.getLootTableSeed(),
				//#endif
				stack -> {
					if (!EssentialUtils.placeItemInInventory(player, stack)) {
						this.dropStack(stack);
					}
				}
			);

			ci.cancel();

		}
	}
}