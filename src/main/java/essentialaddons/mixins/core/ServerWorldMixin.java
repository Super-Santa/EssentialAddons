package essentialaddons.mixins.core;

import essentialaddons.EssentialSettings;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ExperienceOrbEntity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.TypeFilter;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.function.BooleanSupplier;
import java.util.function.Predicate;

@Mixin(ServerWorld.class)
public abstract class ServerWorldMixin {
    @Shadow public abstract <T extends Entity> List<? extends T> getEntitiesByType(TypeFilter<Entity, T> filter, Predicate<? super T> predicate);

    @Inject(method = "tick", at = @At("HEAD"))
    public void tick(BooleanSupplier shouldKeepTicking, CallbackInfo ci) {
        if (EssentialSettings.removeXpEntitiesAfterThreshold > 0) {
            List<? extends ExperienceOrbEntity> all = this.getEntitiesByType(EntityType.EXPERIENCE_ORB, ExperienceOrbEntity -> true);
            if (all.size() > EssentialSettings.removeXpEntitiesAfterThreshold) {
                all.forEach(Entity::kill);
            }
        }
        if (EssentialSettings.removeItemEntitiesAfterThreshold > 0) {
            List<? extends ItemEntity> all = this.getEntitiesByType(EntityType.ITEM, ItemEntity -> true);
            if (all.size() > EssentialSettings.removeItemEntitiesAfterThreshold) {
                all.forEach(Entity::kill);
            }
        }
    }
}