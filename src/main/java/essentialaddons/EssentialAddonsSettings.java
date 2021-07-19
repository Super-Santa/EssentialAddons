package essentialaddons;

import carpet.CarpetSettings;
import carpet.settings.ParsedRule;
import carpet.settings.Rule;
import carpet.settings.Validator;
import carpet.utils.Messenger;

import static carpet.settings.RuleCategory.*;

public class EssentialAddonsSettings {

    public static boolean inventoryStacking = false;

    private final static String ESSENTIAL = "essential";

    @Rule(
            desc = "Toggles the ability to fly while in survival mode",
            extra = "Using this also disables fall damage",
            validate = {Validator._COMMAND_LEVEL_VALIDATOR.class},
            options = {"ops", "false", "true"},
            category = {ESSENTIAL, COMMAND}
    )
    public static String commandFly = "false";

    @Rule(
            desc = "Allows /repair to be used to repair any item the player is holding",
            validate = {Validator._COMMAND_LEVEL_VALIDATOR.class},
            options = {"ops", "false", "true"},
            category = {ESSENTIAL, COMMAND}
    )
    public static String commandRepair = "false";

    @Rule(
            desc = "Allows /gmc, /gms, /gmsp, and /gma to be used",
            validate = {Validator._COMMAND_LEVEL_VALIDATOR.class},
            options = {"ops", "false", "true"},
            category = {ESSENTIAL, COMMAND, CREATIVE}
    )
    public static String commandGM = "false";

    @Rule(
            desc = "Allows /heal to be used to heal and feed the player",
            validate = {Validator._COMMAND_LEVEL_VALIDATOR.class},
            options = {"ops", "false", "true"},
            category = {ESSENTIAL, COMMAND}
    )
    public static String commandHeal = "false";

    @Rule(
            desc = "Allows /extinguish to be used to extinguish the player",
            validate = {Validator._COMMAND_LEVEL_VALIDATOR.class},
            options = {"ops", "false", "true"},
            category = {ESSENTIAL, COMMAND}
    )
    public static String commandExtinguish = "false";

    @Rule(
            desc = "Toggles invulnerability",
            extra = "Can be buggy if used while in creative mode",
            validate = {Validator._COMMAND_LEVEL_VALIDATOR.class},
            options = {"ops", "false", "true"},
            category = {ESSENTIAL, COMMAND}
    )
    public static String commandGod = "false";

    @Rule(
            desc = "Enables /defuse to be used to stop any tnt from exploding within a given range",
            extra = "Usage: /defuse (Range)",
            validate = {Validator._COMMAND_LEVEL_VALIDATOR.class},
            options = {"ops", "false", "true"},
            category = {ESSENTIAL, COMMAND, CREATIVE}
    )
    public static String commandDefuse = "false";

    @Rule(
            desc = "Allows /more to be used to give a full stack of whatever item the player is holding",
            validate = {Validator._COMMAND_LEVEL_VALIDATOR.class},
            options = {"ops", "false", "true"},
            category = {ESSENTIAL, COMMAND}
    )
    public static String commandMore = "false";

    @Rule(
            desc = "Toggles strength",
            validate = {Validator._COMMAND_LEVEL_VALIDATOR.class},
            options = {"ops", "false", "true"},
            category = {ESSENTIAL, COMMAND, CREATIVE}
    )
    public static String commandStrength = "false";

    @Rule(
            desc = "Toggles night vision",
            validate = {Validator._COMMAND_LEVEL_VALIDATOR.class},
            options = {"ops", "false", "true"},
            category = {ESSENTIAL, COMMAND, CREATIVE}
    )
    public static String commandNightVision = "false";

    @Rule(
            desc = "Allows the player to teleport to different dimensions with a simple command",
            extra = "It will always teleport you to 0,0 in said dimension",
            validate = {Validator._COMMAND_LEVEL_VALIDATOR.class},
            options = {"ops", "false", "true"},
            category = {ESSENTIAL, COMMAND, CREATIVE}
    )
    public static String commandDimensions = "false";

    @Rule(
            desc = "Allows the player to teleport between the nether and overworld at equivalent coords",
            validate = {Validator._COMMAND_LEVEL_VALIDATOR.class},
            options = {"ops", "false", "true"},
            category = {ESSENTIAL, COMMAND, CREATIVE}
    )
    public static String commandSwitchDimensions = "false";

    @Rule(
            desc = "Allows players to warp using /setwarp and /warp",
            extra = "You are only able to set one warp which will be removed after server restart",
            validate = {Validator._COMMAND_LEVEL_VALIDATOR.class},
            options = {"ops", "false", "true"},
            category = {ESSENTIAL, COMMAND, CREATIVE}
    )
    public static String commandWarp = "false";

    @Rule(
            desc = "Allows your to edit a sign after its places by right clicking it while sneaking",
            options = {"true", "false"},
            category = {ESSENTIAL, COMMAND, EXPERIMENTAL, FEATURE}
    )
    public static boolean editableSigns = false;

