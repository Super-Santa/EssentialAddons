package me.supersanta.essential_addons.mixins.feature.careful_drop;

import me.supersanta.essential_addons.feature.careful_break.CarefulDrop;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.vehicle.AbstractChestBoat;
import net.minecraft.world.entity.vehicle.MinecartTNT;
import net.minecraft.world.entity.vehicle.VehicleEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({AbstractChestBoat.class, VehicleEntity.class, MinecartTNT.class})
public class GenericVehicleEntityMixin {
    @Inject(
        method = "destroy(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/damagesource/DamageSource;)V",
        at = @At("HEAD")
    )
    private void preDestroy(ServerLevel level, DamageSource source, CallbackInfo ci) {
        CarefulDrop.setVehicleDestroyingEntity(source.getEntity());
    }

    @Inject(
        method = "destroy(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/damagesource/DamageSource;)V",
        at = @At("RETURN")
    )
    private void postDestroy(ServerLevel level, DamageSource source, CallbackInfo ci) {
        CarefulDrop.setVehicleDestroyingEntity(null);
    }
}
