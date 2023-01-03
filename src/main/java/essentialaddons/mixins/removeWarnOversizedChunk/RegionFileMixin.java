package essentialaddons.mixins.removeWarnOversizedChunk;

import essentialaddons.EssentialSettings;
import net.minecraft.world.storage.RegionFile;
//#if MC >= 11800
import org.slf4j.Logger;
//#else
//$$import org.apache.logging.log4j.Logger;
//#endif
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(RegionFile.class)
public class RegionFileMixin {
    //#if MC >= 11800
    @Redirect(method = "writeChunk", at = @At(value = "INVOKE", target = "Lorg/slf4j/Logger;warn(Ljava/lang/String;[Ljava/lang/Object;)V", remap = false), require = 0)
    private void onWarn(Logger logger, String message, Object[] objects) {
        if (!EssentialSettings.removeWarnOversizedChunk) {
            logger.warn(message, objects);
        }
    }
    //#else
    //$$@Redirect(method = "writeChunk", at = @At(value = "INVOKE", target = "Lorg/apache/logging/log4j/Logger;warn(Ljava/lang/String;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;)V", remap = false), require = 0)
    //$$private void onWarn(org.apache.logging.log4j.Logger logger, String message, Object pos, Object buff, Object path) {
    //$$    if (!EssentialSettings.removeWarnOversizedChunk) {
    //$$        logger.warn(message, pos, buff, path);
    //$$    }
    //$$}
    //#endif
}