    @Rule(
            desc = "Survival friendly spectator mode, puts the player in and out of spectator mode",
            extra = "Allows for saving location after server reset using rule cameraModeRestoreLocation and adds functionality for cameraModeSurvivalRestrictions",
            options = {"false", "true"},
            category = {ESSENTIAL, COMMAND, EXPERIMENTAL, SURVIVAL, FEATURE}
    )
    public static boolean commandCameraMode = false;

    @Rule(
            desc = "Ports cameraModeSurvivalRestrictions from carpet 1.12 into commandCameraMode",
            extra = "Does not allow you to use /cs if you are in danger",
            options = {"false", "true"},
            category = {ESSENTIAL, EXPERIMENTAL, SURVIVAL, FEATURE}
    )
    public static boolean cameraModeSurvivalRestrictions = false;

    @Rule(
            desc = "Restores player location back to original location in survival, similar to the cs script by Kdender",
            extra = "Saves location even after server restart",
            options = {"false", "true"},
            category = {ESSENTIAL, EXPERIMENTAL, SURVIVAL, FEATURE}
    )
    public static boolean cameraModeRestoreLocation = true;

    @Rule(
            desc = "Allows anyone to use the /kick command",
            options = {"false", "true"},
            category = {ESSENTIAL, COMMAND, SURVIVAL}
    )
    public static boolean commandPublicKick = false;

    @Rule(
            desc = "Allows anyone to use the /op command",
            options = {"false", "true"},
            category = {ESSENTIAL, COMMAND, CREATIVE}
    )
    public static boolean commandPublicOp = false;

    @Rule(
            desc = "Allows anyone to use the /scoreboard command",
            options = {"false", "true"},
            category = {ESSENTIAL, COMMAND, SURVIVAL}
    )
    public static boolean commandPublicScoreboard = false;

    @Rule(
            desc = "Allows you to always eat cake",
            options = {"false", "true"},
            category = {ESSENTIAL, EXPERIMENTAL, FEATURE}
    )
    public static boolean cakeAlwaysEat = false;

    @Rule(
            desc = "Allows you to open your enderchest with /enderchest",
            validate = {Validator._COMMAND_LEVEL_VALIDATOR.class},
            options = {"ops", "false", "true"},
            category = {ESSENTIAL, COMMAND, CREATIVE}
    )
    public static String commandEnderChest = "false";

    @Rule(
            desc = "Allows all players to change view distance",
            options = {"false", "true"},
            category = {ESSENTIAL, COMMAND, CREATIVE}
    )
    public static boolean commandPublicViewDistance = false;

    @Rule(
            desc = "Combines the duration of consumed potions",
            options = {"false", "true"},
            category = {ESSENTIAL, EXPERIMENTAL, FEATURE}
    )
    public static boolean combinePotionDuration = false;

    @Rule(
            desc = "Shulker boxes stack in player inventories",
            extra = "Enable tweakEmptyShulkerBoxesStack in tweakeroo on the client",
            options = {"false", "true"},
            category = {ESSENTIAL, EXPERIMENTAL, FEATURE}
    )
    public static boolean stackableShulkersInPlayerInventories = false;

    @Rule(
            desc = "Shulker boxes stack with items inside will stack with other shulkers with the same items",
            extra = "This rule requires stackableShulkersInPlayerInventories to be enabled",
            options = {"false", "true"},
            category = {ESSENTIAL, EXPERIMENTAL, FEATURE}
    )
    public static boolean stackableShulkersWithItems = false;

    @Rule(
            desc = "Allows you to put shulker boxes inside of other shulkers",
            options = {"false", "true"},
            category = {ESSENTIAL, EXPERIMENTAL, FEATURE}
    )
    public static boolean shulkerSception = false;

    @Rule(
            desc = "Mining blocks while crouching will put mined blocks striaght into your inventory, THIS DOES NOT WORK WITH CARPET-ADDONS INSTALLED",
            extra = "Same as wholmT's implementation in carpetAddons but works with stackable shulkers, requires players to subscribe to carefulbreak",
            options = {"false", "true"},
            category = {ESSENTIAL, SURVIVAL, FEATURE}
    )
    public static boolean essentialCarefulBreak = false;

    @Rule(
            desc = "This allows for better shulker box stacking on the floor",
            extra = "This rule is for using stackable shulkers with hopper optimisations, any other cases you should use stackableShulkersInPlayerInventories",
            options = {"false", "true"},
            category = {ESSENTIAL, SURVIVAL, EXPERIMENTAL}
    )
    public static boolean betterStackableShulkers = false;

    @Rule(
            desc = "This allows for survival players to have infinite blocks, food, and enderpearls",
            options = {"false", "true"},
            category = {ESSENTIAL, CREATIVE, EXPERIMENTAL}
    )
    public static boolean infiniteItems = false;

    @Rule(
            desc = "Tries to kill experience orbs when server is over set mspt, set to 0 to disable",
            options = {"0", "50", "75", "100"},
            strict = false,
            category = {ESSENTIAL, CREATIVE, FEATURE}
    )
    public static int removeXpEntitiesIfMsptOver = 0;
}


