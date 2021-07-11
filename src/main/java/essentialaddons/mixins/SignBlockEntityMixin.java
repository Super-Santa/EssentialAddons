package essentialaddons.mixins;

import essentialaddons.EssentialAddonsSettings;
import net.minecraft.block.entity.SignBlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Hand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(SignBlockEntity.class)
public class SignBlockEntityMixin {

    @Shadow
    private boolean editable;

    @Inject(method = "onActivate", at = @At("HEAD"))
    private void onActivate(ServerPlayerEntity playerEntity, CallbackInfoReturnable<Boolean> callbackInfoReturnable) {
        if (EssentialAddonsSettings.editableSigns && playerEntity.getActiveHand() == Hand.MAIN_HAND && playerEntity.isSneaking()) {
            editable = true;
            playerEntity.openEditSignScreen((SignBlockEntity) (Object) this);
        }
    }
}
