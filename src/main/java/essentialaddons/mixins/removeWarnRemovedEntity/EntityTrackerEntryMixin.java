package essentialaddons.mixins.removeWarnRemovedEntity;

import essentialaddons.EssentialSettings;
import net.minecraft.server.network.EntityTrackerEntry;
//#if MC >= 11800
import org.slf4j.Logger;
//#else
//$$import org.apache.logging.log4j.Logger;
//#endif
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(EntityTrackerEntry.class)
public class EntityTrackerEntryMixin {



    //#if MC >= 12000
    @Redirect(method = "sendPackets", at = @At(value = "INVOKE", target = "Lorg/slf4j/Logger;warn(Ljava/lang/String;Ljava/lang/Object;)V", remap = false), require = 0)
    //#elseif MC >= 11800
    //$$@Redirect(method = "sendPackets(Ljava/util/function/Consumer;)V", at = @At(value = "INVOKE", target = "Lorg/slf4j/Logger;warn(Ljava/lang/String;Ljava/lang/Object;)V", remap = false), require = 0)
    //#else
    //$$@Redirect(method = "sendPackets(Ljava/util/function/Consumer;)V", at = @At(value = "INVOKE", target = "Lorg/apache/logging/log4j/Logger;warn(Ljava/lang/String;Ljava/lang/Object;)V", remap = false), require = 0)
    //#endif
    private void onWarn(Logger logger, String message, Object p0) {
        if (!EssentialSettings.removeWarnRemovedEntity) {
            logger.warn(message, p0);
        }
    }
}
