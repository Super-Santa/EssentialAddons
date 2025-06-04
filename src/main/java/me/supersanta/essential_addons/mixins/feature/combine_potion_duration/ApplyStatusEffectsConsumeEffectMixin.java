package me.supersanta.essential_addons.mixins.feature.combine_potion_duration;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import me.supersanta.essential_addons.EssentialSettings;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.consume_effects.ApplyStatusEffectsConsumeEffect;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ApplyStatusEffectsConsumeEffect.class)
public class ApplyStatusEffectsConsumeEffectMixin {
    @WrapOperation(
        method = "apply",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/entity/LivingEntity;addEffect(Lnet/minecraft/world/effect/MobEffectInstance;)Z"
        )
    )
    private boolean onAddEffect(
        LivingEntity entity,
        MobEffectInstance instance,
        Operation<Boolean> original
    ) {
        if (EssentialSettings.combinePotionDuration) {
            MobEffectInstance old = entity.getEffect(instance.getEffect());
            if (old != null && old.getAmplifier() == instance.getAmplifier()) {
                if (!old.isInfiniteDuration() && !instance.isInfiniteDuration()) {
                    float scale = old.getDuration() + instance.getDuration() / (float) instance.getDuration();
                    MobEffectInstance updated = instance.withScaledDuration(scale);
                    return original.call(entity, updated);
                }
            }
        }
        return original.call(entity, instance);
    }
}
