package essentialaddons.mixins.core;

import carpet.CarpetServer;
import essentialaddons.EssentialAddonsServer;
import essentialaddons.EssentialAddonsSettings;
import essentialaddons.helpers.CameraData;
import essentialaddons.helpers.ReloadFakePlayers;
import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.io.IOException;

@Mixin(MinecraftServer.class)
public class MinecraftServer_onSaveMixin {
    @Inject(method = "save", at = @At("RETURN"))
    private void onWorldsSaved(boolean suppressLogs, boolean bl, boolean bl2, CallbackInfoReturnable<Boolean> cir) {
        if (CarpetServer.minecraft_server == null)
            return;
        try {
            CameraData.writeSaveFile(CameraData.cameraData);
        }
        catch (IOException e) {
            e.printStackTrace();
            EssentialAddonsServer.LOGGER.error("Failed to write camera data file");
        }
        if (EssentialAddonsSettings.reloadFakePlayers)
            ReloadFakePlayers.saveFakePlayers(CarpetServer.minecraft_server);
    }
}
