package me.supersanta.essential_addons.feature.combine_potion_duration

import net.minecraft.world.effect.MobEffectInstance
import net.minecraft.world.entity.LivingEntity

object CombinePotionDuration {
    @JvmStatic
    fun tryCombineEffects(entity: LivingEntity, new: MobEffectInstance): MobEffectInstance {
        return this.tryCombineEffects(entity.getEffect(new.effect), new)
    }

    @JvmStatic
    fun tryCombineEffects(old: MobEffectInstance?, new: MobEffectInstance): MobEffectInstance {
        if (old == null || old.amplifier != new.amplifier) {
            return new
        }
        if (old.isInfiniteDuration || new.isInfiniteDuration) {
            return new
        }

        val scale = (old.duration + new.duration) / new.duration.toFloat()
        return new.withScaledDuration(scale)
    }
}