package me.supersanta.essential_addons.mixins.feature.careful_drop;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import me.supersanta.essential_addons.feature.careful_break.CarefulDrop;
import me.supersanta.essential_addons.utils.EssentialUtilsKt;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.function.Consumer;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {
    @WrapOperation(
        method = "dropFromLootTable(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/damagesource/DamageSource;Z)V",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/level/storage/loot/LootTable;getRandomItems(Lnet/minecraft/world/level/storage/loot/LootParams;JLjava/util/function/Consumer;)V"
        )
    )
    private void onDropFromLootTable(
        LootTable instance,
        LootParams params,
        long seed,
        Consumer<ItemStack> output,
        Operation<Void> original,
        @Local(argsOnly = true) DamageSource source
    ) {
        Consumer<ItemStack> replacement = output;
        if (source.getEntity() instanceof ServerPlayer player) {
            if (EssentialUtilsKt.hasCarefulDrop(player)) {
                replacement = stack -> CarefulDrop.carefullyDrop(player, stack, output);
            }
        }
        original.call(instance, params, seed, replacement);
    }
}
