package me.supersanta.essential_addons.mixins.feature.cake_always_eat;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalBooleanRef;
import me.supersanta.essential_addons.EssentialSettings;
import net.minecraft.world.InteractionResult;
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
    private static boolean tryEat(
        boolean original,
        @Share("alwaysEat") LocalBooleanRef alwaysEat
    ) {
        if (!original && EssentialSettings.cakeAlwaysEat) {
            alwaysEat.set(true);
            return true;
        }
        return original;
    }

    @ModifyReturnValue(
        method = "eat",
        at = @At("RETURN")
    )
    private static InteractionResult replaceInteractionResult(
        InteractionResult original,
        @Share("alwaysEat") LocalBooleanRef alwaysEat
    ) {
        if (alwaysEat.get()) {
            return InteractionResult.SUCCESS_SERVER;
        }
        return original;
    }
}

