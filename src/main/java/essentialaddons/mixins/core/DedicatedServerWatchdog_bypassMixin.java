package essentialaddons.mixins.core;

import essentialaddons.EssentialAddonsSettings;
import net.minecraft.server.dedicated.DedicatedServerWatchdog;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(DedicatedServerWatchdog.class)
public class DedicatedServerWatchdog_bypassMixin {

    @ModifyVariable(method= "run()V",at=@At(value="FIELD",target="Lnet/minecraft/server/dedicated/DedicatedServerWatchdog;maxTickTime:J"))
    private long ModifyMaxTickTime(long maxTickTime) {
        if (EssentialAddonsSettings.watchDogFix) {
            return Long.MAX_VALUE;
        }
        return maxTickTime;
    }
}
