package me.supersanta.essential_addons.mixins.feature.lag_spike;

import me.supersanta.essential_addons.feature.lag_spike.LagSpike;
import me.supersanta.essential_addons.feature.lag_spike.LagSpikes;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Level.class)
public class LevelMixin {
    @Inject(
        method = "tickBlockEntities()V",
        at = @At("HEAD")
    )
    private void preBlockTick(CallbackInfo ci) {
        LagSpikes.process(LagSpike.TickPhase.BlockTick, LagSpike.SubPhase.Pre);
    }

    @Inject(
        method = "tickBlockEntities()V",
        at = @At("RETURN")
    )
    private void postBlockTick(CallbackInfo ci) {
        LagSpikes.process(LagSpike.TickPhase.BlockTick, LagSpike.SubPhase.Post);
    }
}
