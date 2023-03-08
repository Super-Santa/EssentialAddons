package essentialaddons.mixins.mojank;

import net.minecraft.server.network.ServerPlayerInteractionManager;
import net.minecraft.world.GameMode;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ServerPlayerInteractionManager.class)
public class ServerPlayerInteractionManagerMixin {
	//#if MC >= 11903
	@Redirect(method = "changeGameMode", at = @At(value = "FIELD", target = "Lnet/minecraft/server/network/ServerPlayerInteractionManager;previousGameMode:Lnet/minecraft/world/GameMode;"))
	private GameMode getGameMode(ServerPlayerInteractionManager instance) {
		return instance.getGameMode();
	}
	//#endif
}
