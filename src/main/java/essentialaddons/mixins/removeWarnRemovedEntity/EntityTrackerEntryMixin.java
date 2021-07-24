package essentialaddons.mixins.removeWarnRemovedEntity;

import essentialaddons.EssentialAddonsSettings;
import net.minecraft.server.network.EntityTrackerEntry;
import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(EntityTrackerEntry.class)
public class EntityTrackerEntryMixin {
    @Redirect(method = "sendPackets", at = @At(value = "INVOKE", target = "Lorg/apache/logging/log4j/Logger;warn(Ljava/lang/String;)V"))
    private void onWarn(Logger logger, String message) {
        if (EssentialAddonsSettings.removeWarnRemovedEntity)
            return;
        logger.warn(message);
    }
}
