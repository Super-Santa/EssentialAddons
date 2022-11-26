package essentialaddons.mixins.removeWarnMismatchBlockPos;

import essentialaddons.EssentialSettings;
import net.minecraft.server.network.ServerPlayerInteractionManager;

//#if MC >= 11800
import org.slf4j.Logger;
//#else
//$$import org.apache.logging.log4j.Logger;
//#endif
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ServerPlayerInteractionManager.class)
public class ServerPlayerInteractionManagerMixin {
    //#if MC >= 11800
    @Redirect(method = "processBlockBreakingAction", at = @At(value = "INVOKE", target = "Lorg/slf4j/Logger;warn(Ljava/lang/String;Ljava/lang/Object;Ljava/lang/Object;)V", remap = false), require = 0)
    //#else
    //$$@Redirect(method = "processBlockBreakingAction", at = @At(value = "INVOKE", target = "Lorg/apache/logging/log4j/Logger;warn(Ljava/lang/String;Ljava/lang/Object;Ljava/lang/Object;)V", remap = false), require = 0)
    //#endif
    private void onWarn(Logger logger, String message, Object p0, Object p1) {
        if (!EssentialSettings.removeWarnMismatchBlockPos) {
            logger.warn("Mismatch in destroy block pos: {} {}", p0, p1);
        }
    }
}
