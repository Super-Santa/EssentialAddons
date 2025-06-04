package me.supersanta.essential_addons.mixins.feature.cake_always_eat;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import me.supersanta.essential_addons.EssentialSettings;
import net.minecraft.world.level.block.CakeBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(CakeBlock.class)
public class CakeBlockMixin {
    @ModifyExpressionValue(
        method = "eat",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/entity/player/Player;canEat(Z)Z"
        )
    )
    private static boolean tryEat(boolean original) {
        return original || EssentialSettings.cakeAlwaysEat;
    }
}

