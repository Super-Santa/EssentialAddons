package me.supersanta.essential_addons.mixins.feature.reload_fake_players;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.client.server.IntegratedServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(IntegratedServer.class)
public class IntegratedServerMixin {
	// Fixes an issue with ReloadFakePlayers where
    // if there are more than 8 Fake Players that are
    // reloaded, the player will not be able to join
    // because the vanilla max in single player
    // is 8 players...
	@ModifyReturnValue(
        method = "getMaxPlayers",
        at = @At("RETURN")
    )
	private static int getMaxPlayers(int constant) {
		return 420;
	}
}
