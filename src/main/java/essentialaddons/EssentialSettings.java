package essentialaddons;


import carpet.api.settings.Rule;
import carpet.api.settings.Validators;
import essentialaddons.utils.EssentialValidators;

import static carpet.api.settings.RuleCategory.*;


public class EssentialSettings {
	private final static String ESSENTIAL = "essential";

	@Rule(
		categories = {ESSENTIAL, SURVIVAL}
	)
	public static boolean broadcastToAll = false;

	@Rule(
		categories = {ESSENTIAL, EXPERIMENTAL, FEATURE}
	)
	public static boolean cakeAlwaysEat = false;

	@Rule(
		categories = {ESSENTIAL, EXPERIMENTAL, SURVIVAL, FEATURE}
	)
	public static boolean cameraModeRestoreLocation = true;

	@Rule(
		categories = {ESSENTIAL, EXPERIMENTAL, SURVIVAL, FEATURE}
	)
	public static boolean cameraModeSurvivalRestrictions = false;

	@Rule(
		categories = {ESSENTIAL, SURVIVAL, COMMAND}
	)
	public static boolean cameraModeTeleportBlacklist = false;

	@Rule(
		categories = {ESSENTIAL, SURVIVAL, COMMAND}
	)
	public static boolean cameraModeTeamTeleportBlacklist = false;

	@Rule(
		categories = {ESSENTIAL, EXPERIMENTAL, FEATURE}
	)
	public static boolean combinePotionDuration = false;

	@Rule(
		categories = {ESSENTIAL, COMMAND, SURVIVAL, FEATURE}
	)
	public static String commandBackup = "false";

	@Rule(
		categories = {ESSENTIAL, COMMAND, EXPERIMENTAL, SURVIVAL, FEATURE}
	)
	public static String commandCameraMode = "false";

	@Rule(
		categories = {ESSENTIAL, COMMAND, CREATIVE}
	)
	public static String commandDefuse = "false";

	@Rule(
		categories = {ESSENTIAL, COMMAND, CREATIVE}
	)
	public static String commandDimensions = "false";

	@Rule(
		categories = {ESSENTIAL, COMMAND, CREATIVE}
	)
	public static String commandEnderChest = "false";

	@Rule(
		categories = {ESSENTIAL, COMMAND}
	)
	public static String commandExtinguish = "false";

	@Rule(
		categories = {ESSENTIAL, COMMAND}
	)
	public static String commandFly = "false";

	@Rule(
		categories = {ESSENTIAL, COMMAND, CREATIVE}
	)
	public static String commandGM = "false";

	@Rule(
		categories = {ESSENTIAL, COMMAND}
	)
	public static String commandGod = "false";

	@Rule(
		categories = {ESSENTIAL, COMMAND, CREATIVE}
	)
	public static String commandHat = "false";

	@Rule(
		categories = {ESSENTIAL, COMMAND}
	)
	public static String commandHeal = "false";

	@Rule(
		categories = {ESSENTIAL, COMMAND, CREATIVE}
	)
	public static String commandLagSpike = "false";

	@Rule(
		categories = {ESSENTIAL, COMMAND}
	)
	public static String commandMods = "ops";

	@Rule(
		categories = {ESSENTIAL, COMMAND}
	)
	public static String commandMore = "false";

	@Rule(
		categories = {ESSENTIAL, COMMAND, CREATIVE}
	)
	public static String commandNear = "false";

	@Rule(
		categories = {ESSENTIAL, COMMAND, CREATIVE}
	)
	public static String commandNightVision = "false";

	@Rule(
		categories = {ESSENTIAL, COMMAND, EXPERIMENTAL}
	)
	public static String commandGhostPlayer = "false";

	@Rule(
		categories = {ESSENTIAL, COMMAND, SURVIVAL}
	)
	public static boolean commandPublicKick = false;

	@Rule(
		categories = {ESSENTIAL, COMMAND, CREATIVE}
	)
	public static boolean commandPublicOp = false;

	@Rule(
		categories = {ESSENTIAL, COMMAND, SURVIVAL}
	)
	public static boolean commandPublicSaveAll = false;

	@Rule(
		categories = {ESSENTIAL, COMMAND, SURVIVAL}
	)
	public static boolean commandPublicScoreboard = false;

	@Rule(
		categories = {ESSENTIAL, COMMAND, SURVIVAL}
	)
	public static boolean commandPublicTeam = false;

	@Rule(
		categories = {ESSENTIAL, COMMAND, SURVIVAL}
	)
	public static boolean commandPublicViewDistance = false;

	@Rule(
		categories = {ESSENTIAL, COMMAND, CREATIVE}
	)
	public static boolean commandRename = false;

