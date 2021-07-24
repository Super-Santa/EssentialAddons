package essentialaddons.mixins.essentialCarefulbreak;

import essentialaddons.EssentialAddonsSettings;
import essentialaddons.EssentialAddonsUtils;
import essentialaddons.helpers.SubscribeData;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.IOException;

@Mixin(Block.class)
public abstract class BlockMixin {
    //Code from wholmT
    @Redirect(method = "afterBreak", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/Block;dropStacks(Lnet/minecraft/block/BlockState;Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/entity/BlockEntity;Lnet/minecraft/entity/Entity;Lnet/minecraft/item/ItemStack;)V"), require = 0)
    private void onDropStacks(BlockState state, World world, BlockPos pos, BlockEntity blockEntity, Entity entity, ItemStack stack) throws IOException {
        if (EssentialAddonsSettings.essentialCarefulBreak && entity instanceof PlayerEntity && entity.isInSneakingPose()) {
            SubscribeData data = SubscribeData.subscribeData.get(entity.getUuid());
            if (data == null) {
                SubscribeData.subscribeData.put(entity.getUuid(), new SubscribeData(false, false));
                SubscribeData.writeSaveFile(SubscribeData.subscribeData);
            }
            else if (data.isSubscribedCarefulBreak) {
                EssentialAddonsUtils.placeItemInInventory(state, world, pos, blockEntity, entity, stack);
                return;
            }
        }
        Block.dropStacks(state, world, pos, blockEntity, entity, stack);
    }
    //carefulBreak PISTON_HEADS
    @Inject(method = "onBreak", at = @At("HEAD"))
    private void onBreak1(World world, BlockPos pos, BlockState state, PlayerEntity player, CallbackInfo ci) throws IOException {
        if (EssentialAddonsSettings.essentialCarefulBreak && player.isInSneakingPose()) {
            SubscribeData data = SubscribeData.subscribeData.get(player.getUuid());
            if (data == null) {
                SubscribeData.subscribeData.put(player.getUuid(), new SubscribeData(false, false));
                SubscribeData.writeSaveFile(SubscribeData.subscribeData);
            }
            else if (data.isSubscribedCarefulBreak) {
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
