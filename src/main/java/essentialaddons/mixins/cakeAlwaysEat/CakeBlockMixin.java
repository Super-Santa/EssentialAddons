package essentialaddons.mixins.cakeAlwaysEat;

import essentialaddons.EssentialSettings;
import net.minecraft.block.BlockState;
import net.minecraft.block.CakeBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.stat.Stats;
import net.minecraft.state.property.IntProperty;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldAccess;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(CakeBlock.class)
public class CakeBlockMixin {
    @Final
    @Shadow
    public static IntProperty BITES;

    @Inject(method = "tryEat", at = @At("HEAD"))
    private static void tryEat(WorldAccess world, BlockPos pos, BlockState state, PlayerEntity player, CallbackInfoReturnable<CallbackInfo> info) {
        if (EssentialSettings.cakeAlwaysEat) {
            player.incrementStat(Stats.EAT_CAKE_SLICE);
            player.getHungerManager().add(2, 0.1F);
            int i = state.get(BITES);
            if (i < 6) {
                world.setBlockState(pos, state.with(BITES, i + 1), 3);
                player.swingHand(Hand.MAIN_HAND, true);
            }
            else {
                world.removeBlock(pos, false);
            }
        }
    }
}

