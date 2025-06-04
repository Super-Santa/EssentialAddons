package me.supersanta.essential_addons.mixins.feature.careful_drop;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import me.supersanta.essential_addons.feature.careful_break.CarefulDrop;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.decoration.ItemFrame;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ItemFrame.class)
public class ItemFrameMixin {
    @WrapOperation(
        method = "dropItem(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/entity/Entity;Z)V",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/entity/decoration/ItemFrame;spawnAtLocation(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/item/ItemStack;)Lnet/minecraft/world/entity/item/ItemEntity;"
        )
    )
    private ItemEntity onDropItem(
        ItemFrame instance,
        ServerLevel level,
        ItemStack stack,
        Operation<ItemEntity> original,
        @Local(argsOnly = true) Entity entity
    ) {
        CarefulDrop.carefullyDrop(entity, stack, remaining -> original.call(instance, level, remaining));
        return null;
    }
}
