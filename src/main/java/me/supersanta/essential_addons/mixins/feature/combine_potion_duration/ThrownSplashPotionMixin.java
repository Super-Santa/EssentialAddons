package me.supersanta.essential_addons.mixins.feature.combine_potion_duration;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import me.supersanta.essential_addons.EssentialSettings;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrownSplashPotion;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ThrownSplashPotion.class)
public class ThrownSplashPotionMixin {
    @WrapOperation(
        method = "onHitAsPotion",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/entity/LivingEntity;addEffect(Lnet/minecraft/world/effect/MobEffectInstance;Lnet/minecraft/world/entity/Entity;)Z"
        )
    )
    private boolean onAddEffect(
        LivingEntity entity,
        MobEffectInstance instance,
        Entity thrower,
        Operation<Boolean> original
    ) {
        if (EssentialSettings.combinePotionDuration) {
            MobEffectInstance old = entity.getEffect(instance.getEffect());
            if (old != null && old.getAmplifier() == instance.getAmplifier()) {
                if (!old.isInfiniteDuration() && !instance.isInfiniteDuration()) {
                    float scale = old.getDuration() + instance.getDuration() / (float) instance.getDuration();
                    MobEffectInstance updated = instance.withScaledDuration(scale);
                    return original.call(entity, updated, thrower);
                }
            }
        }
        return original.call(entity, instance, thrower);
    }
}
