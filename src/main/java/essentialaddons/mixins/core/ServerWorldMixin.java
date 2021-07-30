package essentialaddons.mixins.core;

import carpet.CarpetServer;
import carpet.patches.EntityPlayerMPFake;
import essentialaddons.EssentialAddonsSettings;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.MathHelper;
import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.function.BooleanSupplier;

@Mixin(ServerWorld.class)
public abstract class ServerWorldMixin {

    @Inject(method = "tick", at = @At("HEAD"), cancellable = true)
    public void tick(BooleanSupplier shouldKeepTicking, CallbackInfo ci) {
        if (EssentialAddonsSettings.removeXpEntitiesAfterThreshold > 0) {
            CarpetServer.minecraft_server.getWorlds().forEach(serverWorld ->  {
                List<Entity> all = serverWorld.getEntitiesByType(EntityType.EXPERIENCE_ORB, ExperienceOrbEntity -> true);
                if (all.size() > EssentialAddonsSettings.removeXpEntitiesAfterThreshold)
                    all.forEach(Entity::remove);
            });
        }
        if (EssentialAddonsSettings.removeItemEntitiesAfterThreshold > 0) {
            CarpetServer.minecraft_server.getWorlds().forEach(serverWorld ->  {
                List<Entity> all = serverWorld.getEntitiesByType(EntityType.ITEM, ItemEntity -> true);
                if (all.size() > EssentialAddonsSettings.removeItemEntitiesAfterThreshold)
                    all.forEach(Entity::remove);
            });
        }
    }
    @Redirect(method = "checkEntityChunkPos", at = @At(value = "INVOKE", target = "Lorg/apache/logging/log4j/Logger;warn(Ljava/lang/String;Ljava/lang/Object;)V"))
    private void onWarn(Logger logger, String message, Object p0, Entity entity) {
        if (entity instanceof EntityPlayerMPFake) {
            ServerPlayerEntity playerEntity = ((ServerPlayerEntity) entity);
            playerEntity.getServerWorld().getChunk(MathHelper.floor(playerEntity.getX()/16), MathHelper.floor(playerEntity.getZ()/16)).addEntity(entity);
            logger.warn("Entity {} may not be loaded properly", p0);
        }
        else
            logger.warn("Entity {} left loaded chunk area", p0);
    }
}