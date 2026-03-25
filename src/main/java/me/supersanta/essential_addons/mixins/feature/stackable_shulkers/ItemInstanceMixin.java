package me.supersanta.essential_addons.mixins.feature.stackable_shulkers;

import com.google.common.collect.Iterables;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import me.supersanta.essential_addons.EssentialSettings;
import me.supersanta.essential_addons.utils.EssentialUtilsKt;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.ItemInstance;
import net.minecraft.world.item.component.ItemContainerContents;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

// Mixin before Tweakeroo
@Mixin(value = ItemInstance.class, priority = 900)
public interface ItemInstanceMixin {
    @ModifyReturnValue(
        method = "getMaxStackSize",
        at = @At("RETURN")
    )
    private int onGetMaxStackSize(int original) {
        if (EssentialSettings.stackableShulkersInPlayerInventories) {
            ItemInstance instance = (ItemInstance) this;
            if (EssentialUtilsKt.isShulkerBox(instance.typeHolder().value())) {
                ItemContainerContents contents = instance.get(DataComponents.CONTAINER);
                if (contents == null || Iterables.isEmpty(contents.nonEmptyItems())) {
                    return 64;
                }
                if (EssentialSettings.stackableShulkersWithItems) {
                    return 64;
                }
            }
        }
        return original;
    }
}
