package me.supersanta.essential_addons.mixins.feature.structure_block;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import me.supersanta.essential_addons.EssentialSettings;
import net.minecraft.world.level.block.entity.StructureBlockEntity;
import net.minecraft.world.level.levelgen.structure.templatesystem.LiquidSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(StructureBlockEntity.class)
public class StructureBlockEntityMixin {
    @ModifyExpressionValue(
        method = "placeStructure(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureTemplate;)V",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructurePlaceSettings;setIgnoreEntities(Z)Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructurePlaceSettings;"
        )
    )
    private StructurePlaceSettings onBuildSettings(StructurePlaceSettings original) {
        LiquidSettings settings = EssentialSettings.structureBlockReplaceFluids ?
            LiquidSettings.IGNORE_WATERLOGGING : LiquidSettings.APPLY_WATERLOGGING;
        return original.setLiquidSettings(settings);
    }
}
