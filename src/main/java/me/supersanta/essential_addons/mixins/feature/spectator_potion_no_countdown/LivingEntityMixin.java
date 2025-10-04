package me.supersanta.essential_addons.mixins.feature.spectator_potion_no_countdown;

import me.supersanta.essential_addons.EssentialSettings;
import net.minecraft.network.protocol.game.ClientboundUpdateMobEffectPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static me.supersanta.essential_addons.utils.EssentialUtilsKt.has;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {
    @Inject(
        method = "tickEffects",
        at = @At("HEAD"),
        cancellable = true
    )
    private void onTickEffects(CallbackInfo ci) {
        //noinspection ConstantValue
        if ((Object) this instanceof ServerPlayer player && player.isSpectator()) {
            boolean shouldSkipTickingEffects = EssentialSettings.spectatorPotionNoCountdown ||
                has(player.createCommandSourceStack(), EssentialSettings.commandCameraMode, "command.cs");
            if (shouldSkipTickingEffects) {
                if (player.level().getServer().getTickCount() % 20 == 0) {
                    for (MobEffectInstance effect : player.getActiveEffects()) {
                        player.connection.send(new ClientboundUpdateMobEffectPacket(player.getId(), effect, false));
                    }
                }
                ci.cancel();
            }
        }
    }
}
