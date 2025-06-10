package me.supersanta.essential_addons.mixins.feature.minecart_boosting;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import me.supersanta.essential_addons.EssentialSettings;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(AbstractMinecart.class)
public class AbstractMinecartMixin {
    @ModifyExpressionValue(
        method = "pushOtherMinecart",
        at = @At(
            value = "CONSTANT",
            args = "doubleValue=0.800000011920929"
        )
    )
    private double minecartBoosting(double original) {
        if (EssentialSettings.minecartBoosting) {
            return -Double.MAX_VALUE;
        }
        return original;
    }
}
