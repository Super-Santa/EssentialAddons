package essentialaddons.mixins.gameRuleSync;

import com.mojang.authlib.GameProfile;
import essentialaddons.feature.GameRuleNetworkHandler;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.network.ServerPlayerEntity;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.UUID;

@Mixin(PlayerManager.class)
public abstract class PlayerManagerMixin {
	@Shadow
	@Nullable
	public abstract ServerPlayerEntity getPlayer(UUID uuid);

	@Inject(method = "addToOperators", at = @At("TAIL"))
	private void onOp(GameProfile profile, CallbackInfo ci) {
		ServerPlayerEntity playerEntity = this.getPlayer(profile.getId());
		if (playerEntity != null) {
			GameRuleNetworkHandler.INSTANCE.updatePlayerStatus(playerEntity);
		}
	}

	@Inject(method = "removeFromOperators", at = @At("TAIL"))
	private void onDeOp(GameProfile profile, CallbackInfo ci) {
		ServerPlayerEntity playerEntity = this.getPlayer(profile.getId());
		if (playerEntity != null) {
			GameRuleNetworkHandler.INSTANCE.updatePlayerStatus(playerEntity);
		}
	}
}
