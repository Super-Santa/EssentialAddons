package me.supersanta.essential_addons.mixins.feature.stackable_shulkers;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import me.supersanta.essential_addons.EssentialSettings;
import me.supersanta.essential_addons.utils.EssentialUtilsKt;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.protocol.game.ClientboundContainerSetSlotPacket;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ClientboundContainerSetSlotPacket.class)
public class ClientboundContainerSetSlotPacketMixin {
    @ModifyExpressionValue(
        method = "<init>(IIILnet/minecraft/world/item/ItemStack;)V",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/item/ItemStack;copy()Lnet/minecraft/world/item/ItemStack;"
        )
    )
    private ItemStack onCopyItemStack(ItemStack original) {
        if (EssentialSettings.stackableShulkersInPlayerInventories && EssentialUtilsKt.isShulkerBox(original)) {
            original.set(DataComponents.MAX_STACK_SIZE, 64);
        }
        return original;
    }
}
