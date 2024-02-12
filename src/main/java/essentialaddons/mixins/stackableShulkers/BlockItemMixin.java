package essentialaddons.mixins.stackableShulkers;

import com.llamalad7.mixinextras.injector.WrapWithCondition;
import essentialaddons.EssentialSettings;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsage;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.List;
import java.util.stream.Stream;

@Mixin(BlockItem.class)
public class BlockItemMixin {
	@WrapWithCondition(
		method = "onItemEntityDestroyed",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/item/ItemUsage;spawnItemContents(Lnet/minecraft/entity/ItemEntity;Ljava/util/stream/Stream;)V"
		)
	)
	private boolean onSpawnShulkerItems(
		ItemEntity entity,
		Stream<ItemStack> contents
	) {
		if (!EssentialSettings.stackableShulkersWithItems) {
			return true;
		}
		ItemStack stack = entity.getStack();
		if (stack.getCount() == 1) {
			return true;
		}

		List<ItemStack> items = contents.toList();
		for (int i = 0; i < stack.getCount(); i++) {
			ItemUsage.spawnItemContents(entity, items.stream().map(ItemStack::copy));
		}
		return false;
	}
}
