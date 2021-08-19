package essentialaddons.mixins.removeWarnMismatchBlockPos;

import essentialaddons.EssentialAddonsSettings;
import net.minecraft.server.network.ServerPlayerInteractionManager;
import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ServerPlayerInteractionManager.class)
public class ServerPlayerInteractionManagerMixin {
    @Redirect(method = "processBlockBreakingAction", at = @At(value = "INVOKE", target = "Lorg/apache/logging/log4j/Logger;warn(Ljava/lang/String;)V"))
    private void onWarn(Logger logger, String message) {
        if (EssentialAddonsSettings.removeWarnMismatchBlockPos)
            return;
        logger.warn(message);
    }
}
