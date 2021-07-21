package essentialaddons.mixins.core;

import essentialaddons.EssentialAddonsServer;
import essentialaddons.EssentialAddonsSettings;
import net.minecraft.server.dedicated.MinecraftDedicatedServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(MinecraftDedicatedServer.class)
public class MinecraftDedicatedServer_watchdogMixin {

    @Redirect(method= "setupServer()Z",at=@At(value="INVOKE",target="Ljava/lang/Thread;start()V"))
    protected void OverrideWatchdogThread(Thread me) {
        EssentialAddonsServer.watchdogThread = me;
        if (!EssentialAddonsSettings.watchDogFix) {
            me.start();
        }
    }
}
