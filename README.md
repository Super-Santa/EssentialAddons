# EssentialAddons
[Fabric Carpet](https://github.com/gnembon/fabric-carpet) extension that adds  things from the Spigot plugin Essentials, or other features I think are needed for Minecraft.

Feel free to contribute by adding as many features as you want!

# Features
## commandFly
Toggles the ability to fly while in survival mode
* Type: `String`
* Default value: `false`
* Required options: `ops`,`false`,`true`
* Categories: `ESSENTIAL`, `COMMAND`
* Additional notes:
    * Using this also disables fall damage
    
## commandRepair
Repairs any item the player is holding
* Type: `String`
* Default value: `false`
* Required options: `ops`,`false`,`true`
* Categories: `ESSENTIAL`, `COMMAND`

## commandGM
Enable shortcuts for switching gamemodes (`/gmc`, `/gms`, `/gma`, `/gmsp`)
* Type: `String`
* Default value: `false`
* Required options: `ops`,`false`,`true`
* Categories: `ESSENTIAL`, `COMMAND`, `CREATIVE`

## commandHeal
Heals and feeds the player
* Type: `String`
* Default value: `false`
* Required options: `ops`,`false`,`true`
* Categories: `ESSENTIAL`, `COMMAND`

## commandExtinguish
Extinguishes the player
* Type: `String`
* Default value: `false`
* Required options: `ops`,`false`,`true`
* Categories: `ESSENTIAL`, `COMMAND`

## commandGod
Toggles invulnerability 
* Type: `String`
* Default value: `false`
* Required options: `ops`,`false`,`true`
* Categories: `ESSENTIAL`, `COMMAND`
* Additional notes:
    * Can be buggy if used while in creative mode

## commandDefuse
Used to stop any tnt from exploding within a given range
* Type: `String`
* Default value: `false`
* Required options: `ops`,`false`,`true`
* Categories: `ESSENTIAL`, `COMMAND`, `CREATIVE`

## commandMore
Gives you a full stack of whatever you are holding
* Type: `STRING`
* Default value: `false`
* Required options: `ops`,`false`,`true`
* Categories: `ESSENTIAL`, `COMMAND`

## commandStrength
Toggles Strength
* Type: `String`
* Default value: `false`
* Required options: `ops`,`false`,`true`
* Categories: `ESSENTIAL`, `COMMAND`, `CREATIVE`

## commandNightVision
Toggles Night Vision
* Type: `String`
* Default value: `false`
* Required options: `ops`,`false`,`true`
* Categories: `ESSENTIAL`, `COMMAND`, `CREATIVE`

## commandDimensions
Switch dimensions with `/end`, `/overworld`, and `/nether`
* Type: `String`
* Default value: `false`
* Required options: `ops`,`false`,`true`
* Categories: `ESSENTIAL`, `COMMAND`, `CREATIVE`
* Additional notes:
    * It will always teleport you to 0,0 in said dimension

## commandSwitchDimension
Teleports the player between the nether and overworld at equivalent coords 
* Type: `String`
* Default value: `false`
* Required options: `ops`,`false`,`true`
* Categories: `ESSENTIAL`, `COMMAND`, `CREATIVE`

## commandWarp
Enables `/setwarp` and `/warp`
* Type: `String`
* Default value: `false`
* Required options: `ops`,`false`,`true`
* Categories: `ESSENTIAL`, `COMMAND`, `CREATIVE`
* Additional notes:
    * Each player can only set one warp witch will be removed after server restart
    
## commandEnderChest
Opens your enderchest
* Type: `STRING`
* Default value: `false`
* Required options: `ops`,`false`,`true`
* Categories: `ESSENTIAL`, `COMMAND`, `CREATIVE`

## commandWorkbench
Opens the crafting table gui
* Type: `String`
* Default value: `false`
* Required options: `ops`,`false`,`true`
* Categories: `ESSENTIAL`, `COMMAND`, `CREATIVE`

## commandHat
Sets the item you are holding as your head slot
* Type: `String`
* Default value: `false`
* Required options: `ops`,`false`,`true`
* Categories: `ESSENTIAL`, `COMMAND`, `CREATIVE`

## commandPublicViewDistance
Allows all players to change the view distance
* Type: `Boolean`
* Default value: `false`
* Required options: `false`,`true`
* Categories: `ESSENTIAL`, `COMMAND`, `SURVIVAL`

## commandLagSpike
Allows you to simulate a lag spike
* Type: `String`
* Default value: `false`
* Required options: `ops`,`false`,`true`
* Categories: `ESSENTIAL`, `COMMAND`, `CREATIVE`

## commandPublicKick
Allows anyone to use the `/kick` command
* Type: `Boolean`
* Default value: `false`
* Required options: `false`,`true`
* Categories: `ESSENTIAL`, `COMMAND`, `SURVIVAL`

## commandPublicOp
Allows anyone to use the `/op` command
* Type: `Boolean`
* Default value: `false`
* Required options: `false`,`true`
* Categories: `ESSENTIAL`, `COMMAND`, `CREATIVE`

## commandPublicScoreboard
Allows all players to use the `/scoreboard`
* Type: `Boolean`
* Default value: `false`
* Required options: `false`,`true`
* Categories: `ESSENTIAL`, `COMMAND`, `SURVIVAL`

## commandTop
Teleports the player up
* Type: `Boolean`
* Default value: `false`
* Required options: `false`,`true`
* Categories: `ESSENTIAL`, `COMMAND`, `CREATIVE`

## commandNear
Lists other players near you
* Type: `Boolean`
* Default value: `false`
* Required options: `false`,`true`
* Categories: `ESSENTIAL`, `COMMAND`, `CREATIVE`

## commandRegion
Tells you what region you are in and allows you to teleport to a region
* Type: `Boolean`
* Default value: `false`
* Required options: `false`,`true`
* Categories: `ESSENTIAL`, `COMMAND`, `CREATIVE`

## commandCameraMode
Survival friendly spectator mode
* Type: `Boolean`
* Default value: `false`
* Required options: `false`,`true`
* Categories: `ESSENTIAL`, `COMMAND`, `EXPERIMENTAL`, `SURVIVAL`, `FEATURE`

## cameraModeTeleportBlacklist
Prevents players in spectator from teleporting to other players
* Type: `Boolean`
* Default value: `false`
* Required options: `false`,`true`
* Categories: `ESSENTIAL`, `SURVIVAL`, `EXPERIMENTAL`

## cameraModeSurvivalRestrictions
Does not allow you to go into spectator if you are in danger
* Type: `Boolean`
* Default value: `false`
* Required options: `false`,`true`
* Categories: `ESSENTIAL`, `EXPERIMENTAL`, `SURVIVAL`, `FEATURE`

## cameraModeRestoreLocation
Restores location when leaving spectator mode 
* Type: `Boolean`
* Default value: `false`
* Required options: `false`,`true`
* Categories: `ESSENTIAL`, `EXPERIMENTAL`, `SURVIVAL`, `FEATURE`
* Additional notes:
    * Saves location even after server restart
    
## cakeAlwaysEat
Allows you to always eat cake
* Type: `Boolean`
* Default value: `false`
* Required options: `false`,`true`
* Categories: `ESSENTIAL`, `EXPERIMENTAL`, `FEATURE`

## editableSigns
Allow signs to be edited after placed
* Type: `Boolean`
* Default value: `false`
* Required options: `false`,`true`
* Categories: `ESSENTIAL`, `EXPERIMENTAL`, `FEATURE`

## combinePotionDuration
Potion duration is combined
* Type: `Boolean`
* Default value: `false`
* Required options: `false`,`true`
* Categories: `ESSENTIAL`, `EXPERIMENTAL`, `FEATURE`

## stackableShulkerBoxesInPlayerInventories
Shulker boxes stack in player inventories
* Type: `Boolean`
* Default value: `false`
* Required options: `false`,`true`
* Categories: `ESSENTIAL`, `EXPERIMENTAL`, `FEATURE`
* Additional notes:
    * Recommended to enabled tweakEmptyShulkerBoxesStack in tweakeroo to avoid visual bugs
    
## stackableShulkersWithItems
Shulker boxes with the same items will stack
* Type: `Boolean`
* Default value: `false`
* Required options: `false`,`true`
* Categories: `ESSENTIAL`, `EXPERIMENTAL`, `FEATURE`
* Additional notes:
    * Requires stackableShulkerBoxesInPlayerInventories
    
## shulkerSception
Shulkers can be placed inside other shulkers
* Type: `Boolean`
* Default value: `false`
* Required options: `false`,`true`
* Categories: `ESSENTIAL`, `EXPERIMENTAL`, `FEATURE`

## essentialCarefulBreak
Mining blocks while crouching will put them straight into your inventory
* Type: `Boolean`
* Default value: `false`
* Required options: `false`,`true`
* Categories: `ESSENTIAL`, `COMMAND`, `SURVIVAL`, `FEATURE`
* Additional notes:
    * Players must subscribe to carefulbreak in order for it to work by using `/subscribe carefulbreak`

## betterStackableShulkers
Shulkers stack when picking them up from the ground
* Type: `Boolean`
* Default value: `false`
* Required options: `false`,`true`
* Categories: `ESSENTIAL`, `SURVIVAL`, `EXPERIMENTAL`
* Additional notes:
    * This rule should be used instead of stackableShulkerBoxesInPlayerInventories if your server is running hopper optimisations
    
## infiniteItems
Allows for survival players to have infinite blocks, food, and enderpearls
* Type: `Boolean`
* Default value: `false`
* Required options: `false`,`true`
* Categories: `ESSENTIAL`, `CREATIVE`, `EXPERIMENTAL`

## removeXpEntitiesAfterThreshold
Removes all xp entities after set amount is reached
* Type: `Int`
* Default value: `0`
* Categories: `ESSENTIAL`, `CREATIVE`, `FEATURE`

## removeItemEntitiesAfterThreshold
Removes all item entities after set amount is reached 
* Type: `Int`
* Default value: `0`
* Categories: `ESSENTIAL`, `CREATIVE`, `FEATURE`

## removeWarnRemovedEntity
Removes the waring `Fetching packet for removed entity...`
* Type: `Boolean`
* Default value: `false`
* Required options: `false`,`true`
* Categories: `ESSENTIAL`, `SURVIVAL`, `FEATURE`

## reloadFakePlayers
Automatically respawns fake players on server restart
* Type: `Boolean`
* Default value: `false`
* Required options: `false`,`true`
* Categories: `ESSENTIAL`, `SURVIVAL`, `FEATURE`

## reloadFakePlayerActions
Automatically reloads the fake players actions after server restart
* Type: `Boolean`
* Default value: `false`
* Required options: `false`,`true`
* Categories: `ESSENTIAL`, `SURVIVAL`, `FEATURE`, `EXPERIMENTAL`
