package essentialaddons.mixins.structureBlockReplaceFluids;

import essentialaddons.EssentialSettings;
import net.minecraft.block.entity.StructureBlockBlockEntity;
import net.minecraft.structure.StructurePlacementData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(StructureBlockBlockEntity.class)
public class StructureBlockBlockEntityMixin {
	@Redirect(
		method = "loadAndPlaceStructure(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/structure/StructureTemplate;)V",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/structure/StructurePlacementData;setIgnoreEntities(Z)Lnet/minecraft/structure/StructurePlacementData;"
		)
	)
	private StructurePlacementData modifyStructurePlacement(StructurePlacementData instance, boolean ignoreEntities) {
		return instance.setIgnoreEntities(ignoreEntities).setPlaceFluids(!EssentialSettings.structureBlockReplaceFluids);
	}
}
