package me.supersanta.essential_addons.mixins.feature.sensitive_plants;

import me.supersanta.essential_addons.EssentialSettings;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.BambooStalkBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BambooStalkBlock.class)
public abstract class BambooStalkBlockMixin {
    @Inject(
        method = "canSurvive",
        at = @At("HEAD"),
        cancellable = true
    )
    @SuppressWarnings("deprecation")
    private void onCanSurvive(
        BlockState state,
        LevelReader level,
        BlockPos pos,
        CallbackInfoReturnable<Boolean> cir
    ) {
        if (EssentialSettings.sensitiveBamboo) {
            for (Direction direction : Direction.Plane.HORIZONTAL) {
                BlockState adjacent = level.getBlockState(pos.relative(direction));
                if (adjacent.isSolid() || level.getFluidState(pos.relative(direction)).is(FluidTags.LAVA)) {
                    cir.setReturnValue(false);
                    break;
                }
            }
        }
    }

    @Inject(
        method = "growBamboo",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/level/Level;setBlock(Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;I)Z",
            ordinal = 2
        )
    )
    private void onSetBlock(
        BlockState state,
        Level level,
        BlockPos pos,
        RandomSource random,
        int age,
        CallbackInfo ci
    ) {
        if (EssentialSettings.sensitiveBamboo) {
            if (!state.canSurvive(level, pos.above())) {
                level.scheduleTick(pos.above(), (Block) (Object) this, 1);
            }
        }
    }
}
