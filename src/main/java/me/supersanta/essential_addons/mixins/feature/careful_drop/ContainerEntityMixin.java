package me.supersanta.essential_addons.mixins.feature.careful_drop;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import me.supersanta.essential_addons.feature.careful_break.CarefulDrop;
import net.minecraft.world.Container;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.vehicle.ContainerEntity;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ContainerEntity.class)
public interface ContainerEntityMixin {
    @WrapOperation(
        method = "chestVehicleDestroyed",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/Containers;dropContents(Lnet/minecraft/world/level/Level;Lnet/minecraft/world/entity/Entity;Lnet/minecraft/world/Container;)V"
        )
    )
    private void onDropContainerContents(
        Level level,
        Entity entity,
        Container inventory,
        Operation<Void> original,
        @Local(argsOnly = true) DamageSource source
    ) {
        if (!CarefulDrop.carefullyDropContents(source, level, entity, inventory)) {
            original.call(level, entity, inventory);
        }
    }
}
