package essentialaddons.mixins.essentialCarefulbreak;

import carpet.CarpetServer;
import essentialaddons.EssentialAddonsSettings;
import essentialaddons.EssentialAddonsUtils;
import essentialaddons.helpers.SubscribeData;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.IOException;

@Mixin(Block.class)
public abstract class BlockMixin {
    //Code from wholmT
    @Inject(
            method = "afterBreak",
            at = @At(
                    value = "INVOKE",
                    shift = At.Shift.BEFORE,
                    target = "Lnet/minecraft/block/Block;dropStacks(Lnet/minecraft/block/BlockState;Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/entity/BlockEntity;Lnet/minecraft/entity/Entity;Lnet/minecraft/item/ItemStack;)V"
            ),
            cancellable = true
    )
    private void onDropStacks(World world, PlayerEntity player, BlockPos pos, BlockState state, BlockEntity blockEntity, ItemStack stack, CallbackInfo ci) throws IOException {
        if (CarpetServer.minecraft_server != null && EssentialAddonsSettings.essentialCarefulBreak && player.isInSneakingPose()) {
            SubscribeData data = SubscribeData.subscribeData.get(player.getUuid());
            if (data == null) {
                SubscribeData.subscribeData.put(player.getUuid(), new SubscribeData(false, false));
                SubscribeData.writeSaveFile(SubscribeData.subscribeData);
            }
            else if (data.isSubscribedCarefulBreak()) {
                EssentialAddonsUtils.placeItemInInventory(state, world, pos, blockEntity, player, stack);
                ci.cancel();
            }
        }
    }
    //carefulBreak PISTON_HEADS
    @Inject(method = "onBreak", at = @At("HEAD"))
    private void onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player, CallbackInfo ci) throws IOException {
        if (CarpetServer.minecraft_server != null && EssentialAddonsSettings.essentialCarefulBreak && player.isInSneakingPose()) {
            SubscribeData data = SubscribeData.subscribeData.get(player.getUuid());
            if (data == null) {
                SubscribeData.subscribeData.put(player.getUuid(), new SubscribeData(false, false));
                SubscribeData.writeSaveFile(SubscribeData.subscribeData);
            }
            else if (data.isSubscribedCarefulBreak()) {
                if (state.getBlock() == Blocks.PISTON_HEAD) {
                    Direction direction = state.get(FacingBlock.FACING).getOpposite();
                    pos = pos.offset(direction);
                    BlockState blockState = world.getBlockState(pos);
                    Block block = world.getBlockState(pos).getBlock();
                    if (block == Blocks.PISTON || block == Blocks.STICKY_PISTON && blockState.get(PistonBlock.EXTENDED)) {
                        EssentialAddonsUtils.placeItemInInventory(blockState, world, pos, null, player, player.getMainHandStack());
                        world.removeBlock(pos, false);
                    }
                }
            }
        }
    }
}
