package essentialaddons.mixins.removeWarnOversizedChunk;

import essentialaddons.EssentialSettings;
import net.minecraft.world.storage.RegionFile;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(RegionFile.class)
public class RegionFileMixin {
    @Redirect(method = "writeChunk", at = @At(value = "INVOKE", target = "Lorg/slf4j/Logger;warn(Ljava/lang/String;[Ljava/lang/Object;)V", remap = false), require = 0)
    private void onWarn(Logger logger, String message, Object[] objects) {
        if (!EssentialSettings.removeWarnOversizedChunk) {
            logger.warn(message, objects);
        }
    }
}
