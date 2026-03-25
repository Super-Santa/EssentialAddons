package me.supersanta.essential_addons.mixins.feature.stackable_shulkers;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import com.llamalad7.mixinextras.sugar.Local;
import me.supersanta.essential_addons.EssentialSettings;
import me.supersanta.essential_addons.utils.EssentialUtilsKt;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.component.ItemContainerContents;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.stream.Stream;

@Mixin(BlockItem.class)
public class BlockItemMixin {
    @WrapWithCondition(
        method = "onDestroyed",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/item/ItemUtils;onContainerDestroyed(Lnet/minecraft/world/entity/item/ItemEntity;Ljava/util/stream/Stream;)V"
        )
    )
    private boolean onDestroyItemEntity(
        ItemEntity entity,
        Stream<ItemStack> items,
        @Local(name = "container") ItemContainerContents contents
    ) {
        if (!EssentialSettings.stackableShulkersWithItems) {
            return true;
        }
        ItemStack stack = entity.getItem();
        if (stack.getCount() == 1 || !EssentialUtilsKt.isShulkerBox(stack.getItem())) {
            return true;
        }

        for (int i = 0; i < stack.getCount(); i++) {
            ItemUtils.onContainerDestroyed(entity, contents.nonEmptyItemCopyStream());
        }
        return false;
    }
}
