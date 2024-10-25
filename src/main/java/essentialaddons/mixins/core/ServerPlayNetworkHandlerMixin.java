package essentialaddons.mixins.core;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import essentialaddons.EssentialSettings;
import essentialaddons.EssentialUtils;
import essentialaddons.utils.ConfigTeamTeleportBlacklist;
import essentialaddons.utils.Subscription;
import net.minecraft.entity.Entity;
import net.minecraft.network.listener.ServerPlayPacketListener;
import net.minecraft.network.packet.c2s.play.SpectatorTeleportC2SPacket;
import net.minecraft.network.packet.s2c.play.PositionFlag;
import net.minecraft.scoreboard.AbstractTeam;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

import java.util.Set;

@Mixin(ServerPlayNetworkHandler.class)
abstract class ServerPlayNetworkHandlerMixin implements ServerPlayPacketListener {
    @Shadow
    public ServerPlayerEntity player;

    @WrapWithCondition(
        method = "onSpectatorTeleport",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/server/network/ServerPlayerEntity;teleport(Lnet/minecraft/server/world/ServerWorld;DDDLjava/util/Set;FFZ)Z"
        )
    )
    private boolean checkTeleportBlacklist(
        ServerPlayerEntity instance,
        ServerWorld world,
        double destX,
        double destY,
        double destZ,
        Set<PositionFlag> flags,
        float yaw,
        float pitch,
        boolean resetCamera,
        SpectatorTeleportC2SPacket packet
    ) {
        Entity entity = packet.getTarget(world);
        if (EssentialSettings.cameraModeTeleportBlacklist) {
            if (!(entity instanceof ServerPlayerEntity otherPlayer)) {
                return false;
            }
            if (Subscription.TELEPORT_BLACKLIST.hasPlayer(otherPlayer) && !instance.hasPermissionLevel(4)) {
                EssentialUtils.sendToActionBar(instance, "§6This player has teleporting §cDISABLED");
                return false;
            }
        }
        if (EssentialSettings.cameraModeTeamTeleportBlacklist) {
            if (!(entity instanceof ServerPlayerEntity otherPlayer)) {
                return false;
            }
            AbstractTeam team = otherPlayer.getScoreboardTeam();
            if (team != null && ConfigTeamTeleportBlacklist.INSTANCE.isTeamBlacklisted(team.getName()) && !instance.hasPermissionLevel(4)) {
                EssentialUtils.sendToActionBar(instance, "§6This player is on a team which you cannot teleport to!");
                return false;
            }
        }
        return true;
    }
}