package me.supersanta.essential_addons.mixins.feature.shulkerception;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import me.supersanta.essential_addons.EssentialSettings;
import net.minecraft.world.inventory.ShulkerBoxSlot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ShulkerBoxSlot.class)
public class ShulkerBoxSlotMixin {
    @ModifyReturnValue(
        method = "mayPlace",
        at = @At("RETURN")
    )
    private boolean mayPlaceItemInShulker(boolean original) {
        return original || EssentialSettings.shulkerception;
    }
}
