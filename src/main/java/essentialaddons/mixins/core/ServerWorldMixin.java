package essentialaddons.mixins.core;

import carpet.CarpetServer;
import essentialaddons.EssentialAddonsSettings;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ExperienceOrbEntity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.function.BooleanSupplier;

@Mixin(ServerWorld.class)
public abstract class ServerWorldMixin {

    @Inject(method = "tick", at = @At("HEAD"), cancellable = true)
    public void tick(BooleanSupplier shouldKeepTicking, CallbackInfo ci) {
        if (EssentialAddonsSettings.removeXpEntitiesAfterThreshold > 0) {
            CarpetServer.minecraft_server.getWorlds().forEach(serverWorld ->  {
                List<? extends ExperienceOrbEntity> all = serverWorld.getEntitiesByType(EntityType.EXPERIENCE_ORB, ExperienceOrbEntity -> true);
                if (all.size() > EssentialAddonsSettings.removeXpEntitiesAfterThreshold)
                    all.forEach(Entity::kill);
            });
        }
        if (EssentialAddonsSettings.removeItemEntitiesAfterThreshold > 0) {
            CarpetServer.minecraft_server.getWorlds().forEach(serverWorld ->  {
                List<? extends ItemEntity> all = serverWorld.getEntitiesByType(EntityType.ITEM, ItemEntity -> true);
                if (all.size() > EssentialAddonsSettings.removeItemEntitiesAfterThreshold)
                    all.forEach(Entity::kill);
            });
        }
    }
}