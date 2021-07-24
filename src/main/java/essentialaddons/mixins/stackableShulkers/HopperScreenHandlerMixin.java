package essentialaddons.mixins.stackableShulkers;

import essentialaddons.EssentialAddonsSettings;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.screen.HopperScreenHandler;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(HopperScreenHandler.class)
public class HopperScreenHandlerMixin {

    @Inject(method="canUse", at=@At("HEAD"))
    public void canUse(PlayerEntity player, CallbackInfoReturnable<Boolean> cir){
        if (EssentialAddonsSettings.stackableShulkerLithiumFix)
            EssentialAddonsSettings.hopperOpen = true;
    }

    @Inject(method="close", at=@At("HEAD"))
    public void close(PlayerEntity player, CallbackInfo ci){
        if (EssentialAddonsSettings.stackableShulkerLithiumFix)
            EssentialAddonsSettings.hopperOpen = false;
    }
}
