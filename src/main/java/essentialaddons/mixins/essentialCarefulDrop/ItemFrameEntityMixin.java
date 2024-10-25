package essentialaddons.mixins.essentialCarefulDrop;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import com.llamalad7.mixinextras.sugar.Local;
import essentialaddons.EssentialUtils;
import essentialaddons.utils.Subscription;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.decoration.AbstractDecorationEntity;
import net.minecraft.entity.decoration.ItemFrameEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ItemFrameEntity.class)
public abstract class ItemFrameEntityMixin extends AbstractDecorationEntity {
	protected ItemFrameEntityMixin(EntityType<? extends AbstractDecorationEntity> entityType, World world) {
		super(entityType, world);
	}

	@WrapWithCondition(
		method = "dropHeldStack",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/entity/decoration/ItemFrameEntity;dropStack(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/item/ItemStack;)Lnet/minecraft/entity/ItemEntity;"
		)
	)
	private boolean onDropStack0(
		ItemFrameEntity instance,
		ServerWorld world,
		ItemStack itemStack,
		@Local(argsOnly = true) @Nullable Entity entity
	) {
		if (!EssentialUtils.tryCareful(entity, Subscription.ESSENTIAL_CAREFUL_DROP, itemStack)) {
			this.dropStack(world, itemStack);
			return false;
		}
		return true;
	}
}
