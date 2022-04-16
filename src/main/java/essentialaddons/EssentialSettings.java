package essentialaddons;

import carpet.settings.Rule;
import carpet.settings.Validator;

import static carpet.settings.RuleCategory.*;

public class EssentialSettings {

    private final static String ESSENTIAL = "essential";

    @Rule(
        desc = "Broadcasts all OP messages to everyone",
        options = {"false", "true"},
        category = {ESSENTIAL, SURVIVAL}
    )
    public static boolean broadcastToAll = false;

    @Rule(
        desc = "Allows you to always eat cake",
        options = {"false", "true"},
        category = {ESSENTIAL, EXPERIMENTAL, FEATURE}
    )
    public static boolean cakeAlwaysEat = false;

    @Rule(
        desc = "Restores player location back to original location in survival, similar to the cs script by Kdender",
        extra = "Saves location even after server restart",
        options = {"false", "true"},
        category = {ESSENTIAL, EXPERIMENTAL, SURVIVAL, FEATURE}
    )
    public static boolean cameraModeRestoreLocation = true;

    @Rule(
        desc = "Ports cameraModeSurvivalRestrictions from carpet 1.12 into commandCameraMode",
        extra = "Does not allow you to use /cs if you are in danger",
        options = {"false", "true"},
        category = {ESSENTIAL, EXPERIMENTAL, SURVIVAL, FEATURE}
    )
    public static boolean cameraModeSurvivalRestrictions = false;

    @Rule(
        desc = "Prevents players from teleporting to players in spectator",
        options = {"false", "true"},
        category = {ESSENTIAL, SURVIVAL, COMMAND}
    )
    public static boolean cameraModeTeleportBlacklist = false;

    @Rule(
        desc = "Combines the duration of potions",
        options = {"false", "true"},
        category = {ESSENTIAL, EXPERIMENTAL, FEATURE}
    )
    public static boolean combinePotionDuration = false;

    @Rule(
        desc = "",
        options = {"false", "true", "ops"},
        category = {ESSENTIAL, COMMAND, SURVIVAL, FEATURE}
    )
    public static String commandBackup = "false";

    @Rule(
        desc = "Survival friendly spectator mode, puts the player in and out of spectator mode",
        extra = "Allows for saving location after server reset using rule cameraModeRestoreLocation and adds functionality for cameraModeSurvivalRestrictions",
        options = {"false", "true"},
        category = {ESSENTIAL, COMMAND, EXPERIMENTAL, SURVIVAL, FEATURE}
    )
    public static boolean commandCameraMode = false;

    @Rule(
        desc = "Enables /defuse to be used to stop any tnt from exploding within a given range",
        extra = "Usage: /defuse (Range)",
        validate = {Validator._COMMAND_LEVEL_VALIDATOR.class},
        options = {"ops", "false", "true"},
        category = {ESSENTIAL, COMMAND, CREATIVE}
    )
    public static String commandDefuse = "false";

    @Rule(
        desc = "Allows the player to teleport to different dimensions with a simple command",
        extra = "It will teleport you to specified location, unless unspecified in which case it will teleport you to 0,0",
        validate = {Validator._COMMAND_LEVEL_VALIDATOR.class},
        options = {"ops", "false", "true"},
        category = {ESSENTIAL, COMMAND, CREATIVE}
    )
    public static String commandDimensions = "false";

    @Rule(
        desc = "Allows you to open your enderchest with /enderchest",
        validate = {Validator._COMMAND_LEVEL_VALIDATOR.class},
        options = {"ops", "false", "true"},
        category = {ESSENTIAL, COMMAND, CREATIVE}
    )
    public static String commandEnderChest = "false";

    @Rule(
        desc = "Allows /extinguish to be used to extinguish the player",
        validate = {Validator._COMMAND_LEVEL_VALIDATOR.class},
        options = {"ops", "false", "true"},
        category = {ESSENTIAL, COMMAND}
    )
    public static String commandExtinguish = "false";

    @Rule(
        desc = "Toggles the ability to fly while in survival mode",
        extra = "Using this also disables fall damage",
        validate = {Validator._COMMAND_LEVEL_VALIDATOR.class},
        options = {"ops", "false", "true"},
        category = {ESSENTIAL, COMMAND}
    )
    public static String commandFly = "false";

    @Rule(
        desc = "Allows /gmc, /gms, /gmsp, and /gma to be used",
        validate = {Validator._COMMAND_LEVEL_VALIDATOR.class},
        options = {"ops", "false", "true"},
        category = {ESSENTIAL, COMMAND, CREATIVE}
    )
    public static String commandGM = "false";

    @Rule(
        desc = "Toggles invulnerability",
        extra = "Can be buggy if used while in creative mode",
        validate = {Validator._COMMAND_LEVEL_VALIDATOR.class},
        options = {"ops", "false", "true"},
        category = {ESSENTIAL, COMMAND}
    )
    public static String commandGod = "false";

