package essentialaddons.mixins;

import essentialaddons.EssentialAddonsSettings;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.projectile.thrown.PotionEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(PotionEntity.class)
public class PotionEntityMixin {
    @Redirect(method = "applySplashPotion", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;addStatusEffect(Lnet/minecraft/entity/effect/StatusEffectInstance;)Z"), require = 0)
    private boolean onAddStatusEffect(LivingEntity livingEntity, StatusEffectInstance statusEffectInstance) {
        if (EssentialAddonsSettings.combinePotionDuration) {
            StatusEffectInstance oldEffect = livingEntity.getStatusEffect(statusEffectInstance.getEffectType());
            int oldDur = 0;
            if (oldEffect != null && oldEffect.getAmplifier() == statusEffectInstance.getAmplifier()) {
                oldDur = oldEffect.getDuration();
            }
            StatusEffectInstance newEffect = new StatusEffectInstance(statusEffectInstance.getEffectType(), statusEffectInstance.getDuration() + oldDur, statusEffectInstance.getAmplifier());
            return livingEntity.addStatusEffect(newEffect);
        }
        return livingEntity.addStatusEffect(statusEffectInstance);
    }
}

