# EssentialAddons
[Fabric Carpet](https://github.com/gnembon/fabric-carpet) extension that adds  things from the Spigot plugin Essentials, or other features I think are needed for Minecraft.

Feel free to contribute by adding as many features as you want!

## Index

* [broadcastToAll](#broadcasttoall)
* [cakeAlwaysEat](#cakealwayseat)
* [cameraModeRestoreLocation](#cameramoderestorelocation)
* [cameraModeSurvivalRestrictions](#cameramodesurvivalrestrictions)
* [cameraModeTeleportBlacklist](#cameramodeteleportblacklist)
* [combinePotionDuration](#combinepotionduration)
* [commandCameraMode](#commandcameramode)
* [commandDefuse](#commanddefuse)
* [commandDimensions](#commanddimensions)
* [commandEnderChest](#commandenderchest)
* [commandExtinguish](#commandextinguish)
* [commandFly](#commandfly)
* [commandGM](#commandgm)
* [commandGod](#commandgod)
* [commandHat](#commandhat)
* [commandHeal](#commandheal)
* [commandLagSpike](#commandlagspike)
* [commandMods](#commandmods)
* [commandMore](#commandmore)
* [commandNightVision](#commandnightvision)
* [commandPlayerFake](#commandplayerfake)
* [commandPublicKick](#commandpublickick)
* [commandPublicOp](#commandpublicop)
* [commandPublicSaveAll](#commandpublicsaveall)
* [commandPublicScoreboard](#commandpublicscoreboard)
* [commandPublicViewDistance](#commandpublicviewdistance)
* [commandRegion](#commandregion)
* [commandRename](#commandrename)
* [commandRepair](#commandrepair)
* [commandStrength](#commandstrength)
* [commandSwitchDimension](#commandswitchdimension)
* [commandTop](#commandtop)
* [commandWarp](#commandwarp)
* [commandWorkbench](#commandworkbench)
* [editableSigns](#editablesigns)
* [essentialCarefulBreak](#essentialcarefulbreak)
* [hostileMobsSpawnInCompleteDarkness](#hostilemobsspawnincompletedarkness)
* [infiniteItems](#infiniteitems)
* [reloadFakePlayerActions](#reloadfakeplayeractions)
* [reloadFakePlayers](#reloadfakeplayers)
* [removeItemEntitiesAfterThreshold](#removeitementitiesafterthreshold)
* [removeWarnRemovedEntity](#removewarnremovedentity)
* [removeWarnMismatchBlockPos](#removewarnmismatchblockpos)
* [removeXpEntitiesAfterThreshold](#removexpentitiesafterthreshold)
* [shulkerSception](#shulkersception)
* [stackableShulkerComparatorOverloadFix](#stackableshulkercomparatoroverloadfix)
* [stackableShulkerBoxesInPlayerInventories](#stackableshulkerboxesinplayerinventories)
* [stackableShulkersWithItems](#stackableshulkerswithitems)



# Features

## broadcastToAll
Broadcasts all OP messages to everyone
* Type: `Boolean`
* Default value: `false`
* Required options: `false`,`true`
* Categories: `ESSENTIAL`, `SURVIVAL`

## cakeAlwaysEat
Allows you to always eat cake
* Type: `Boolean`
* Default value: `false`
* Required options: `false`,`true`
* Categories: `ESSENTIAL`, `EXPERIMENTAL`, `FEATURE`

## cameraModeRestoreLocation
Restores location when leaving spectator mode
* Type: `Boolean`
* Default value: `false`
* Required options: `false`,`true`
* Categories: `ESSENTIAL`, `EXPERIMENTAL`, `SURVIVAL`, `FEATURE`
* Additional notes:
  * Saves location even after server restart

## cameraModeSurvivalRestrictions
Does not allow you to go into spectator if you are in danger
* Type: `Boolean`
* Default value: `false`
* Required options: `false`,`true`
* Categories: `ESSENTIAL`, `EXPERIMENTAL`, `SURVIVAL`, `FEATURE`

## cameraModeTeleportBlacklist
Prevents players in spectator from teleporting to other players
* Type: `Boolean`
* Default value: `false`
* Required options: `false`,`true`
* Categories: `ESSENTIAL`, `SURVIVAL`, `EXPERIMENTAL`

## combinePotionDuration
Potion duration is combined
* Type: `Boolean`
* Default value: `false`
* Required options: `false`,`true`
* Categories: `ESSENTIAL`, `EXPERIMENTAL`, `FEATURE`

## commandCameraMode
Survival friendly spectator mode
* Type: `Boolean`
* Default value: `false`
* Required options: `false`,`true`
* Categories: `ESSENTIAL`, `COMMAND`, `EXPERIMENTAL`, `SURVIVAL`, `FEATURE`

## commandDefuse
Used to stop any tnt from exploding within a given range
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
  * It will teleport you to specified location, unless unspecified in which case it will teleport you to 0,0

## commandEnderChest
Opens your enderchest
* Type: `STRING`
* Default value: `false`
* Required options: `ops`,`false`,`true`
* Categories: `ESSENTIAL`, `COMMAND`, `CREATIVE`

## commandExtinguish
Extinguishes the player
* Type: `String`
* Default value: `false`
* Required options: `ops`,`false`,`true`
* Categories: `ESSENTIAL`, `COMMAND`

## commandFly
Toggles the ability to fly while in survival mode
* Type: `String`
* Default value: `false`
* Required options: `ops`,`false`,`true`
* Categories: `ESSENTIAL`, `COMMAND`
* Additional notes:
  * Using this also disables fall damage

## commandGM
Enable shortcuts for switching gamemodes (`/gmc`, `/gms`, `/gma`, `/gmsp`)
* Type: `String`
* Default value: `false`
* Required options: `ops`,`false`,`true`
* Categories: `ESSENTIAL`, `COMMAND`, `CREATIVE`

## commandGod
Toggles invulnerability
* Type: `String`
* Default value: `false`
* Required options: `ops`,`false`,`true`
* Categories: `ESSENTIAL`, `COMMAND`
* Additional notes:
  * Can be buggy if used while in creative mode

## commandHat
Sets the item you are holding as your head slot
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

## commandLagSpike
Allows you to simulate a lag spike
* Type: `String`
* Default value: `false`
* Required options: `ops`,`false`,`true`
* Categories: `ESSENTIAL`, `COMMAND`, `CREATIVE`

## commandMods
Allows you to do /mods, it lists all the mods running on the server
* Type: `STRING`
* Default value: `false`
* Required options: `ops`,`false`,`true`
* Categories: `ESSENTIAL`, `COMMAND`

## commandMore
Gives you a full stack of whatever you are holding
* Type: `STRING`
* Default value: `false`
* Required options: `ops`,`false`,`true`
* Categories: `ESSENTIAL`, `COMMAND`

## commandNear
Lists other players near you
* Type: `Boolean`
* Default value: `false`
* Required options: `false`,`true`
* Categories: `ESSENTIAL`, `COMMAND`, `CREATIVE`

## commandNightVision
Toggles Night Vision
* Type: `String`
* Default value: `false`
* Required options: `ops`,`false`,`true`
* Categories: `ESSENTIAL`, `COMMAND`, `CREATIVE`

## commandPlayerFake
This allows you to spawn a fake player that doesn't load chunks (They appear on tab list and you are able to teleport to them)
* Type: `String`
* Default value: `false`
* Required options: `ops`,`false`,`true`
* Categories: `ESSENTIAL`, `COMMAND`, `EXPERIMENTAL`

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

## commandPublicSaveAll
Allows anyone to use the `/save-all` command
* Type: `Boolean`
* Default value: `false`
* Required options: `false`,`true`
* Categories: `ESSENTIAL`, `COMMAND`, `SURVIVAL`

## commandPublicScoreboard
Allows all players to use the `/scoreboard`
* Type: `Boolean`
* Default value: `false`
* Required options: `false`,`true`
* Categories: `ESSENTIAL`, `COMMAND`, `SURVIVAL`

## commandPublicViewDistance
Allows all players to change the view distance
* Type: `Boolean`
* Default value: `false`
* Required options: `false`,`true`
* Categories: `ESSENTIAL`, `COMMAND`, `SURVIVAL`

## commandRegion
Tells you what region you are in and allows you to teleport to a region
* Type: `Boolean`
* Default value: `false`
* Required options: `false`,`true`
* Categories: `ESSENTIAL`, `COMMAND`, `CREATIVE`

## commandRename
Allows you to rename the item you are holding
* Type: `Boolean`
* Default value: `false`
* Required options: `false`,`true`
* Categories: `ESSENTIAL`, `COMMAND`, `CREATIVE`

## commandRepair
Repairs any item the player is holding
* Type: `String`
* Default value: `false`
* Required options: `ops`,`false`,`true`
* Categories: `ESSENTIAL`, `COMMAND`

## commandStrength
Toggles Strength
* Type: `String`
* Default value: `false`
* Required options: `ops`,`false`,`true`
* Categories: `ESSENTIAL`, `COMMAND`, `CREATIVE`

## commandSwitchDimension
Teleports the player between the nether and overworld at equivalent coords
* Type: `String`
* Default value: `false`
* Required options: `ops`,`false`,`true`
* Categories: `ESSENTIAL`, `COMMAND`, `CREATIVE`

## commandTop
Teleports the player up
* Type: `Boolean`
* Default value: `false`
* Required options: `false`,`true`
* Categories: `ESSENTIAL`, `COMMAND`, `CREATIVE`

## commandWarp
Enables `/setwarp` and `/warp`
* Type: `String`
* Default value: `false`
* Required options: `ops`,`false`,`true`
* Categories: `ESSENTIAL`, `COMMAND`, `CREATIVE`
* Additional notes:
  * Each player can only set one warp witch will be removed after server restart

## commandWorkbench
Opens the crafting table gui
* Type: `String`
* Default value: `false`
* Required options: `ops`,`false`,`true`
* Categories: `ESSENTIAL`, `COMMAND`, `CREATIVE`

## editableSigns
Allow signs to be edited after placed
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

## hostileMobsSpawnInCompleteDarkness
Only allows hostile mobs to spawn in complete darkness (1.18)
* Type: `Boolean`
* Default value: `false`
* Required options: `false`,`true`
* Categories: `ESSENTIAL`, `EXPERIMENTAL`, `FEATURE`

## infiniteItems
Allows for survival players to have infinite blocks, food, and enderpearls
* Type: `Boolean`
* Default value: `false`
* Required options: `false`,`true`
* Categories: `ESSENTIAL`, `CREATIVE`, `EXPERIMENTAL`

## reloadFakePlayerActions
Automatically reloads the fake players actions after server restart
* Type: `Boolean`
* Default value: `false`
* Required options: `false`,`true`
* Categories: `ESSENTIAL`, `SURVIVAL`, `FEATURE`, `EXPERIMENTAL`

## reloadFakePlayers
Automatically respawns fake players on server restart
* Type: `Boolean`
* Default value: `false`
* Required options: `false`,`true`
* Categories: `ESSENTIAL`, `SURVIVAL`, `FEATURE`

## removeItemEntitiesAfterThreshold
Removes all item entities after set amount is reached
* Type: `Int`
* Default value: `0`
* Categories: `ESSENTIAL`, `CREATIVE`, `FEATURE`

## removeWarnMismatchBlockPos
Removes the waring `Mismatch in destroy block pos...`
* Type: `Boolean`
* Default value: `false`
* Required options: `false`,`true`
* Categories: `ESSENTIAL`, `SURVIVAL`, `FEATURE`

## removeWarnRemovedEntity
Removes the waring `Fetching packet for removed entity...`
* Type: `Boolean`
* Default value: `false`
* Required options: `false`,`true`
* Categories: `ESSENTIAL`, `SURVIVAL`, `FEATURE`

## removeXpEntitiesAfterThreshold
Removes all xp entities after set amount is reached
* Type: `Int`
* Default value: `0`
* Categories: `ESSENTIAL`, `CREATIVE`, `FEATURE`

## shulkerSception
Shulkers can be placed inside other shulkers
* Type: `Boolean`
* Default value: `false`
* Required options: `false`,`true`
* Categories: `ESSENTIAL`, `EXPERIMENTAL`, `FEATURE`

## stackableShulkerComparatorOverloadFix
Fixes stacked shulkers overloading comparators
* Type: `Boolean`
* Default value: `false`
* Required options: `false`,`true`
* Categories: `ESSENTIAL`, `SURVIVAL`, `FEATURE`

## stackableShulkerBoxesInPlayerInventories
Shulker boxes stack in player inventories, this is not the same as `stackableShulkerBoxes` in Carpet. This will **always** allow you to stack shulkers in your inventory.
* Type: `Boolean`
* Default value: `false`
* Required options: `false`,`true`
* Categories: `ESSENTIAL`, `EXPERIMENTAL`, `FEATURE`
* Additional notes:
  * Disable all other `stackableShulker` rules (unless you are using `stackableShulkersWithItems`)

## stackableShulkersWithItems
Shulker boxes with the same items will stack
* Type: `Boolean`
* Default value: `false`
* Required options: `false`,`true`
* Categories: `ESSENTIAL`, `EXPERIMENTAL`, `FEATURE`
* Additional notes:
  * Requires stackableShulkerBoxesInPlayerInventories
