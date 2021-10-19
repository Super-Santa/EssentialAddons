package essentialaddons.mixins.HUDController;

import carpet.logging.HUDController;
import essentialaddons.logging.EssentialAddonsHUDController;
import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin(HUDController.class)
public abstract class HUDControllerMixin {
    @Inject(
            method = "update_hud", at = @At(value = "INVOKE",target = "Ljava/util/Map;keySet()Ljava/util/Set;"), remap = false)
    private static void updateEssentialAddonsHUDLoggers(MinecraftServer server, CallbackInfo ci) {
        EssentialAddonsHUDController.updateHUD(server);
    }
}