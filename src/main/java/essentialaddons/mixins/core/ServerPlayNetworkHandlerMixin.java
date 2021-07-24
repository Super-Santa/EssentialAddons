package essentialaddons.mixins.core;

import essentialaddons.EssentialAddonsSettings;
import essentialaddons.EssentialAddonsUtils;
import essentialaddons.helpers.SubscribeData;
import net.minecraft.network.packet.c2s.play.ClickSlotC2SPacket;
import net.minecraft.network.packet.c2s.play.SpectatorTeleportC2SPacket;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Objects;
import java.util.UUID;

@Mixin(ServerPlayNetworkHandler.class)
class ServerPlayNetworkHandlerMixin {

    @Inject(method="onClickSlot", at=@At("HEAD"))
    public void onClickSlotStarts(ClickSlotC2SPacket packet, CallbackInfo ci){
        if (EssentialAddonsSettings.stackableShulkersInPlayerInventories)
            EssentialAddonsSettings.inventoryStacking = true;
    }

    @Inject(method="onClickSlot", at=@At("RETURN"))
    public void onClickSlotEnds(ClickSlotC2SPacket packet, CallbackInfo ci){
        if (EssentialAddonsSettings.stackableShulkersInPlayerInventories)
            EssentialAddonsSettings.inventoryStacking = false;
    }

    @Redirect(method = "onSpectatorTeleport", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/network/ServerPlayerEntity;teleport(Lnet/minecraft/server/world/ServerWorld;DDDFF)V"), require = 0)
    private void checkTeleportBlacklist(ServerPlayerEntity playerEntity, ServerWorld targetWorld, double x, double y, double z, float yaw, float pitch, SpectatorTeleportC2SPacket packet) {
        if (EssentialAddonsSettings.cameraModeTeleportBlacklist) {
            UUID entityUUID = Objects.requireNonNull(packet.getTarget(targetWorld)).getUuid();
            if (SubscribeData.subscribeData.get(entityUUID) == null)
                SubscribeData.subscribeData.put(entityUUID, new SubscribeData(false, false));
            else if (SubscribeData.subscribeData.get(Objects.requireNonNull(packet.getTarget(targetWorld)).getUuid()).isSubscribedTeleportBlacklist && !playerEntity.hasPermissionLevel(4)) {
                EssentialAddonsUtils.sendToActionBar(playerEntity, "§6This player has teleporting §cDISABLED");
                return;
            }
        }
        playerEntity.teleport(targetWorld, x, y, z, yaw, pitch);
    }
}