    @Rule(
        desc = "Allows you to equip items to your head slot using /hat",
        validate = {Validator._COMMAND_LEVEL_VALIDATOR.class},
        options = {"ops", "false", "true"},
        category = {ESSENTIAL, COMMAND, CREATIVE}
    )
    public static String commandHat = "false";

    @Rule(
        desc = "Allows /heal to be used to heal and feed the player",
        validate = {Validator._COMMAND_LEVEL_VALIDATOR.class},
        options = {"ops", "false", "true"},
        category = {ESSENTIAL, COMMAND}
    )
    public static String commandHeal = "false";

    @Rule(
        desc = "Allows you to simulate a lag spike using /lagspike",
        validate = {Validator._COMMAND_LEVEL_VALIDATOR.class},
        options = {"ops", "false", "true"},
        category = {ESSENTIAL, COMMAND, CREATIVE}
    )
    public static String commandLagSpike = "false";

    @Rule(
        desc = "Allows you to do /mods, it lists all the mods running on the server",
        validate = {Validator._COMMAND_LEVEL_VALIDATOR.class},
        options = {"ops", "false", "true"},
        category = {ESSENTIAL, COMMAND}
    )
    public static String commandMods = "ops";

    @Rule(
        desc = "Allows /more to be used to give a full stack of whatever item the player is holding",
        validate = {Validator._COMMAND_LEVEL_VALIDATOR.class},
        options = {"ops", "false", "true"},
        category = {ESSENTIAL, COMMAND}
    )
    public static String commandMore = "false";

    @Rule(
        desc = "Lists other players near you",
        options = {"false", "true"},
        category = {ESSENTIAL, COMMAND, CREATIVE}
    )
    public static boolean commandNear = false;

    @Rule(
        desc = "Toggles night vision",
        validate = {Validator._COMMAND_LEVEL_VALIDATOR.class},
        options = {"ops", "false", "true"},
        category = {ESSENTIAL, COMMAND, CREATIVE}
    )
    public static String commandNightVision = "false";

