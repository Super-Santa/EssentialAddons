package essentialaddons;

//#if MC >= 11900
import carpet.api.settings.Rule;
import carpet.api.settings.Validators;
//#else
//$$import carpet.settings.Rule;
//$$import carpet.settings.Validator;
//#endif
import essentialaddons.utils.EssentialValidators;

//#if MC >= 11900
import static carpet.api.settings.RuleCategory.*;
//#else
//$$import static carpet.settings.RuleCategory.*;
//#endif

public class EssentialSettings {
    private final static String ESSENTIAL = "essential";

    @Rule(
        //#if MC >= 11900
        categories = {ESSENTIAL, SURVIVAL}
        //#else
        //$$desc = "Broadcasts all OP messages to everyone",
        //$$category = {ESSENTIAL, SURVIVAL}
        //#endif
    )
    public static boolean broadcastToAll = false;

    @Rule(
        //#if MC >= 11900
        categories = {ESSENTIAL, EXPERIMENTAL, FEATURE}
        //#else
        //$$desc = "Allows you to always eat cake",
        //$$category = {ESSENTIAL, EXPERIMENTAL, FEATURE}
        //#endif
    )
    public static boolean cakeAlwaysEat = false;

    @Rule(
        //#if MC >= 11900
        categories = {ESSENTIAL, EXPERIMENTAL, SURVIVAL, FEATURE}
        //#else
        //$$desc = "Restores player location back to original location in survival, similar to the cs script by Kdender",
        //$$extra = "Saves location even after server restart",
        //$$category = {ESSENTIAL, EXPERIMENTAL, SURVIVAL, FEATURE}
        //#endif
    )
    public static boolean cameraModeRestoreLocation = true;

    @Rule(
        //#if MC >= 11900
        categories = {ESSENTIAL, EXPERIMENTAL, SURVIVAL, FEATURE}
        //#else
        //$$desc = "Ports cameraModeSurvivalRestrictions from carpet 1.12 into commandCameraMode",
        //$$extra = "Does not allow you to use /cs if you are in danger",
        //$$category = {ESSENTIAL, EXPERIMENTAL, SURVIVAL, FEATURE}
        //#endif
    )
    public static boolean cameraModeSurvivalRestrictions = false;

    @Rule(
        //#if MC >= 11900
        categories = {ESSENTIAL, SURVIVAL, COMMAND}
        //#else
        //$$desc = "Prevents players from teleporting to players in spectator",
        //$$category = {ESSENTIAL, SURVIVAL, COMMAND}
        //#endif
    )
    public static boolean cameraModeTeleportBlacklist = false;

    @Rule(
        //#if MC >= 11900
        categories = {ESSENTIAL, SURVIVAL, COMMAND}
        //#else
        //$$desc = "Prevents players from teleporting to players on specific teams in spectator",
        //$$category = {ESSENTIAL, SURVIVAL, COMMAND}
        //#endif
    )
    public static boolean cameraModeTeamTeleportBlacklist = false;

    @Rule(
        //#if MC >= 11900
        categories = {ESSENTIAL, EXPERIMENTAL, FEATURE}
        //#else
        //$$desc = "Combines the duration of potions",
        //$$category = {ESSENTIAL, EXPERIMENTAL, FEATURE}
        //#endif
    )
    public static boolean combinePotionDuration = false;

    @Rule(
        //#if MC >= 11900
        categories = {ESSENTIAL, COMMAND, SURVIVAL, FEATURE}
        //#else
        //$$desc = "Allows you to backup regions in an area to the backups folder in the world folder",
        //$$category = {ESSENTIAL, COMMAND, SURVIVAL, FEATURE}
        //#endif
    )
    public static String commandBackup = "false";

    @Rule(
        //#if MC >= 11900
        categories = {ESSENTIAL, COMMAND, EXPERIMENTAL, SURVIVAL, FEATURE}
        //#else
        //$$desc = "Survival friendly spectator mode, puts the player in and out of spectator mode",
        //$$extra = "Allows for saving location after server reset using rule cameraModeRestoreLocation and adds functionality for cameraModeSurvivalRestrictions",
        //$$category = {ESSENTIAL, COMMAND, EXPERIMENTAL, SURVIVAL, FEATURE}
        //#endif
    )
    public static String commandCameraMode = "false";

    @Rule(
        //#if MC >= 11900
        categories = {ESSENTIAL, COMMAND, CREATIVE}
        //#else
        //$$desc = "Enables /defuse to be used to stop any tnt from exploding within a given range",
        //$$extra = "Usage: /defuse (Range)",
        //$$category = {ESSENTIAL, COMMAND, CREATIVE}
        //#endif
    )
    public static String commandDefuse = "false";

