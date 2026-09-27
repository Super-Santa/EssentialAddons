package me.supersanta.essential_addons.mixins.feature.careful_drop;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import me.supersanta.essential_addons.feature.careful_break.CarefulDrop;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.decoration.painting.Painting;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Painting.class)
public class PaintingMixin {
    @WrapOperation(
        method = "dropItem",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/entity/decoration/painting/Painting;spawnAtLocation(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/item/ItemStack;)Lnet/minecraft/world/entity/item/ItemEntity;"
        )
    )
    private ItemEntity onDropItem(
        Painting instance,
        ServerLevel level,
        ItemStack itemStack,
        Operation<ItemEntity> original,
        @Local(argsOnly = true) @Nullable Entity causedBy
    ) {
        return CarefulDrop.carefullyDrop(causedBy, itemStack, remaining -> {
            return original.call(instance, level, remaining);
        });
    }
}
