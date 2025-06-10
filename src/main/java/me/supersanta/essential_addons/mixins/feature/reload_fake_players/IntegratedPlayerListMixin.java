package me.supersanta.essential_addons.mixins.feature.reload_fake_players;

import net.minecraft.client.server.IntegratedPlayerList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(IntegratedPlayerList.class)
public class IntegratedPlayerListMixin {
	/**
	 * Fixes an issue with ReloadFakePlayers where
	 * if there are more than 8 Fake Players that are
	 * reloaded the player will not be able to join
	 * because the vanilla max in single player
	 * is 8 players... I have no idea why.
	 */
	@ModifyConstant(method = "<init>", constant = @Constant(intValue = 8))
	private static int getMaxPlayers(int constant) {
		return 420;
	}
}