    @Rule(
        //#if MC >= 11900
        categories = {ESSENTIAL, COMMAND, CREATIVE}
        //#else
        //$$desc = "Allows the player to teleport to different dimensions with a simple command",
        //$$extra = "It will teleport you to specified location, unless unspecified in which case it will teleport you to 0,0",
        //$$category = {ESSENTIAL, COMMAND, CREATIVE}
        //#endif
    )
    public static String commandDimensions = "false";

    @Rule(
        //#if MC >= 11900
        categories = {ESSENTIAL, COMMAND, CREATIVE}
        //#else
        //$$desc = "Allows you to open your enderchest with /enderchest",
        //$$category = {ESSENTIAL, COMMAND, CREATIVE}
        //#endif
    )
    public static String commandEnderChest = "false";

    @Rule(
        //#if MC >= 11900
        categories = {ESSENTIAL, COMMAND}
        //#else
        //$$desc = "Allows /extinguish to be used to extinguish the player",
        //$$category = {ESSENTIAL, COMMAND}
        //#endif
    )
    public static String commandExtinguish = "false";

    @Rule(
        //#if MC >= 11900
        categories = {ESSENTIAL, COMMAND}
        //#else
        //$$desc = "Toggles the ability to fly while in survival mode",
        //$$category = {ESSENTIAL, COMMAND}
        //#endif
    )
    public static String commandFly = "false";

    @Rule(
        //#if MC >= 11900
        categories = {ESSENTIAL, COMMAND, CREATIVE}
        //#else
        //$$desc = "Allows /gmc, /gms, /gmsp, and /gma to be used",
        //$$category = {ESSENTIAL, COMMAND, CREATIVE}
        //#endif
    )
    public static String commandGM = "false";

    @Rule(
        //#if MC >= 11900
        categories = {ESSENTIAL, COMMAND}
        //#else
        //$$desc = "Can be buggy if used while in creative mode",
        //$$category = {ESSENTIAL, COMMAND}
        //#endif
    )
    public static String commandGod = "false";

    @Rule(
        //#if MC >= 11900
        categories = {ESSENTIAL, COMMAND, CREATIVE}
        //#else
        //$$desc = "Allows you to equip items to your head slot using /hat",
        //$$category = {ESSENTIAL, COMMAND, CREATIVE}
        //#endif
    )
    public static String commandHat = "false";

    @Rule(
        //#if MC >= 11900
        categories = {ESSENTIAL, COMMAND}
        //#else
        //$$desc = "Allows /heal to be used to heal and feed the player",
        //$$category = {ESSENTIAL, COMMAND}
        //#endif
    )
    public static String commandHeal = "false";

    @Rule(
        //#if MC >= 11900
        categories = {ESSENTIAL, COMMAND, CREATIVE}
        //#else
        //$$desc = "Allows you to simulate a lag spike using /lagspike",
        //$$category = {ESSENTIAL, COMMAND, CREATIVE}
        //#endif
    )
    public static String commandLagSpike = "false";

    @Rule(
        //#if MC >= 11900
        categories = {ESSENTIAL, COMMAND}
        //#else
        //$$desc = "Allows you to do /mods, it lists all the mods running on the server",
        //$$category = {ESSENTIAL, COMMAND}
        //#endif
    )
    public static String commandMods = "ops";

    @Rule(
        //#if MC >= 11900
        categories = {ESSENTIAL, COMMAND}
        //#else
        //$$desc = "Allows /more to be used to give a full stack of whatever item the player is holding",
        //$$category = {ESSENTIAL, COMMAND}
        //#endif
    )
    public static String commandMore = "false";

    @Rule(
        //#if MC >= 11900
        categories = {ESSENTIAL, COMMAND, CREATIVE}
        //#else
        //$$desc = "Lists other players near you",
        //$$category = {ESSENTIAL, COMMAND, CREATIVE}
        //#endif
    )
    public static String commandNear = "false";

    @Rule(
        //#if MC >= 11900
        categories = {ESSENTIAL, COMMAND, CREATIVE}
        //#else
        //$$desc = "Toggles night vision",
        //$$category = {ESSENTIAL, COMMAND, CREATIVE}
        //#endif
    )
    public static String commandNightVision = "false";

    @Rule(
        //#if MC >= 11900
        categories = {ESSENTIAL, COMMAND, EXPERIMENTAL}
        //#else
        //$$desc = "This allows you to spawn a fake player that doesn't load chunks (They appear on tab list and you are able to teleport to them)",
        //$$category = {ESSENTIAL, COMMAND, EXPERIMENTAL}
        //#endif
    )
    public static String commandGhostPlayer = "false";

