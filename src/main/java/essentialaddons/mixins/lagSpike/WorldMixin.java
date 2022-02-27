package essentialaddons.mixins.lagSpike;

import essentialaddons.feature.LagSpike;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.function.Predicate;

@Mixin(World.class)
public class WorldMixin {

    @Inject(method= "tickBlockEntities()V",at=@At(value="HEAD"))
    protected void BeforeTileTicks(CallbackInfo ci) {
        LagSpike.processLagSpikes(LagSpike.TickPhase.TILE_TICK, LagSpike.PrePostSubPhase.PRE);
    }

    @Inject(method= "tickBlockEntities()V",at=@At(value="RETURN"))
    protected void AfterTileTicks(CallbackInfo ci) {
        LagSpike.processLagSpikes(LagSpike.TickPhase.TILE_TICK, LagSpike.PrePostSubPhase.POST);
    }

    @Inject(method= "tickBlockEntities()V",at=@At(value="HEAD"))
    protected void BeforeTileEntities(CallbackInfo ci) {
        LagSpike.processLagSpikes(LagSpike.TickPhase.TILE_ENTITY, LagSpike.PrePostSubPhase.PRE);
    }

    @Inject(method= "tickBlockEntities()V",at=@At(value="RETURN"))
    protected void AfterTileEntities(CallbackInfo ci) {
        LagSpike.processLagSpikes(LagSpike.TickPhase.TILE_ENTITY, LagSpike.PrePostSubPhase.POST);
    }

    @Inject(method= "getOtherEntities(Lnet/minecraft/entity/Entity;Lnet/minecraft/util/math/Box;Ljava/util/function/Predicate;)Ljava/util/List;",at=@At(value="HEAD"))
    protected void BeforeOtherEntities(Entity except, Box box, Predicate<? super Entity> predicate, CallbackInfoReturnable<List<Entity>> cir) {
        LagSpike.processLagSpikes(LagSpike.TickPhase.OTHER_ENTITY, LagSpike.PrePostSubPhase.PRE);
    }

    @Inject(method= "getOtherEntities(Lnet/minecraft/entity/Entity;Lnet/minecraft/util/math/Box;Ljava/util/function/Predicate;)Ljava/util/List;",at=@At(value="RETURN"))
    protected void AfterOtherEntities(Entity except, Box box, Predicate<? super Entity> predicate, CallbackInfoReturnable<List<Entity>> cir) {
        LagSpike.processLagSpikes(LagSpike.TickPhase.OTHER_ENTITY, LagSpike.PrePostSubPhase.POST);
    }
}
