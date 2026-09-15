package me.supersanta.essential_addons.mixins.feature.careful_drop;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import me.supersanta.essential_addons.feature.careful_break.CarefulDrop;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Inventory.class)
public class InventoryMixin {
    @WrapOperation(
        method = "dropAll",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/entity/player/Player;createItemStackToDrop(Lnet/minecraft/world/item/ItemStack;ZZ)Lnet/minecraft/world/entity/item/ItemEntity;"
        )
    )
    private ItemEntity onDropAllItems(
        Player instance,
        ItemStack stack,
        boolean randomly,
        boolean thrownFromHand,
        Operation<ItemEntity> original
    ) {
        CarefulDrop.carefullyDrop(instance.getLastAttacker(), stack, remaining -> {
            original.call(instance, remaining, randomly, thrownFromHand);
        });
        return null;
    }
}
