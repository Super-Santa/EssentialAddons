package me.supersanta.essential_addons.mixins.feature.combine_potion_duration;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import me.supersanta.essential_addons.EssentialSettings;
import me.supersanta.essential_addons.feature.combine_potion_duration.CombinePotionDuration;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.alchemy.PotionContents;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(PotionContents.class)
public class PotionContentsMixin {
    @WrapOperation(
        method = "lambda$applyToLivingEntity$0",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/entity/LivingEntity;addEffect(Lnet/minecraft/world/effect/MobEffectInstance;)Z"
        )
    )
    private static boolean onAddEffect(
        LivingEntity entity,
        MobEffectInstance instance,
        Operation<Boolean> original
    ) {
        if (EssentialSettings.combinePotionDuration) {
            instance = CombinePotionDuration.tryCombineEffects(entity, instance);
        }
        return original.call(entity, instance);
    }
}
