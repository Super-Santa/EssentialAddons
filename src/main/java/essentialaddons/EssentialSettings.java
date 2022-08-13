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
        categories = {ESSENTIAL, EXPERIMENTAL, FEATURE}
    )
    public static boolean combinePotionDuration = false;

    @Rule(
        options = {"false", "true", "ops"},
        categories = {ESSENTIAL, COMMAND, SURVIVAL, FEATURE}
    )
    public static String commandBackup = "false";

    @Rule(
        categories = {ESSENTIAL, COMMAND, EXPERIMENTAL, SURVIVAL, FEATURE}
    )
    public static boolean commandCameraMode = false;

    @Rule(
        validators = {carpet.api.settings.Validators.CommandLevel.class},
        options = {"ops", "false", "true"},
        categories = {ESSENTIAL, COMMAND, CREATIVE}
    )
    public static String commandDefuse = "false";

    @Rule(
        validators = {Validators.CommandLevel.class},
        options = {"ops", "false", "true"},
        categories = {ESSENTIAL, COMMAND, CREATIVE}
    )
    public static String commandDimensions = "false";

    @Rule(
        validators = {Validators.CommandLevel.class},
        options = {"ops", "false", "true"},
        categories = {ESSENTIAL, COMMAND, CREATIVE}
    )
    public static String commandEnderChest = "false";

    @Rule(
        validators = {Validators.CommandLevel.class},
        options = {"ops", "false", "true"},
        categories = {ESSENTIAL, COMMAND}
    )
    public static String commandExtinguish = "false";

    @Rule(
        validators = {Validators.CommandLevel.class},
        options = {"ops", "false", "true"},
        categories = {ESSENTIAL, COMMAND}
    )
    public static String commandFly = "false";

    @Rule(
        validators = {Validators.CommandLevel.class},
        options = {"ops", "false", "true"},
        categories = {ESSENTIAL, COMMAND, CREATIVE}
    )
    public static String commandGM = "false";

    @Rule(
        validators = {Validators.CommandLevel.class},
        options = {"ops", "false", "true"},
        categories = {ESSENTIAL, COMMAND}
    )
    public static String commandGod = "false";

    @Rule(
        validators = {Validators.CommandLevel.class},
        options = {"ops", "false", "true"},
        categories = {ESSENTIAL, COMMAND, CREATIVE}
    )
    public static String commandHat = "false";

    @Rule(
        validators = {Validators.CommandLevel.class},
        options = {"ops", "false", "true"},
        categories = {ESSENTIAL, COMMAND}
    )
    public static String commandHeal = "false";

    @Rule(
        validators = {Validators.CommandLevel.class},
        options = {"ops", "false", "true"},
        categories = {ESSENTIAL, COMMAND, CREATIVE}
    )
    public static String commandLagSpike = "false";

    @Rule(
        validators = {Validators.CommandLevel.class},
        options = {"ops", "false", "true"},
        categories = {ESSENTIAL, COMMAND}
    )
    public static String commandMods = "ops";

    @Rule(
        validators = {Validators.CommandLevel.class},
        options = {"ops", "false", "true"},
        categories = {ESSENTIAL, COMMAND}
    )
    public static String commandMore = "false";

    @Rule(
        categories = {ESSENTIAL, COMMAND, CREATIVE}
    )
    public static boolean commandNear = false;

    @Rule(
        validators = {Validators.CommandLevel.class},
        options = {"ops", "false", "true"},
        categories = {ESSENTIAL, COMMAND, CREATIVE}
    )
    public static String commandNightVision = "false";

    @Rule(
        validators = {Validators.CommandLevel.class},
        options = {"ops", "false", "true"},
        categories = {ESSENTIAL, COMMAND, EXPERIMENTAL}
    )
    public static String commandPlayerFake = "false";

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
    public static boolean commandPublicViewDistance = false;

    @Rule(
        categories = {ESSENTIAL, COMMAND, CREATIVE}
    )
    public static boolean commandRegion = false;

    @Rule(
        categories = {ESSENTIAL, COMMAND, CREATIVE}
    )
    public static boolean commandRename = false;

    @Rule(
        validators = {Validators.CommandLevel.class},
        options = {"ops", "false", "true"},
        categories = {ESSENTIAL, COMMAND}
    )
    public static String commandRepair = "false";

    @Rule(
        validators = {Validators.CommandLevel.class},
        options = {"ops", "false", "true"},
        categories = {ESSENTIAL, COMMAND, CREATIVE}
    )
    public static String commandStrength = "false";

    @Rule(
        validators = {Validators.CommandLevel.class},
        options = {"ops", "false", "true"},
        categories = {ESSENTIAL, COMMAND, CREATIVE}
    )
    public static String commandSwitchDimensions = "false";

    @Rule(
        categories = {ESSENTIAL, COMMAND, CREATIVE}
    )
    public static boolean commandTop = false;

    @Rule(
        validators = {Validators.CommandLevel.class},
        options = {"ops", "false", "true"},
        categories = {ESSENTIAL, COMMAND, CREATIVE}
    )
    public static String commandWarp = "false";

    @Rule(
        validators = {Validators.CommandLevel.class},
        options = {"ops", "false", "true"},
        categories = {ESSENTIAL, COMMAND, CREATIVE}
    )
    public static String commandWorkbench = "false";

    @Rule(
        categories = {ESSENTIAL, EXPERIMENTAL, FEATURE}
    )
    public static boolean editableSigns = false;

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
        categories = {ESSENTIAL, FEATURE, EXPERIMENTAL},
        strict = false,
        validators = Validators.NonNegativeNumber.class
    )
    public static int maxChatLength = 256;

    @Rule(
        categories = {ESSENTIAL, FEATURE, EXPERIMENTAL}
    )
    public static boolean minecartBoosting = false;

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
}

