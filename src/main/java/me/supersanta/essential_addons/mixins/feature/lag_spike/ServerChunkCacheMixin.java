package me.supersanta.essential_addons.mixins.feature.lag_spike;

import me.supersanta.essential_addons.feature.lag_spike.LagSpike;
import me.supersanta.essential_addons.feature.lag_spike.LagSpikes;
import net.minecraft.server.level.ServerChunkCache;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.BooleanSupplier;

@Mixin(ServerChunkCache.class)
@SuppressWarnings("DiscouragedShift")
public class ServerChunkCacheMixin {
    @Inject(
        method = "tick",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/level/TicketStorage;purgeStaleTickets()V",
            shift = At.Shift.BEFORE
        )
    )
    private void prePurgeStaleTickets(BooleanSupplier hasTimeLeft, boolean tickChunks, CallbackInfo ci) {
        LagSpikes.process(LagSpike.TickPhase.ChunkUnloading, LagSpike.SubPhase.Pre);
    }

    @Inject(
        method = "tick",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/level/TicketStorage;purgeStaleTickets()V",
            shift = At.Shift.AFTER
        )
    )
    private void postPurgeStaleTickets(BooleanSupplier hasTimeLeft, boolean tickChunks, CallbackInfo ci) {
        LagSpikes.process(LagSpike.TickPhase.ChunkUnloading, LagSpike.SubPhase.Post);
    }

    @Inject(
        method = "tick",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/server/level/ServerChunkCache;tickChunks()V",
            shift = At.Shift.BEFORE
        )
    )
    private void preMobSpawning(BooleanSupplier hasTimeLeft, boolean tickChunks, CallbackInfo ci) {
        LagSpikes.process(LagSpike.TickPhase.MobSpawning, LagSpike.SubPhase.Pre);
    }

    @Inject(
        method = "tick",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/server/level/ServerChunkCache;tickChunks()V",
            shift = At.Shift.AFTER
        )
    )
    private void postMobSpawning(BooleanSupplier hasTimeLeft, boolean tickChunks, CallbackInfo ci) {
        LagSpikes.process(LagSpike.TickPhase.MobSpawning, LagSpike.SubPhase.Post);
    }

    @Inject(
        method = "tick",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/server/level/ChunkMap;tick()V",
            shift = At.Shift.BEFORE
        )
    )
    private void prePlayerMovement(BooleanSupplier hasTimeLeft, boolean tickChunks, CallbackInfo ci) {
        LagSpikes.process(LagSpike.TickPhase.Player, LagSpike.SubPhase.Pre);
    }

    @Inject(
        method = "tick",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/server/level/ChunkMap;tick()V",
            shift = At.Shift.AFTER
        )
    )
    private void postPlayerMovement(BooleanSupplier hasTimeLeft, boolean tickChunks, CallbackInfo ci) {
        LagSpikes.process(LagSpike.TickPhase.Player, LagSpike.SubPhase.Post);
    }
}