    @Rule(
        desc = "This allows you to spawn a fake player that doesn't load chunks (They appear on tab list and you are able to teleport to them)",
        validate = {Validator._COMMAND_LEVEL_VALIDATOR.class},
        options = {"ops", "false", "true"},
        category = {ESSENTIAL, COMMAND, EXPERIMENTAL}
    )
    public static String commandPlayerFake = "false";

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
        desc = "Allows all players to use the command /save-all",
        options = {"false", "true"},
        category = {ESSENTIAL, COMMAND, SURVIVAL}
    )
    public static boolean commandPublicSaveAll = false;

    @Rule(
        desc = "Allows anyone to use the /scoreboard command",
        options = {"false", "true"},
        category = {ESSENTIAL, COMMAND, SURVIVAL}
    )
    public static boolean commandPublicScoreboard = false;

    @Rule(
        desc = "Allows all players to change view distance",
        options = {"false", "true"},
        category = {ESSENTIAL, COMMAND, SURVIVAL}
    )
    public static boolean commandPublicViewDistance = false;

    @Rule(
        desc = "Allows you see what region you are in and teleport to a region",
        options = {"false", "true"},
        category = {ESSENTIAL, COMMAND, CREATIVE}
    )
    public static boolean commandRegion = false;

    @Rule(
        desc = "Allows you to rename items with a command",
        options = {"false", "true"},
        category = {ESSENTIAL, COMMAND, CREATIVE}
    )
    public static boolean commandRename = false;

    @Rule(
        desc = "Allows /repair to be used to repair any item the player is holding",
        validate = {Validator._COMMAND_LEVEL_VALIDATOR.class},
        options = {"ops", "false", "true"},
        category = {ESSENTIAL, COMMAND}
    )
    public static String commandRepair = "false";

    @Rule(
        desc = "Toggles strength",
        validate = {Validator._COMMAND_LEVEL_VALIDATOR.class},
        options = {"ops", "false", "true"},
        category = {ESSENTIAL, COMMAND, CREATIVE}
    )
    public static String commandStrength = "false";

    @Rule(
        desc = "Allows the player to teleport between the nether and overworld at equivalent coords",
        validate = {Validator._COMMAND_LEVEL_VALIDATOR.class},
        options = {"ops", "false", "true"},
        category = {ESSENTIAL, COMMAND, CREATIVE}
    )
    public static String commandSwitchDimensions = "false";

    @Rule(
        desc = "Teleports the player up",
        options = {"false", "true"},
        category = {ESSENTIAL, COMMAND, CREATIVE}
    )
    public static boolean commandTop = false;

    @Rule(
        desc = "Allows players to warp using /setwarp and /warp",
        extra = "You are only able to set one warp which will be removed after server restart",
        validate = {Validator._COMMAND_LEVEL_VALIDATOR.class},
        options = {"ops", "false", "true"},
        category = {ESSENTIAL, COMMAND, CREATIVE}
    )
    public static String commandWarp = "false";

    @Rule(
        desc = "Allows you to open a crafting table with /workbench",
        validate = {Validator._COMMAND_LEVEL_VALIDATOR.class},
        options = {"ops", "false", "true"},
        category = {ESSENTIAL, COMMAND, CREATIVE}
    )
    public static String commandWorkbench = "false";

    @Rule(
        desc = "Allows your to edit a sign after its places by right clicking it while sneaking",
        options = {"true", "false"},
        category = {ESSENTIAL, EXPERIMENTAL, FEATURE}
    )
    public static boolean editableSigns = false;

    @Rule(
        desc = "Mining blocks while crouching will put mined blocks straight into your inventory",
        extra = "Same as wholmT's implementation in carpetAddons but works with stackable shulkers, requires players to subscribe to carefulbreak",
        options = {"false", "true"},
        category = {ESSENTIAL, COMMAND, SURVIVAL, FEATURE}
    )
    public static boolean essentialCarefulBreak = false;

    @Rule(
        desc = "Killing mobs while crouching will put dropped items straight into your inventory",
        extra = "requires players to subscribe to carefuldrop",
        options = {"false", "true"},
        category = {ESSENTIAL, COMMAND, SURVIVAL, FEATURE}
    )
    public static boolean essentialCarefulDrop = false;

    @Rule(
            desc = "Automatically drop the fake player inventory on kill",
            options = {"false", "true"},
            category = {ESSENTIAL, SURVIVAL, FEATURE}
    )
    public static boolean fakePlayerDropInventoryOnKill = false;

    @Rule(
        desc = "This allows for survival players to have infinite blocks, food, and enderpearls",
        options = {"false", "true"},
        category = {ESSENTIAL, CREATIVE, EXPERIMENTAL}
    )
    public static boolean infiniteItems = false;

    @Rule(
        desc = "Reimplements minecart boosting",
        options = {"false", "true"},
        category = {ESSENTIAL, FEATURE, EXPERIMENTAL}
    )
    public static boolean minecartBoosting = false;

    @Rule(
        desc = "Automatically reloads the fake players actions after server restart",
        options = {"false", "true"},
        category = {ESSENTIAL, SURVIVAL, FEATURE, EXPERIMENTAL}
    )
    public static boolean reloadFakePlayerActions = false;

    @Rule(
        desc = "Automatically respawns fake players on server restart",
        options = {"false", "true"},
        category = {ESSENTIAL, SURVIVAL, FEATURE}
    )
    public static boolean reloadFakePlayers = false;

    @Rule(
        desc = "Removes all item entities after set amount of item entities is reached in a world, set to 0 to disable",
        options = {"0", "200", "500", "1000"},
        strict = false,
        category = {ESSENTIAL, CREATIVE, FEATURE}
    )
    public static int removeItemEntitiesAfterThreshold = 0;

    @Rule(
        desc = "Removes the warning 'Mismatch in destroy block pos...' in console and logs",
        options = {"false", "true"},
        category = {ESSENTIAL, SURVIVAL, FEATURE}
    )
    public static boolean removeWarnMismatchBlockPos = false;

    @Rule(
        desc = "Removes the warning 'Fetching packet for removed entity...' in console and logs",
        options = {"false", "true"},
        category = {ESSENTIAL, SURVIVAL, FEATURE}
    )
    public static boolean removeWarnRemovedEntity = false;

    @Rule(
        desc = "Removes all xp entities after set amount of xp entities is reached in a world, set to 0 to disable",
        options = {"0", "100", "250", "500"},
        strict = false,
        category = {ESSENTIAL, CREATIVE, FEATURE}
    )
    public static int removeXpEntitiesAfterThreshold = 0;

    @Rule(
        desc = "Allows you to put shulker boxes inside of other shulkers",
        options = {"false", "true"},
        category = {ESSENTIAL, EXPERIMENTAL, FEATURE}
    )
    public static boolean shulkerSception = false;

    @Rule(
        desc = "Stops potion effects from ticking when in spectator",
        options = {"false", "true"},
        category = {ESSENTIAL, SURVIVAL, FEATURE}
    )
    public static boolean spectatorPotionNoCountdown = false;

    @Rule(
        desc = "Fixes stacked shulkers overloading comparators",
        options = {"false", "true"},
        category = {ESSENTIAL, SURVIVAL, FEATURE}
    )
    public static boolean stackableShulkerComparatorOverloadFix = false;

    @Rule(
        desc = "Shulker boxes stack in player inventories, this is not the same as `stackableShulkerBoxes` in Carpet. This will always allow you to stack shulkers in your inventory.",
        extra = "Disable all other `stackableShulker` rules (unless you are using stackableShulkersWithItems)",
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
}

