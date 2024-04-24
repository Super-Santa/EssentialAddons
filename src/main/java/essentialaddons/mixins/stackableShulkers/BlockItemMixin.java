package essentialaddons.mixins.stackableShulkers;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import essentialaddons.EssentialSettings;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsage;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(BlockItem.class)
public class BlockItemMixin {
	@WrapWithCondition(
		method = "onItemEntityDestroyed",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/item/ItemUsage;spawnItemContents(Lnet/minecraft/entity/ItemEntity;Ljava/lang/Iterable;)V"
		)
	)
	private boolean onSpawnShulkerItems(
		ItemEntity entity,
		Iterable<ItemStack> contents
	) {
		if (!EssentialSettings.stackableShulkersWithItems) {
			return true;
		}
		ItemStack stack = entity.getStack();
		if (stack.getCount() == 1) {
			return true;
		}

		for (int i = 0; i < stack.getCount(); i++) {
			ItemUsage.spawnItemContents(entity, contents);
		}
		return false;
	}
}
