package me.supersanta.essential_addons.mixins.feature.sensitive_plants;

import me.supersanta.essential_addons.EssentialSettings;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.SugarCaneBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(SugarCaneBlock.class)
public class SugarCaneBlockMixin {
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
        if (EssentialSettings.sensitiveSugarCane) {
            for (Direction direction : Direction.Plane.HORIZONTAL) {
                BlockState adjacent = level.getBlockState(pos.relative(direction));
                if (adjacent.isSolid() || level.getFluidState(pos.relative(direction)).is(FluidTags.LAVA)) {
                    cir.setReturnValue(false);
                    break;
                }
            }
        }
    }
}
