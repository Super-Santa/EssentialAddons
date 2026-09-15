package me.supersanta.essential_addons.mixins.feature.careful_break;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import me.supersanta.essential_addons.EssentialSettings;
import me.supersanta.essential_addons.feature.careful_break.CarefulBreak;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.ServerPlayerGameMode;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ServerPlayerGameMode.class)
public class ServerPlayerGameModeMixin {
    @Shadow @Final protected ServerPlayer player;

    @WrapOperation(
        method = "destroyBlock",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/server/level/ServerLevel;removeBlock(Lnet/minecraft/core/BlockPos;Z)Z"
        )
    )
    private boolean setScopedBreakerWhenBreakingBlock(ServerLevel instance, BlockPos blockPos, boolean movedByPiston, Operation<Boolean> original) {
        if (EssentialSettings.essentialCarefulBreakDomino) {
            return ScopedValue.where(CarefulBreak.BREAKER, this.player).call(() -> {
                return original.call(instance, blockPos, movedByPiston);
            });
        }
        return original.call(instance, blockPos, movedByPiston);
    }
}