    @Rule(
        //#if MC >= 11900
        categories = {ESSENTIAL, COMMAND, SURVIVAL}
        //#else
        //$$desc = "Allows anyone to use the /kick command",
        //$$category = {ESSENTIAL, COMMAND, SURVIVAL}
        //#endif
    )
    public static boolean commandPublicKick = false;

    @Rule(
        //#if MC >= 11900
        categories = {ESSENTIAL, COMMAND, CREATIVE}
        //#else
        //$$desc = "Allows anyone to use the /op command",
        //$$category = {ESSENTIAL, COMMAND, CREATIVE}
        //#endif
    )
    public static boolean commandPublicOp = false;

    @Rule(
        //#if MC >= 11900
        categories = {ESSENTIAL, COMMAND, SURVIVAL}
        //#else
        //$$desc = "Allows all players to use the command /save-all",
        //$$category = {ESSENTIAL, COMMAND, SURVIVAL}
        //#endif
    )
    public static boolean commandPublicSaveAll = false;

    @Rule(
        //#if MC >= 11900
        categories = {ESSENTIAL, COMMAND, SURVIVAL}
        //#else
        //$$desc = "Allows anyone to use the /scoreboard command",
        //$$category = {ESSENTIAL, COMMAND, SURVIVAL}
        //#endif
    )
    public static boolean commandPublicScoreboard = false;

    @Rule(
        //#if MC >= 11900
        categories = {ESSENTIAL, COMMAND, SURVIVAL}
        //#else
        //$$desc = "Allows anyone to use the /team command",
        //$$category = {ESSENTIAL, COMMAND, SURVIVAL}
        //#endif
    )
    public static boolean commandPublicTeam = false;

    @Rule(
        //#if MC >= 11900
        categories = {ESSENTIAL, COMMAND, SURVIVAL}
        //#else
        //$$desc = "Allows all players to change view distance",
        //$$category = {ESSENTIAL, COMMAND, SURVIVAL}
        //#endif
    )
    public static boolean commandPublicViewDistance = false;

    @Rule(
        //#if MC >= 11900
        categories = {ESSENTIAL, COMMAND, CREATIVE}
        //#else
        //$$desc = "Allows you see what region you are in and teleport to a region",
        //$$category = {ESSENTIAL, COMMAND, CREATIVE}
        //#endif
    )
    public static boolean commandRegion = false;

    @Rule(
        //#if MC >= 11900
        categories = {ESSENTIAL, COMMAND, CREATIVE}
        //#else
        //$$desc = "Allows you to rename items with a command",
        //$$category = {ESSENTIAL, COMMAND, CREATIVE}
        //#endif
    )
    public static boolean commandRename = false;

    @Rule(
        //#if MC >= 11900
        categories = {ESSENTIAL, COMMAND}
        //#else
        //$$desc = "Allows /repair to be used to repair any item the player is holding",
        //$$category = {ESSENTIAL, COMMAND}
        //#endif
    )
    public static String commandRepair = "false";

    @Rule(
        //#if MC >= 11900
        categories = {ESSENTIAL, COMMAND, CREATIVE}
        //#else
        //$$desc = "Toggles strength",
        //$$category = {ESSENTIAL, COMMAND, CREATIVE}
        //#endif
    )
    public static String commandStrength = "false";

    @Rule(
        //#if MC >= 11900
        categories = {ESSENTIAL, COMMAND, CREATIVE}
        //#else
        //$$desc = "Allows the player to teleport between the nether and overworld at equivalent coords",
        //$$category = {ESSENTIAL, COMMAND, CREATIVE}
        //#endif
    )
    public static String commandSwitchDimensions = "false";

    @Rule(
        //#if MC >= 11900
        categories = {ESSENTIAL, COMMAND, CREATIVE}
        //#else
        //$$desc = "Teleports the player up",
        //$$category = {ESSENTIAL, COMMAND, CREATIVE}
        //#endif
    )
    public static boolean commandTop = false;

    @Rule(
        //#if MC >= 11900
        categories = {ESSENTIAL, COMMAND, CREATIVE}
        //#else
        //$$desc = "Allows players to warp using /setwarp and /warp",
        //$$extra = "You are only able to set one warp which will be removed after server restart",
        //$$category = {ESSENTIAL, COMMAND, CREATIVE}
        //#endif
    )
    public static String commandWarp = "false";

    @Rule(
        //#if MC >= 11900
        categories = {ESSENTIAL, COMMAND, CREATIVE}
        //#else
        //$$desc = "Allows you to open a crafting table with /workbench",
        //$$category = {ESSENTIAL, COMMAND, CREATIVE}
        //#endif
    )
    public static String commandWorkbench = "false";

