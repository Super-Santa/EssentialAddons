package essentialaddons.mixins.essentialCarefulDrop;

import essentialaddons.EssentialUtils;
import essentialaddons.utils.Subscription;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.context.LootContextTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {
	public LivingEntityMixin(EntityType<?> type, World world) {
		super(type, world);
	}

	@Shadow
	public abstract Identifier getLootTable();

	@Shadow
	protected abstract LootContext.Builder getLootContextBuilder(boolean causedByPlayer, DamageSource source);

	@Inject(method = "dropLoot", at = @At("HEAD"), cancellable = true)
	private void onDropLoot(DamageSource source, boolean causedByPlayer, CallbackInfo ci) {
		if (EssentialUtils.hasCareful(source.getAttacker(), Subscription.ESSENTIAL_CAREFUL_DROP)) {
			ServerPlayerEntity player = (ServerPlayerEntity) source.getAttacker();
			Identifier identifier = this.getLootTable();
			LootTable lootTable = player.getServerWorld().getServer().getLootManager().getTable(identifier);
			LootContext.Builder builder = this.getLootContextBuilder(causedByPlayer, source);
			lootTable.generateLoot(builder.build(LootContextTypes.ENTITY), stack -> {
				if (!EssentialUtils.placeItemInInventory(player, stack)) {
					this.dropStack(stack);
				}
			});
			ci.cancel();
		}
	}
}
