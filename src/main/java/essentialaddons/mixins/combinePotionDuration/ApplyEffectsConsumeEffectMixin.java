package essentialaddons.mixins.combinePotionDuration;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import essentialaddons.EssentialSettings;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.item.consume.ApplyEffectsConsumeEffect;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ApplyEffectsConsumeEffect.class)
public class ApplyEffectsConsumeEffectMixin {
    @WrapOperation(
        method = "onConsume",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/entity/LivingEntity;addStatusEffect(Lnet/minecraft/entity/effect/StatusEffectInstance;)Z"
        )
    )
    private boolean onAddStatusEffect(
        LivingEntity instance,
        StatusEffectInstance effect,
        Operation<Boolean> original
    ) {
        if (EssentialSettings.combinePotionDuration) {
            StatusEffectInstance oldEffect = instance.getStatusEffect(effect.getEffectType());
            int oldDur = 0;
            if (oldEffect != null && oldEffect.getAmplifier() == effect.getAmplifier()) {
                oldDur = oldEffect.getDuration();
            }
            StatusEffectInstance newEffect = new StatusEffectInstance(effect.getEffectType(), effect.getDuration() + oldDur, effect.getAmplifier());
            return original.call(instance, newEffect);
        }
        return original.call(instance, effect);
    }
}
