package me.supersanta.essential_addons.mixins.feature.infinite_items;

import me.supersanta.essential_addons.EssentialSettings;
import net.casual.arcade.utils.PlayerUtils;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemStack.class)
public class ItemStackMixin {
    @Inject(
        method = "consume",
        at = @At("HEAD"),
        cancellable = true
    )
    private void onConsume(int amount, LivingEntity entity, CallbackInfo ci) {
        if (entity instanceof ServerPlayer player && EssentialSettings.infiniteItems) {
            PlayerUtils.updateSelectedSlot(player);
            ci.cancel();
        }
    }
}
