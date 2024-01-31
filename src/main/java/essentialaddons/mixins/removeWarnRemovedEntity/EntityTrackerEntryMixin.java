package essentialaddons.mixins.removeWarnRemovedEntity;

import essentialaddons.EssentialSettings;
import net.minecraft.server.network.EntityTrackerEntry;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(EntityTrackerEntry.class)
public class EntityTrackerEntryMixin {
    @Redirect(method = "sendPackets", at = @At(value = "INVOKE", target = "Lorg/slf4j/Logger;warn(Ljava/lang/String;Ljava/lang/Object;)V", remap = false), require = 0)
    private void onWarn(Logger logger, String message, Object p0) {
        if (!EssentialSettings.removeWarnRemovedEntity) {
            logger.warn(message, p0);
        }
    }
}
