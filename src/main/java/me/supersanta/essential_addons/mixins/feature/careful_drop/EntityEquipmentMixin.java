package me.supersanta.essential_addons.mixins.feature.careful_drop;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import me.supersanta.essential_addons.feature.careful_break.CarefulDrop;
import net.minecraft.world.entity.EntityEquipment;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(EntityEquipment.class)
public class EntityEquipmentMixin {
    @WrapOperation(
        method = "dropAll",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/entity/LivingEntity;drop(Lnet/minecraft/world/item/ItemStack;ZZ)Lnet/minecraft/world/entity/item/ItemEntity;"
        )
    )
    private ItemEntity onDropAll(
        LivingEntity instance,
        ItemStack stack,
        boolean randomizeMotion,
        boolean includeThrower,
        Operation<ItemEntity> original
    ) {
        CarefulDrop.carefullyDrop(instance.getLastAttacker(), stack, remaining -> {
            original.call(instance, remaining, randomizeMotion, includeThrower);
        });
        return null;
    }
}
