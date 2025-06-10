package me.supersanta.essential_addons.mixins.feature.lag_spike;

import me.supersanta.essential_addons.feature.lag_spike.LagSpike;
import me.supersanta.essential_addons.feature.lag_spike.LagSpikes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.chunk.LevelChunk;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.BooleanSupplier;

@SuppressWarnings("DiscouragedShift")
@Mixin(ServerLevel.class)
public class ServerLevelMixin {
    @Inject(
        method = "tickChunk",
        at = @At("HEAD")
    )
    private void preRandomTick(LevelChunk chunk, int randomTickSpeed, CallbackInfo ci) {
        LagSpikes.process(LagSpike.TickPhase.RandomTick, LagSpike.SubPhase.Pre);
    }

    @Inject(
        method = "tickChunk",
        at = @At("RETURN")
    )
    private void postRandomTick(LevelChunk chunk, int randomTickSpeed, CallbackInfo ci) {
        LagSpikes.process(LagSpike.TickPhase.RandomTick, LagSpike.SubPhase.Post);
    }

    @Inject(
        method = "tick",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/server/level/ServerLevel;runBlockEvents()V",
            shift = At.Shift.BEFORE
        )
    )
    private void preBlockEvents(BooleanSupplier hasTimeLeft, CallbackInfo ci) {
        LagSpikes.process(LagSpike.TickPhase.BlockEvent, LagSpike.SubPhase.Pre);
    }

    @Inject(
        method = "tick",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/server/level/ServerLevel;runBlockEvents()V",
            shift = At.Shift.AFTER
        )
    )
    private void postBlockEvents(BooleanSupplier hasTimeLeft, CallbackInfo ci) {
        LagSpikes.process(LagSpike.TickPhase.BlockEvent, LagSpike.SubPhase.Post);
    }

    @Inject(
        method = "tick",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/level/entity/EntityTickList;forEach(Ljava/util/function/Consumer;)V",
            shift = At.Shift.BEFORE
        )
    )
    private void preEntityTick(BooleanSupplier hasTimeLeft, CallbackInfo ci) {
        LagSpikes.process(LagSpike.TickPhase.EntityTick, LagSpike.SubPhase.Pre);
    }

    @Inject(
        method = "tick",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/level/entity/EntityTickList;forEach(Ljava/util/function/Consumer;)V",
            shift = At.Shift.AFTER
        )
    )
    private void postEntityTick(BooleanSupplier hasTimeLeft, CallbackInfo ci) {
        LagSpikes.process(LagSpike.TickPhase.EntityTick, LagSpike.SubPhase.Post);
    }
}
