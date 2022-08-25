package essentialaddons.mixins.core;

import essentialaddons.EssentialAddons;
import essentialaddons.utils.Config;
import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MinecraftServer.class)
public class MinecraftServerMixin {
    @Inject(method = "save", at = @At("RETURN"))
    private void onWorldsSaved(boolean suppressLogs, boolean bl, boolean bl2, CallbackInfoReturnable<Boolean> cir) {
        // Auto-saves and /save-all
        for (Config config : EssentialAddons.CONFIG_SET) {
            config.saveConfig();
        }
    }
}
