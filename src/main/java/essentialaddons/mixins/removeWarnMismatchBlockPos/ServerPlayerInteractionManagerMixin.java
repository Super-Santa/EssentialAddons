package essentialaddons.mixins.removeWarnMismatchBlockPos;

import essentialaddons.EssentialSettings;
import net.minecraft.server.network.ServerPlayerInteractionManager;
import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ServerPlayerInteractionManager.class)
public class ServerPlayerInteractionManagerMixin {
    @Redirect(method = "processBlockBreakingAction", at = @At(value = "INVOKE", target = "Lorg/apache/logging/log4j/Logger;warn(Ljava/lang/String;Ljava/lang/Object;Ljava/lang/Object;)V"), require = 0)
    private void onWarn(Logger logger, String message, Object p0, Object p1) {
        if (EssentialSettings.removeWarnMismatchBlockPos) {
            return;
        }
        logger.warn("Mismatch in destroy block pos: {} {}", p0, p1);
    }
}
