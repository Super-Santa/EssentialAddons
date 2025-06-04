package me.supersanta.essential_addons.mixins.feature.broadcast_to_all;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import me.supersanta.essential_addons.EssentialSettings;
import net.minecraft.commands.CommandSourceStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(CommandSourceStack.class)
public class CommandSourceStackMixin {
    @ModifyExpressionValue(
        method = "broadcastToAdmins",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/server/players/PlayerList;isOp(Lcom/mojang/authlib/GameProfile;)Z"
        )
    )
    private boolean shouldBroadcast(boolean original) {
        return original || EssentialSettings.broadcastToAll;
    }
}
