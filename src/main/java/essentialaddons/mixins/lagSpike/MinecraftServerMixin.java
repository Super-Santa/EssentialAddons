package essentialaddons.mixins.lagSpike;

import essentialaddons.feature.LagSpike;
import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.BooleanSupplier;

@Mixin(MinecraftServer.class)
public class MinecraftServerMixin {

    @Inject(method= "tick(Ljava/util/function/BooleanSupplier;)V",at=@At(value="INVOKE",target="Lnet/minecraft/server/MinecraftServer;save(ZZZ)Z",shift=At.Shift.BEFORE))
    protected void BeforeAutoSave(BooleanSupplier shouldKeepTicking, CallbackInfo ci) {
        LagSpike.processLagSpikes(LagSpike.TickPhase.AUTOSAVE, LagSpike.PrePostSubPhase.PRE);
    }

    @Inject(method= "tick(Ljava/util/function/BooleanSupplier;)V",at=@At(value="INVOKE",target="Lnet/minecraft/server/MinecraftServer;save(ZZZ)Z",shift=At.Shift.AFTER))
    protected void AfterAutoSave(BooleanSupplier shouldKeepTicking, CallbackInfo ci) {
        LagSpike.processLagSpikes(LagSpike.TickPhase.AUTOSAVE, LagSpike.PrePostSubPhase.POST);
    }

    @Inject(method= "tickWorlds(Ljava/util/function/BooleanSupplier;)V",at=@At(value="INVOKE",target="Lnet/minecraft/server/world/ServerWorld;tick(Ljava/util/function/BooleanSupplier;)V",shift=At.Shift.BEFORE))
    protected void BeforeServerTick(BooleanSupplier shouldKeepTicking, CallbackInfo ci) {
        LagSpike.processLagSpikes(LagSpike.TickPhase.TICK, LagSpike.PrePostSubPhase.PRE);
    }

    @Inject(method= "tickWorlds(Ljava/util/function/BooleanSupplier;)V",at=@At(value="INVOKE",target="Lnet/minecraft/server/world/ServerWorld;tick(Ljava/util/function/BooleanSupplier;)V",shift=At.Shift.AFTER))
    protected void AfterServerTick(BooleanSupplier shouldKeepTicking, CallbackInfo ci) {
        LagSpike.processLagSpikes(LagSpike.TickPhase.TICK, LagSpike.PrePostSubPhase.POST);
    }
}
