package essentialaddons.mixins.spectatorPotionNoCountdown;

import essentialaddons.EssentialAddonsSettings;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {

    @SuppressWarnings("ConstantConditions")
    @Inject(method = "tickStatusEffects", at = @At(value = "HEAD"), cancellable = true)
    private void checkPlayerSpectator(CallbackInfo ci) {
        if (EssentialAddonsSettings.spectatorPotionNoCountdown && (Object) this instanceof PlayerEntity && ((PlayerEntity) (Object) this).isSpectator()) {
            ci.cancel();
        }
    }
}