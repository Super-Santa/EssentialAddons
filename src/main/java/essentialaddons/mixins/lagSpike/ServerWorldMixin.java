package essentialaddons.mixins.lagSpike;

import essentialaddons.feature.LagSpike;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.chunk.WorldChunk;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.BooleanSupplier;

@Mixin(ServerWorld.class)
public class ServerWorldMixin {

	@Inject(method = "tickChunk(Lnet/minecraft/world/chunk/WorldChunk;I)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/chunk/WorldChunk;getSectionArray()[Lnet/minecraft/world/chunk/ChunkSection;", shift = At.Shift.BEFORE))
	protected void BeforeServerTick(WorldChunk chunk, int randomTickSpeed, CallbackInfo ci) {
		LagSpike.processLagSpikes(LagSpike.TickPhase.RANDOM_TICK, LagSpike.PrePostSubPhase.PRE);
	}

	@Inject(method = "tickChunk(Lnet/minecraft/world/chunk/WorldChunk;I)V", at = @At(value = "RETURN"))
	protected void AfterServerTick(WorldChunk chunk, int randomTickSpeed, CallbackInfo ci) {
		LagSpike.processLagSpikes(LagSpike.TickPhase.RANDOM_TICK, LagSpike.PrePostSubPhase.POST);
	}

	@Inject(method = "tick(Ljava/util/function/BooleanSupplier;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/world/ServerWorld;processSyncedBlockEvents()V", shift = At.Shift.BEFORE))
	protected void BeforeBlockEvents(BooleanSupplier shouldKeepTicking, CallbackInfo ci) {
		LagSpike.processLagSpikes(LagSpike.TickPhase.BLOCK_EVENT, LagSpike.PrePostSubPhase.PRE);
	}

	@Inject(method = "tick(Ljava/util/function/BooleanSupplier;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/world/ServerWorld;processSyncedBlockEvents()V", shift = At.Shift.AFTER))
	protected void AfterBlockEvents(BooleanSupplier shouldKeepTicking, CallbackInfo ci) {
		LagSpike.processLagSpikes(LagSpike.TickPhase.BLOCK_EVENT, LagSpike.PrePostSubPhase.POST);
	}

	@Inject(method = "tick(Ljava/util/function/BooleanSupplier;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/world/ServerWorld;getForcedChunks()Lit/unimi/dsi/fastutil/longs/LongSet;", shift = At.Shift.BEFORE))
	protected void BeforeEntities(BooleanSupplier shouldKeepTicking, CallbackInfo ci) {
		LagSpike.processLagSpikes(LagSpike.TickPhase.ENTITY, LagSpike.PrePostSubPhase.PRE);
	}

	@Inject(method = "tick(Ljava/util/function/BooleanSupplier;)V", at = @At(value = "RETURN"))
	protected void AfterEntities(BooleanSupplier shouldKeepTicking, CallbackInfo ci) {
		LagSpike.processLagSpikes(LagSpike.TickPhase.ENTITY, LagSpike.PrePostSubPhase.POST);
	}

}
