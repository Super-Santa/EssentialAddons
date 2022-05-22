package essentialaddons.utils;

import carpet.settings.ParsedRule;
import carpet.settings.Validator;
import essentialaddons.EssentialSettings;
import essentialaddons.feature.GameRuleNetworkHandler;
import net.minecraft.server.command.ServerCommandSource;

public class Validators {
	public static class GameRuleNoOpValidator extends Validator<Boolean> {
		@Override
		public Boolean validate(ServerCommandSource source, ParsedRule<Boolean> currentRule, Boolean newValue, String string) {
			// We set it prematurely, so we can do the following check
			EssentialSettings.gameRuleNonOp = newValue;
			GameRuleNetworkHandler.INSTANCE.getValidPlayers().forEach(GameRuleNetworkHandler.INSTANCE::updatePlayerStatus);
			return newValue;
		}
	}

	public static class GameRuleSyncValidator extends Validator<Boolean> {
		@Override
		public Boolean validate(ServerCommandSource source, ParsedRule<Boolean> currentRule, Boolean newValue, String string) {
			EssentialSettings.gameRuleSync = newValue;
			if (newValue) {
				GameRuleNetworkHandler.INSTANCE.getValidPlayers().forEach(GameRuleNetworkHandler.INSTANCE::sendAllRules);
			}
			GameRuleNetworkHandler.INSTANCE.getValidPlayers().forEach(GameRuleNetworkHandler.INSTANCE::updatePlayerStatus);
			return newValue;
		}
	}
}
