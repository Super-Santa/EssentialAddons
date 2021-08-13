package essentialaddons.mixins.broadcastToAll;

import com.mojang.authlib.GameProfile;
import essentialaddons.EssentialAddonsSettings;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.command.ServerCommandSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ServerCommandSource.class)
public class ServerCommandSourceMixin {
    @Redirect(method = "sendToOps", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/PlayerManager;isOperator(Lcom/mojang/authlib/GameProfile;)Z"))
    private boolean shouldBroadcast(PlayerManager playerManager, GameProfile profile) {
        if (EssentialAddonsSettings.broadcastToAll)
            return true;
        return playerManager.isOperator(profile);
    }
}
