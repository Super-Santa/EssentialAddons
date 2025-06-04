package me.supersanta.essential_addons.mixins.feature.careful_drop;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import me.supersanta.essential_addons.feature.careful_break.CarefulDrop;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.vehicle.VehicleEntity;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(VehicleEntity.class)
public class VehicleEntityMixin {
    @WrapOperation(
        method = "destroy(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/item/Item;)V",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/entity/vehicle/VehicleEntity;spawnAtLocation(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/item/ItemStack;)Lnet/minecraft/world/entity/item/ItemEntity;"
        )
    )
    private ItemEntity onDropItem(
        VehicleEntity instance,
        ServerLevel level,
        ItemStack stack,
        Operation<ItemEntity> original
    ) {
        Entity entity = CarefulDrop.getVehicleDestroyingEntity();
        CarefulDrop.carefullyDrop(entity, stack, remaining -> original.call(instance, level, remaining));
        return null;
    }
}
