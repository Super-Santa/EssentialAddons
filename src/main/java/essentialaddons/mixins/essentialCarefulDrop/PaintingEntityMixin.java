package essentialaddons.mixins.essentialCarefulDrop;

import essentialaddons.EssentialUtils;
import essentialaddons.utils.Subscription;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.decoration.painting.PaintingEntity;
import net.minecraft.item.ItemConvertible;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(PaintingEntity.class)
public class PaintingEntityMixin {
	@Redirect(method = "onBreak", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/decoration/painting/PaintingEntity;dropItem(Lnet/minecraft/item/ItemConvertible;)Lnet/minecraft/entity/ItemEntity;"))
	private ItemEntity onDropItem(PaintingEntity instance, ItemConvertible itemConvertible, Entity entity) {
		if (EssentialUtils.tryCareful(entity, Subscription.ESSENTIAL_CAREFUL_DROP, itemConvertible.asItem().getDefaultStack())) {
			return null;
		}
		return instance.dropItem(itemConvertible);
	}
}
