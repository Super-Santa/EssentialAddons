package essentialaddons.mixins.core;

import essentialaddons.EssentialSettings;
import essentialaddons.EssentialUtils;
import essentialaddons.utils.Subscription;
import net.minecraft.entity.Entity;
import net.minecraft.network.packet.c2s.play.SpectatorTeleportC2SPacket;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ServerPlayNetworkHandler.class)
class ServerPlayNetworkHandlerMixin {
    @Redirect(method = "onSpectatorTeleport", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/network/ServerPlayerEntity;teleport(Lnet/minecraft/server/world/ServerWorld;DDDFF)V"), require = 0)
    private void checkTeleportBlacklist(ServerPlayerEntity playerEntity, ServerWorld targetWorld, double x, double y, double z, float yaw, float pitch, SpectatorTeleportC2SPacket packet) {
        if (EssentialSettings.cameraModeTeleportBlacklist) {
            Entity entity = packet.getTarget(targetWorld);
            if (!(entity instanceof ServerPlayerEntity otherPlayer)) {
                return;
            }
            if (Subscription.TELEPORT_BLACKLIST.hasPlayer(otherPlayer) && !playerEntity.hasPermissionLevel(4)) {
                EssentialUtils.sendToActionBar(playerEntity, "§6This player has teleporting §cDISABLED");
                return;
            }
        }
        playerEntity.teleport(targetWorld, x, y, z, yaw, pitch);
    }
}