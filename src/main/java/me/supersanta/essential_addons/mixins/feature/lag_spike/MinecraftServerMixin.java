package me.supersanta.essential_addons.mixins.feature.lag_spike;

import me.supersanta.essential_addons.feature.lag_spike.LagSpike;
import me.supersanta.essential_addons.feature.lag_spike.LagSpikes;
import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.BooleanSupplier;

@Mixin(MinecraftServer.class)
@SuppressWarnings("DiscouragedShift")
public class MinecraftServerMixin {
    @Inject(
        method = "tickServer",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/server/MinecraftServer;autoSave()V",
            shift = At.Shift.BEFORE
        )
    )
    private void preAutoSave(BooleanSupplier hasTimeLeft, CallbackInfo ci) {
        LagSpikes.process(LagSpike.TickPhase.Autosave, LagSpike.SubPhase.Pre);
    }

    @Inject(
        method = "tickServer",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/server/MinecraftServer;autoSave()V",
            shift = At.Shift.AFTER
        )
    )
    private void postAutoSave(BooleanSupplier hasTimeLeft, CallbackInfo ci) {
        LagSpikes.process(LagSpike.TickPhase.Autosave, LagSpike.SubPhase.Post);
    }

    @Inject(
        method = "tickServer",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/server/MinecraftServer;tickChildren(Ljava/util/function/BooleanSupplier;)V",
            shift = At.Shift.BEFORE
        )
    )
    private void preTickChildren(BooleanSupplier hasTimeLeft, CallbackInfo ci) {
        LagSpikes.process(LagSpike.TickPhase.Tick, LagSpike.SubPhase.Pre);
    }

    @Inject(
        method = "tickServer",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/server/MinecraftServer;tickChildren(Ljava/util/function/BooleanSupplier;)V",
            shift = At.Shift.BEFORE
        )
    )
    private void postTickChildren(BooleanSupplier hasTimeLeft, CallbackInfo ci) {
        LagSpikes.process(LagSpike.TickPhase.Tick, LagSpike.SubPhase.Post);
    }
}
