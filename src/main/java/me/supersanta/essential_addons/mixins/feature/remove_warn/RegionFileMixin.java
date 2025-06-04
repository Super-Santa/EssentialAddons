package me.supersanta.essential_addons.mixins.feature.remove_warn;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import me.supersanta.essential_addons.EssentialSettings;
import net.minecraft.world.level.chunk.storage.RegionFile;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(RegionFile.class)
public class RegionFileMixin {
    @WrapWithCondition(
        method = "write",
        at = @At(
            value = "INVOKE",
            target = "Lorg/slf4j/Logger;warn(Ljava/lang/String;[Ljava/lang/Object;)V",
            remap = false
        )
    )
    private boolean shouldWarnOversizedChunk(Logger instance, String s, Object[] objects) {
        return !EssentialSettings.removeWarnOversizedChunk;
    }
}
