package essentialaddons.mixins.lagSpike;

import essentialaddons.feature.LagSpike;
import net.minecraft.server.world.ServerChunkManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.BooleanSupplier;

@Mixin(ServerChunkManager.class)
public class ServerChunkManagerMixin {
	@Inject(
        //#if MC >= 11800
        method = "tick(Ljava/util/function/BooleanSupplier;Z)V",
        //#else
        //$$method = "tick(Ljava/util/function/BooleanSupplier;)V",
        //#endif
        at = @At(value = "INVOKE", target = "Lnet/minecraft/server/world/ThreadedAnvilChunkStorage;tick(Ljava/util/function/BooleanSupplier;)V", shift = At.Shift.BEFORE)
    )
	protected void BeforeChunkUnload(BooleanSupplier shouldKeepTicking,
                                     //#if MC >= 11800
                                     boolean tickChunks,
                                     //#endif
                                     CallbackInfo ci) {
		LagSpike.processLagSpikes(LagSpike.TickPhase.CHUNK_UNLOADING, LagSpike.PrePostSubPhase.PRE);
	}

	@Inject(
        //#if MC >= 11800
        method = "tick(Ljava/util/function/BooleanSupplier;Z)V",
        //#else
        //$$method = "tick(Ljava/util/function/BooleanSupplier;)V",
        //#endif
        at = @At(value = "INVOKE", target = "Lnet/minecraft/server/world/ThreadedAnvilChunkStorage;tick(Ljava/util/function/BooleanSupplier;)V", shift = At.Shift.AFTER)
    )
	protected void AfterChunkUnload(BooleanSupplier shouldKeepTicking,
                                    //#if MC >= 11800
                                    boolean tickChunks,
                                    //#endif
                                    CallbackInfo ci) {
		LagSpike.processLagSpikes(LagSpike.TickPhase.CHUNK_UNLOADING, LagSpike.PrePostSubPhase.POST);
	}

	@Inject(
        //#if MC >= 11800
        method = "tick(Ljava/util/function/BooleanSupplier;Z)V",
        //#else
        //$$method = "tick(Ljava/util/function/BooleanSupplier;)V",
        //#endif
        at = @At(value = "INVOKE", target = "Lnet/minecraft/server/world/ServerChunkManager;tickChunks()V", shift = At.Shift.BEFORE)
    )
	protected void BeforeMobSpawning(BooleanSupplier shouldKeepTicking,
                                     //#if MC >= 11800
                                     boolean tickChunks,
                                     //#endif
                                     CallbackInfo ci) {
		LagSpike.processLagSpikes(LagSpike.TickPhase.MOB_SPAWNING, LagSpike.PrePostSubPhase.PRE);
	}

	@Inject(
        //#if MC >= 11800
        method = "tick(Ljava/util/function/BooleanSupplier;Z)V",
        //#else
        //$$method = "tick(Ljava/util/function/BooleanSupplier;)V",
        //#endif
        at = @At(value = "INVOKE", target = "Lnet/minecraft/server/world/ServerChunkManager;tickChunks()V", shift = At.Shift.AFTER)
    )
	protected void AfterMobSpawning(BooleanSupplier shouldKeepTicking,
                                    //#if MC >= 11800
                                    boolean tickChunks,
                                    //#endif
                                    CallbackInfo ci) {
		LagSpike.processLagSpikes(LagSpike.TickPhase.MOB_SPAWNING, LagSpike.PrePostSubPhase.POST);
	}

	@Inject(method = "tickChunks()V", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/world/ThreadedAnvilChunkStorage;tickEntityMovement()V", shift = At.Shift.BEFORE))
	protected void BeforePlayerMovement(CallbackInfo ci) {
		LagSpike.processLagSpikes(LagSpike.TickPhase.PLAYER, LagSpike.PrePostSubPhase.PRE);
	}

	@Inject(method = "tickChunks()V", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/world/ThreadedAnvilChunkStorage;tickEntityMovement()V", shift = At.Shift.AFTER))
	protected void AfterPlayerMovement(CallbackInfo ci) {
		LagSpike.processLagSpikes(LagSpike.TickPhase.PLAYER, LagSpike.PrePostSubPhase.POST);
	}
}
