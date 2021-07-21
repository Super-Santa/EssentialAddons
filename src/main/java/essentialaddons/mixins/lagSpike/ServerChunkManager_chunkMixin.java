package essentialaddons.mixins.lagSpike;

import essentialaddons.helpers.LagSpikeHelper;
import net.minecraft.server.world.ServerChunkManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.BooleanSupplier;

@Mixin(ServerChunkManager.class)
public class ServerChunkManager_chunkMixin {

    @Inject(method= "tick(Ljava/util/function/BooleanSupplier;)V",at=@At(value="INVOKE",target="Lnet/minecraft/server/world/ThreadedAnvilChunkStorage;tick(Ljava/util/function/BooleanSupplier;)V",shift=At.Shift.BEFORE))
    protected void BeforeChunkUnload(BooleanSupplier shouldKeepTicking, CallbackInfo ci) {
        LagSpikeHelper.processLagSpikes(LagSpikeHelper.TickPhase.CHUNK_UNLOADING, LagSpikeHelper.PrePostSubPhase.PRE);
    }

    @Inject(method= "tick(Ljava/util/function/BooleanSupplier;)V",at=@At(value="INVOKE",target="Lnet/minecraft/server/world/ThreadedAnvilChunkStorage;tick(Ljava/util/function/BooleanSupplier;)V",shift=At.Shift.AFTER))
    protected void AfterChunkUnload(BooleanSupplier shouldKeepTicking, CallbackInfo ci) {
        LagSpikeHelper.processLagSpikes(LagSpikeHelper.TickPhase.CHUNK_UNLOADING, LagSpikeHelper.PrePostSubPhase.POST);
    }

    @Inject(method= "tick(Ljava/util/function/BooleanSupplier;)V",at=@At(value="INVOKE",target="Lnet/minecraft/server/world/ServerChunkManager;tickChunks()V",shift=At.Shift.BEFORE))
    protected void BeforeMobSpawning(BooleanSupplier shouldKeepTicking, CallbackInfo ci) {
        LagSpikeHelper.processLagSpikes(LagSpikeHelper.TickPhase.MOB_SPAWNING, LagSpikeHelper.PrePostSubPhase.PRE);
    }

    @Inject(method= "tick(Ljava/util/function/BooleanSupplier;)V",at=@At(value="INVOKE",target="Lnet/minecraft/server/world/ServerChunkManager;tickChunks()V",shift=At.Shift.AFTER))
    protected void AfterMobSpawning(BooleanSupplier shouldKeepTicking, CallbackInfo ci) {
        LagSpikeHelper.processLagSpikes(LagSpikeHelper.TickPhase.MOB_SPAWNING, LagSpikeHelper.PrePostSubPhase.POST);
    }

    @Inject(method= "tickChunks()V",at=@At(value="INVOKE",target="Lnet/minecraft/server/world/ThreadedAnvilChunkStorage;tickPlayerMovement()V",shift=At.Shift.BEFORE))
    protected void BeforePlayerMovement(CallbackInfo ci) {
        LagSpikeHelper.processLagSpikes(LagSpikeHelper.TickPhase.PLAYER, LagSpikeHelper.PrePostSubPhase.PRE);
    }

    @Inject(method= "tickChunks()V",at=@At(value="INVOKE",target="Lnet/minecraft/server/world/ThreadedAnvilChunkStorage;tickPlayerMovement()V",shift=At.Shift.AFTER))
    protected void AfterPlayerMovement(CallbackInfo ci) {
        LagSpikeHelper.processLagSpikes(LagSpikeHelper.TickPhase.PLAYER, LagSpikeHelper.PrePostSubPhase.POST);
    }
}
