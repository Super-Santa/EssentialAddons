package essentialaddons.mixins.lagSpike;

import essentialaddons.helpers.LagSpikeHelper;
import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.BooleanSupplier;

@Mixin(MinecraftServer.class)
public class MinecraftServer_autoSaveMixin {

    @Inject(method= "tick(Ljava/util/function/BooleanSupplier;)V",at=@At(value="INVOKE",target="Lnet/minecraft/server/PlayerManager;saveAllPlayerData()V",shift=At.Shift.BEFORE))
    protected void BeforeAutoSave(BooleanSupplier shouldKeepTicking, CallbackInfo ci) {
        LagSpikeHelper.processLagSpikes(LagSpikeHelper.TickPhase.AUTOSAVE, LagSpikeHelper.PrePostSubPhase.PRE);
    }

    @Inject(method= "tick(Ljava/util/function/BooleanSupplier;)V",at=@At(value="INVOKE",target="Lnet/minecraft/server/PlayerManager;saveAllPlayerData()V",shift=At.Shift.AFTER))
    protected void AfterAutoSave(BooleanSupplier shouldKeepTicking, CallbackInfo ci) {
        LagSpikeHelper.processLagSpikes(LagSpikeHelper.TickPhase.AUTOSAVE, LagSpikeHelper.PrePostSubPhase.POST);
    }

    @Inject(method= "tickWorlds(Ljava/util/function/BooleanSupplier;)V",at=@At(value="INVOKE",target="Lnet/minecraft/server/world/ServerWorld;tick(Ljava/util/function/BooleanSupplier;)V",shift=At.Shift.BEFORE))
    protected void BeforeServerTick(BooleanSupplier shouldKeepTicking, CallbackInfo ci) {
        LagSpikeHelper.processLagSpikes(LagSpikeHelper.TickPhase.TICK, LagSpikeHelper.PrePostSubPhase.PRE);
    }

    @Inject(method= "tickWorlds(Ljava/util/function/BooleanSupplier;)V",at=@At(value="INVOKE",target="Lnet/minecraft/server/world/ServerWorld;tick(Ljava/util/function/BooleanSupplier;)V",shift=At.Shift.AFTER))
    protected void AfterServerTick(BooleanSupplier shouldKeepTicking, CallbackInfo ci) {
        LagSpikeHelper.processLagSpikes(LagSpikeHelper.TickPhase.TICK, LagSpikeHelper.PrePostSubPhase.POST);
    }
}
