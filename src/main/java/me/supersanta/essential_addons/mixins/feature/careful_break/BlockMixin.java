package me.supersanta.essential_addons.mixins.feature.careful_break;

import me.supersanta.essential_addons.feature.careful_break.CarefulBreak;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.Block;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(Block.class)
public class BlockMixin {
    @ModifyArg(
        method = "updateOrDestroy(Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/level/LevelAccessor;Lnet/minecraft/core/BlockPos;II)V",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/level/LevelAccessor;destroyBlock(Lnet/minecraft/core/BlockPos;ZLnet/minecraft/world/entity/Entity;I)Z"
        ),
        index = 2
    )
    private static Entity setCarefulBreaker(Entity breaker) {
        if (breaker != null) {
            return breaker;
        }
        if (CarefulBreak.BREAKER.isBound()) {
            return CarefulBreak.BREAKER.get();
        }
        return null;
    }
}
