package essentialaddons.mixins.essentialCarefulDrop;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import essentialaddons.EssentialUtils;
import essentialaddons.utils.Subscription;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.context.LootContextParameterSet;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.function.Consumer;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {
	public LivingEntityMixin(EntityType<?> type, World world) {
		super(type, world);
	}

	@WrapOperation(
		method = "dropLoot",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/loot/LootTable;generateLoot(Lnet/minecraft/loot/context/LootContextParameterSet;JLjava/util/function/Consumer;)V"
		)
	)
	private void applyCarefulDrop(
		LootTable instance,
		LootContextParameterSet parameters,
		long seed,
		Consumer<ItemStack> original,
		Operation<Void> operation,
		DamageSource source
	) {
		Consumer<ItemStack> consumer;
		if (EssentialUtils.hasCareful(source.getAttacker(), Subscription.ESSENTIAL_CAREFUL_DROP)) {
			ServerPlayerEntity player = (ServerPlayerEntity) source.getAttacker();
			consumer = stack -> {
				if (!EssentialUtils.placeItemInInventory(player, stack)) {
					original.accept(stack);
				}
			};
		} else {
			consumer = original;
		}
		operation.call(instance, parameters, seed, consumer);
	}
}