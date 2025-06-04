package me.supersanta.essential_addons.mixins.feature.shulkerception;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import me.supersanta.essential_addons.EssentialSettings;
import net.minecraft.world.level.block.entity.ShulkerBoxBlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ShulkerBoxBlockEntity.class)
public class ShulkerBoxBlockEntityMixin {
    @ModifyReturnValue(
        method = "canPlaceItemThroughFace",
        at = @At("RETURN")
    )
    private boolean canPlaceItemThroughFace(boolean original) {
        return original || EssentialSettings.shulkerception;
    }
}
