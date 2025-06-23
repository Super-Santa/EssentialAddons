package me.supersanta.essential_addons.mixins.feature.structure_block;

import me.supersanta.essential_addons.EssentialSettings;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.phys.AABB;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(StructureTemplate.class)
public class StructureTemplateMixin {
    @Shadow private Vec3i size;

    @Inject(
        method = "placeInWorld",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureTemplate;placeEntities(Lnet/minecraft/world/level/ServerLevelAccessor;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/Mirror;Lnet/minecraft/world/level/block/Rotation;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/levelgen/structure/BoundingBox;ZLnet/minecraft/util/ProblemReporter;)V"
        )
    )
    private void beforeSpawnEntities(
        ServerLevelAccessor level,
        BlockPos offset,
        BlockPos pos,
        StructurePlaceSettings settings,
        RandomSource random,
        int flags,
        CallbackInfoReturnable<Boolean> cir
    ) {
        if (EssentialSettings.structureBlockKillEntities) {
            AABB aabb = AABB.encapsulatingFullBlocks(pos, pos.offset(this.size));
            List<Entity> entities = level.getEntitiesOfClass(Entity.class, aabb, e -> !(e instanceof Player));
            for (Entity entity : entities) {
                entity.discard();
            }
        }
    }
}