    @Rule(
        //#if MC >= 11900
        categories = {ESSENTIAL, EXPERIMENTAL, FEATURE}
        //#else
        //$$desc = "Allows you to edit a sign after it has been placed by right clicking it while sneaking",
        //$$category = {ESSENTIAL, EXPERIMENTAL, FEATURE}
        //#endif
    )
    public static boolean editableSigns = false;

    @Rule(
        //#if MC >= 11900
        categories = {ESSENTIAL, COMMAND, SURVIVAL, FEATURE}
        //#else
        //$$desc = "Mining blocks while crouching will put mined blocks straight into your inventory",
        //$$extra = "Same as wholmT's implementation in carpetAddons but works with stackable shulkers, requires players to subscribe to carefulbreak",
        //$$category = {ESSENTIAL, COMMAND, SURVIVAL, FEATURE}
        //#endif
    )
    public static boolean essentialCarefulBreak = false;

    @Rule(
        //#if MC >= 11900
        categories = {ESSENTIAL, COMMAND, SURVIVAL, FEATURE}
        //#else
        //$$desc = "Killing mobs while crouching will put dropped items straight into your inventory",
        //$$extra = "requires players to subscribe to carefuldrop",
        //$$category = {ESSENTIAL, COMMAND, SURVIVAL, FEATURE}
        //#endif
    )
    public static boolean essentialCarefulDrop = false;

    @Rule(
        //#if MC >= 11900
        categories = {ESSENTIAL, SURVIVAL, FEATURE}
        //#else
        //$$desc = "Automatically drop the fake player inventory on kill",
        //$$category = {ESSENTIAL, SURVIVAL, FEATURE}
        //#endif
    )
    public static boolean fakePlayerDropInventoryOnKill = false;

    @Rule(
        //#if MC >= 11900
        categories = {ESSENTIAL, EXPERIMENTAL, FEATURE},
        validators = EssentialValidators.GameRuleNoOpValidator.class
        //#else
        //$$desc = "Allows non-op players to change Game Rules from the client",
        //$$category = {ESSENTIAL, EXPERIMENTAL, FEATURE},
        //$$validate = EssentialValidators.GameRuleNoOpValidator.class
        //#endif
    )
    public static boolean gameRuleNonOp = false;

    @Rule(
        //#if MC >= 11900
        categories = {ESSENTIAL, EXPERIMENTAL, FEATURE},
        validators = EssentialValidators.GameRuleSyncValidator.class
        //#else
        //$$desc = "Syncs the Game Rules with the client",
        //$$extra = "Essential Client is required to change the rules on the client",
        //$$category = {ESSENTIAL, EXPERIMENTAL, FEATURE},
        //$$validate = EssentialValidators.GameRuleSyncValidator.class
        //#endif
    )
    public static boolean gameRuleSync = false;

    @Rule(
        //#if MC >= 11900
        categories = {ESSENTIAL, CREATIVE, EXPERIMENTAL}
        //#else
        //$$desc = "This allows for survival players to have infinite blocks, food, and enderpearls",
        //$$category = {ESSENTIAL, CREATIVE, EXPERIMENTAL}
        //#endif
    )
    public static boolean infiniteItems = false;

    @Rule(
        options = {"256", "1024"},
        strict = false,
        //#if MC >= 11900
        categories = {ESSENTIAL, FEATURE, EXPERIMENTAL},
        validators = Validators.NonNegativeNumber.class
        //#else
        //$$desc = "Changes the max chat length limit, you need EssentialClient for this to work, setting this rule below 256 may cause issues",
        //$$category = {ESSENTIAL, FEATURE, EXPERIMENTAL},
        //$$validate = Validator.NONNEGATIVE_NUMBER.class
        //#endif
    )
    public static int maxChatLength = 256;

    @Rule(
        //#if MC >= 11900
        categories = {ESSENTIAL, FEATURE, EXPERIMENTAL}
        //#else
        //$$desc = "Reimplements minecart boosting",
        //$$category = {ESSENTIAL, FEATURE, EXPERIMENTAL}
        //#endif
    )
    public static boolean minecartBoosting = false;

    @Rule(
        //#if MC >= 11900
        categories = {ESSENTIAL, FEATURE, SURVIVAL}
        //#else
        //$$desc = "Makes phantoms unable to spawn unless the mobcap allows for them to",
        //$$category = {ESSENTIAL, FEATURE, SURVIVAL}
        //#endif
    )
    public static boolean phantomsObeyMobcaps = false;

