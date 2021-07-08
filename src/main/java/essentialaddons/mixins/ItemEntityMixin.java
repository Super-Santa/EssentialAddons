package essentialaddons.mixins;

import essentialaddons.EssentialAddonsSettings;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemEntity.class)
public class ItemEntityMixin {

    @Inject(method = "onPlayerCollision", at=@At("HEAD"))
    public void onPlayerCollisionStarts(PlayerEntity player, CallbackInfo ci){
        EssentialAddonsSettings.inventoryStacking = true;
    }

    @Inject(method = "onPlayerCollision", at=@At("RETURN"))
    public void onPlayerCollisionEnds(PlayerEntity player, CallbackInfo ci){
        EssentialAddonsSettings.inventoryStacking = false;
    }

}
