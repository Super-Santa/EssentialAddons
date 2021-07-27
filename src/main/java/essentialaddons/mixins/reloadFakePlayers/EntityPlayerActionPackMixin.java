package essentialaddons.mixins.reloadFakePlayers;

import carpet.helpers.EntityPlayerActionPack;
import essentialaddons.helpers.ReloadFakePlayers;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EntityPlayerActionPack.class)
public class EntityPlayerActionPackMixin {

    @Shadow @Final private ServerPlayerEntity player;

    @Inject(method = "setForward", at = @At("HEAD"), remap = false)
    public void setForward(float value, CallbackInfoReturnable<EntityPlayerActionPack> cir) {
        ReloadFakePlayers.addMovement(player.getEntityName(), 8, value);
    }
    @Inject(method = "setStrafing", at = @At("HEAD"), remap = false)
    public void setStrafing(float value, CallbackInfoReturnable<EntityPlayerActionPack> cir) {
        ReloadFakePlayers.addMovement(player.getEntityName(), 9, value);
    }
}
