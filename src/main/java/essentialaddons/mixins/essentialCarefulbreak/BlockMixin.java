package essentialaddons.mixins.essentialCarefulbreak;

import essentialaddons.EssentialSettings;
import essentialaddons.EssentialUtils;
import essentialaddons.utils.Subscription;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Block.class)
public abstract class BlockMixin {
    @Inject(method = "afterBreak", at = @At(value = "INVOKE", shift = At.Shift.BEFORE, target = "Lnet/minecraft/block/Block;dropStacks(Lnet/minecraft/block/BlockState;Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/entity/BlockEntity;Lnet/minecraft/entity/Entity;Lnet/minecraft/item/ItemStack;)V"), cancellable = true)
    private void onDropStacks(World world, PlayerEntity player, BlockPos pos, BlockState state, BlockEntity blockEntity, ItemStack stack, CallbackInfo ci) {
        if (player instanceof ServerPlayerEntity serverPlayer && EssentialSettings.essentialCarefulBreak) {
            if ((player.isInSneakingPose() || Subscription.ALWAYS_CAREFUL.hasPlayer(serverPlayer)) && Subscription.ESSENTIAL_CAREFUL_BREAK.hasPlayer(serverPlayer)) {
                EssentialUtils.placeItemInInventory(state, world, pos, blockEntity, serverPlayer, stack);
                ci.cancel();
            }
        }
    }

    @Inject(method = "onBreak", at = @At("HEAD"))
    private void onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player, CallbackInfo ci) {
        if (state.getBlock() == Blocks.PISTON_HEAD && EssentialSettings.essentialCarefulBreak && player instanceof ServerPlayerEntity serverPlayer) {
            if ((serverPlayer.isInSneakingPose() || Subscription.ALWAYS_CAREFUL.hasPlayer(serverPlayer)) && Subscription.ESSENTIAL_CAREFUL_BREAK.hasPlayer(serverPlayer)) {
                Direction direction = state.get(FacingBlock.FACING).getOpposite();
                pos = pos.offset(direction);
                BlockState blockState = world.getBlockState(pos);
                Block block = world.getBlockState(pos).getBlock();
                if (block == Blocks.PISTON || block == Blocks.STICKY_PISTON && blockState.get(PistonBlock.EXTENDED)) {
                    EssentialUtils.placeItemInInventory(blockState, world, pos, null, serverPlayer, serverPlayer.getMainHandStack());
                    world.removeBlock(pos, false);
                }
            }
        }
    }
}
