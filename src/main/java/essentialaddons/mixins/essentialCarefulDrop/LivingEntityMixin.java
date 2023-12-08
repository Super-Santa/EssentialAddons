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

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {
	public LivingEntityMixin(EntityType<?> type, World world) {
		super(type, world);
	}

	@Shadow
	public abstract Identifier getLootTable();

	@Shadow public abstract long getLootTableSeed();

	@Inject(method = "dropLoot", at = @At("HEAD"), cancellable = true)
	private void onDropLoot(DamageSource source, boolean causedByPlayer, CallbackInfo ci) {
		if (EssentialUtils.hasCareful(source.getAttacker(), Subscription.ESSENTIAL_CAREFUL_DROP)) {
			ServerPlayerEntity player = (ServerPlayerEntity) source.getAttacker();

			Identifier identifier = this.getLootTable();
			LootTable lootTable = EssentialUtils.getWorld(player).getServer().getLootManager().getLootTable(identifier);
			ServerWorld world = (ServerWorld) EssentialUtils.getWorld(this);

			LootContextParameterSet.Builder builder = new LootContextParameterSet.Builder(world)
				.add(LootContextParameters.THIS_ENTITY, this)
				.add(LootContextParameters.ORIGIN, this.getPos())
				.add(LootContextParameters.DAMAGE_SOURCE, this.getDamageSources().playerAttack(player))
				.addOptional(LootContextParameters.KILLER_ENTITY, player)
				.addOptional(LootContextParameters.DIRECT_KILLER_ENTITY, player)
				.add(LootContextParameters.LAST_DAMAGE_PLAYER, player)
				.luck(player.getLuck());
			LootContextParameterSet lootContextParameterSet = builder.build(LootContextTypes.ENTITY);

			lootTable.generateLoot(
				lootContextParameterSet,
				this.getLootTableSeed(),
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