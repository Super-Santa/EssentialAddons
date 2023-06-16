package essentialaddons.mixins.core;

import essentialaddons.EssentialAddons;
import essentialaddons.EssentialSettings;
import essentialaddons.EssentialUtils;
import essentialaddons.utils.ConfigTeamTeleportBlacklist;
import essentialaddons.utils.NetworkHandler;
import essentialaddons.utils.Subscription;
import net.minecraft.entity.Entity;
import net.minecraft.network.NetworkThreadUtils;
import net.minecraft.network.listener.ServerPlayPacketListener;
import net.minecraft.network.packet.c2s.play.CustomPayloadC2SPacket;
import net.minecraft.network.packet.c2s.play.SpectatorTeleportC2SPacket;
import net.minecraft.scoreboard.AbstractTeam;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayNetworkHandler.class)
abstract class ServerPlayNetworkHandlerMixin implements ServerPlayPacketListener {
    @Shadow
    public ServerPlayerEntity player;

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
        if (EssentialSettings.cameraModeTeamTeleportBlacklist) {
            Entity entity = packet.getTarget(targetWorld);
            if (!(entity instanceof ServerPlayerEntity otherPlayer)) {
                return;
            }
            AbstractTeam team = otherPlayer.getScoreboardTeam();
            if (team != null && ConfigTeamTeleportBlacklist.INSTANCE.isTeamBlacklisted(team.getName())) {
                EssentialUtils.sendToActionBar(playerEntity, "§6This player is on a team which you cannot teleport to!");
                return;
            }
        }
        playerEntity.teleport(targetWorld, x, y, z, yaw, pitch);
    }

    @Inject(method = "onCustomPayload", at = @At("HEAD"), cancellable = true)
    private void onCustomPayload(CustomPayloadC2SPacket packet, CallbackInfo ci) {
        Identifier identifier = packet.getChannel();
        for (NetworkHandler networkHandler : EssentialAddons.NETWORK_HANDLERS) {
            if (networkHandler.getNetworkChannel().equals(identifier)) {
                NetworkThreadUtils.forceMainThread(packet, this, (ServerWorld) this.player.getWorld());
                networkHandler.handlePacket(packet.getData(), this.player);
                ci.cancel();
            }
        }
    }
}