	@Rule(
		categories = {ESSENTIAL, COMMAND}
	)
	public static String commandRepair = "false";

	@Rule(
		categories = {ESSENTIAL, COMMAND, CREATIVE}
	)
	public static String commandStrength = "false";

	@Rule(
		categories = {ESSENTIAL, COMMAND, CREATIVE}
	)
	public static String commandSwitchDimensions = "false";

	@Rule(
		categories = {ESSENTIAL, COMMAND, CREATIVE}
	)
	public static boolean commandTop = false;

	@Rule(
		categories = {ESSENTIAL, COMMAND, CREATIVE}
	)
	public static String commandWarp = "false";

	@Rule(
		categories = {ESSENTIAL, COMMAND, CREATIVE}
	)
	public static String commandWorkbench = "false";


	@Rule(
		categories = {ESSENTIAL, COMMAND, SURVIVAL, FEATURE}
	)
	public static boolean essentialCarefulBreak = false;

	@Rule(
		categories = {ESSENTIAL, COMMAND, SURVIVAL, FEATURE}
	)
	public static boolean essentialCarefulDrop = false;

	@Rule(
		categories = {ESSENTIAL, SURVIVAL, FEATURE}
	)
	public static boolean fakePlayerDropInventoryOnKill = false;

	@Rule(
		categories = {ESSENTIAL, EXPERIMENTAL, FEATURE},
		validators = EssentialValidators.GameRuleNoOpValidator.class
	)
	public static boolean gameRuleNonOp = false;

	@Rule(
		categories = {ESSENTIAL, EXPERIMENTAL, FEATURE},
		validators = EssentialValidators.GameRuleSyncValidator.class
	)
	public static boolean gameRuleSync = false;

	@Rule(
		categories = {ESSENTIAL, CREATIVE, EXPERIMENTAL}
	)
	public static boolean infiniteItems = false;

	@Rule(
		options = {"256", "1024"},
		strict = false,
		categories = {ESSENTIAL, FEATURE, EXPERIMENTAL},
		validators = Validators.NonNegativeNumber.class
	)
	public static int maxChatLength = 256;

	@Rule(
		categories = {ESSENTIAL, FEATURE, EXPERIMENTAL}
	)
	public static boolean minecartBoosting = false;

	@Rule(
		categories = {ESSENTIAL, FEATURE, SURVIVAL}
	)
	public static boolean phantomsObeyMobcaps = false;

	@Rule(
		categories = {ESSENTIAL, SURVIVAL, FEATURE, EXPERIMENTAL}
	)
	public static boolean reloadFakePlayerActions = false;

	@Rule(
		categories = {ESSENTIAL, SURVIVAL, FEATURE}
	)
	public static boolean reloadFakePlayers = false;

	@Rule(
		options = {"0", "200", "500", "1000"},
		strict = false,
		categories = {ESSENTIAL, CREATIVE, FEATURE}
	)
	public static int removeItemEntitiesAfterThreshold = 0;

	@Rule(
		categories = {ESSENTIAL, SURVIVAL, FEATURE}
	)
	public static boolean removeWarnOversizedChunk = false;

	@Rule(
		categories = {ESSENTIAL, SURVIVAL, FEATURE}
	)
	public static boolean removeWarnMismatchBlockPos = false;

	@Rule(
		categories = {ESSENTIAL, SURVIVAL, FEATURE}
	)
	public static boolean removeWarnRemovedEntity = false;

	@Rule(
		options = {"0", "100", "250", "500"},
		strict = false,
		categories = {ESSENTIAL, CREATIVE, FEATURE}
	)
	public static int removeXpEntitiesAfterThreshold = 0;

	@Rule(
		categories = {ESSENTIAL, EXPERIMENTAL, FEATURE}
	)
	public static boolean shulkerSception = false;

	@Rule(
		categories = {ESSENTIAL, SURVIVAL, FEATURE}
	)
	public static boolean spectatorPotionNoCountdown = false;

	@Rule(
		categories = {ESSENTIAL, SURVIVAL, FEATURE}
	)
	public static boolean stackableShulkerComparatorOverloadFix = false;

	@Rule(
		categories = {ESSENTIAL, EXPERIMENTAL, FEATURE}
	)
	public static boolean stackableShulkersInPlayerInventories = false;

	@Rule(
		categories = {ESSENTIAL, EXPERIMENTAL, FEATURE}
	)
	public static boolean stackableShulkersWithItems = false;

	@Rule(
		categories = {ESSENTIAL, CREATIVE, FEATURE}
	)
	public static boolean structureBlockKillEntities = false;

	@Rule(
		categories = {ESSENTIAL, CREATIVE, FEATURE}
	)
	public static boolean structureBlockReplaceFluids = false;
}

