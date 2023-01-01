package essentialaddons.mixins.structureBlockKillEntities;

import essentialaddons.EssentialSettings;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.structure.StructurePlacementData;
import net.minecraft.structure.StructureTemplate;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3i;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.ServerWorldAccess;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(StructureTemplate.class)
public class StructureTemplateMixin {
	@Shadow
	private Vec3i size;

	@Inject(method = "place", at = @At(value = "INVOKE", target = "Lnet/minecraft/structure/StructureTemplate;spawnEntities(Lnet/minecraft/world/ServerWorldAccess;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/util/BlockMirror;Lnet/minecraft/util/BlockRotation;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/util/math/BlockBox;Z)V", shift = At.Shift.BEFORE))
	//#if MC >= 11900
	private void beforeSpawnEntities(ServerWorldAccess world, BlockPos pos, BlockPos pivot, StructurePlacementData placementData, Random random, int flags, CallbackInfoReturnable<Boolean> cir) {
		//#else
		//$$private void beforeSpawnEntities(ServerWorldAccess world, BlockPos pos, BlockPos pivot, StructurePlacementData placementData, java.util.Random random, int flags, CallbackInfoReturnable<Boolean> cir) {
		//#endif
		if (EssentialSettings.structureBlockKillEntities) {
			List<Entity> entitiesToKill = world.getEntitiesByClass(Entity.class, new Box(pos, pos.add(this.size)), e -> !(e instanceof PlayerEntity));
			for (Entity entity : entitiesToKill) {
				entity.discard();
			}
		}
	}
}
