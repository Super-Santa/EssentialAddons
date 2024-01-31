package essentialaddons.utils;

import carpet.api.settings.CarpetRule;
import carpet.api.settings.Validator;
import essentialaddons.EssentialSettings;
import essentialaddons.feature.GameRuleNetworkHandler;
import net.minecraft.server.command.ServerCommandSource;

public class EssentialValidators {
	public static class GameRuleNoOpValidator extends Validator<Boolean> {
		@Override
		public Boolean validate(ServerCommandSource source, CarpetRule<Boolean> changingRule, Boolean newValue, String userInput) {
			// We set it prematurely, so we can do the following check
			EssentialSettings.gameRuleNonOp = newValue;
			GameRuleNetworkHandler.INSTANCE.getValidPlayers().forEach(GameRuleNetworkHandler.INSTANCE::updatePlayerStatus);
			return newValue;
		}
	}

	public static class GameRuleSyncValidator extends Validator<Boolean> {
		@Override
		public Boolean validate(ServerCommandSource source, CarpetRule<Boolean> changingRule, Boolean newValue, String userInput) {
			EssentialSettings.gameRuleSync = newValue;
			if (newValue) {
				GameRuleNetworkHandler.INSTANCE.getValidPlayers().forEach(GameRuleNetworkHandler.INSTANCE::sendAllRules);
			}
			GameRuleNetworkHandler.INSTANCE.getValidPlayers().forEach(GameRuleNetworkHandler.INSTANCE::updatePlayerStatus);
			return newValue;
		}
	}
}