    @Rule(
        //#if MC >= 11900
        categories = {ESSENTIAL, SURVIVAL, FEATURE, EXPERIMENTAL}
        //#else
        //$$desc = "Automatically reloads the fake players actions after server restart",
        //$$category = {ESSENTIAL, SURVIVAL, FEATURE, EXPERIMENTAL}
        //#endif
    )
    public static boolean reloadFakePlayerActions = false;

    @Rule(
        //#if MC >= 11900
        categories = {ESSENTIAL, SURVIVAL, FEATURE}
        //#else
        //$$desc = "Automatically respawns fake players on server restart",
        //$$category = {ESSENTIAL, SURVIVAL, FEATURE}
        //#endif
    )
    public static boolean reloadFakePlayers = false;

    @Rule(
        options = {"0", "200", "500", "1000"},
        strict = false,
        //#if MC >= 11900
        categories = {ESSENTIAL, CREATIVE, FEATURE}
        //#else
        //$$desc = "Removes all item entities after set amount of item entities is reached in a world, set to 0 to disable",
        //$$category = {ESSENTIAL, CREATIVE, FEATURE}
        //#endif
    )
    public static int removeItemEntitiesAfterThreshold = 0;

    @Rule(
        //#if MC >= 11900
        categories = {ESSENTIAL, SURVIVAL, FEATURE}
        //#else
        //$$desc = "Removes the warning 'Mismatch in destroy block pos...' in console and logs",
        //$$category = {ESSENTIAL, SURVIVAL, FEATURE}
        //#endif
    )
    public static boolean removeWarnMismatchBlockPos = false;

    @Rule(
        //#if MC >= 11900
        categories = {ESSENTIAL, SURVIVAL, FEATURE}
        //#else
        //$$desc = "Removes the warning 'Fetching packet for removed entity...' in console and logs",
        //$$category = {ESSENTIAL, SURVIVAL, FEATURE}
        //#endif
    )
    public static boolean removeWarnRemovedEntity = false;

    @Rule(
        options = {"0", "100", "250", "500"},
        strict = false,
        //#if MC >= 11900
        categories = {ESSENTIAL, CREATIVE, FEATURE}
        //#else
        //$$desc = "Removes all xp entities after set amount of xp entities is reached in a world, set to 0 to disable",
        //$$category = {ESSENTIAL, CREATIVE, FEATURE}
        //#endif
    )
    public static int removeXpEntitiesAfterThreshold = 0;

    @Rule(
        //#if MC >= 11900
        categories = {ESSENTIAL, EXPERIMENTAL, FEATURE}
        //#else
        //$$desc = "Allows you to put shulker boxes inside of other shulkers",
        //$$category = {ESSENTIAL, EXPERIMENTAL, FEATURE}
        //#endif
    )
    public static boolean shulkerSception = false;

    @Rule(
        //#if MC >= 11900
        categories = {ESSENTIAL, SURVIVAL, FEATURE}
        //#else
        //$$desc = "Stops potion effects from ticking when in spectator",
        //$$category = {ESSENTIAL, SURVIVAL, FEATURE}
        //#endif
    )
    public static boolean spectatorPotionNoCountdown = false;

    @Rule(
        //#if MC >= 11900
        categories = {ESSENTIAL, SURVIVAL, FEATURE}
        //#else
        //$$desc = "Fixes stacked shulkers overloading comparators",
        //$$category = {ESSENTIAL, SURVIVAL, FEATURE}
        //#endif
    )
    public static boolean stackableShulkerComparatorOverloadFix = false;

    @Rule(
        //#if MC >= 11900
        categories = {ESSENTIAL, EXPERIMENTAL, FEATURE}
        //#else
        //$$desc = "Shulker boxes stack in player inventories, this is not the same as `stackableShulkerBoxes` in Carpet. This will always allow you to stack shulkers in your inventory.",
        //$$extra = "Disable all other `stackableShulker` rules (unless you are using stackableShulkersWithItems)",
        //$$category = {ESSENTIAL, EXPERIMENTAL, FEATURE}
        //#endif
    )
    public static boolean stackableShulkersInPlayerInventories = false;

    @Rule(
        //#if MC >= 11900
        categories = {ESSENTIAL, EXPERIMENTAL, FEATURE}
        //#else
        //$$desc = "Shulker boxes stack with items inside will stack with other shulkers with the same items",
        //$$extra = "This rule requires stackableShulkersInPlayerInventories to be enabled",
        //$$category = {ESSENTIAL, EXPERIMENTAL, FEATURE}
        //#endif
    )
    public static boolean stackableShulkersWithItems = false;
}

