package essentialaddons.mixins.essentialCarefulbreak;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import essentialaddons.EssentialUtils;
import essentialaddons.utils.Subscription;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.function.Consumer;

import static net.minecraft.block.Block.dropStack;

@Mixin(Block.class)
public abstract class BlockMixin {
    @WrapOperation(
        method = "dropStacks(Lnet/minecraft/block/BlockState;Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/entity/BlockEntity;Lnet/minecraft/entity/Entity;Lnet/minecraft/item/ItemStack;)V",
        at = @At(
            value = "INVOKE",
            target = "Ljava/util/List;forEach(Ljava/util/function/Consumer;)V",
            remap = false
        )
    )
    private static void forEachDroppingStack(
        List<ItemStack> instance,
        Consumer<ItemStack> consumer,
        Operation<Void> original,
        BlockState state,
        World world,
        BlockPos pos,
        @Nullable BlockEntity blockEntity,
        @Nullable Entity entity
    ) {
        if (EssentialUtils.hasCareful(entity, Subscription.ESSENTIAL_CAREFUL_BREAK)) {
            Consumer<ItemStack> drop = consumer;
            consumer = stack -> {
                if (!EssentialUtils.placeItemInInventory(((ServerPlayerEntity) entity), stack)) {
                    drop.accept(stack);
                }
            };
        }
        original.call(instance, consumer);
    }
}
