# EssentialAddons

[![Discord](https://badgen.net/discord/online-members/gn99m4QRY4?icon=discord&label=Discord&list=what)](https://discord.gg/gn99m4QRY4)
[![GitHub downloads](https://img.shields.io/github/downloads/super-santa/essentialaddons/total?label=Github%20downloads&logo=github)](https://github.com/Super-Santa/EssentialAddons/releases)
[![Modrinth downloads](https://img.shields.io/modrinth/dt/EssentialAddons?label=Modrinth%20downloads&logo=modrinth)](https://modrinth.com/mod/essentialaddons)

[Fabric Carpet](https://github.com/gnembon/fabric-carpet) extension that adds things from the Spigot plugin Essentials, or other features I think are needed for Minecraft.

## !!! Find Updated Releases on [Modrinth](https://modrinth.com/mod/essentialaddons) !!!

Features can be enabled through the `/carpet` command:
```
/carpet <rule_name> <rule_value>

# For example:
/carpet phantomsObeyMobcaps true
/carpet commandCameraMode ops
/carpet stackableShulkersInPlayerInventories true
```


Permissions can be customised for commands through a permissions mod such as [LuckPerms](https://luckperms.net/),
the name of the permissions are as follows:
```
essential-addons.command.<command_name>

# For example:
essential-addons.command.cs
essential-addons.command.hat
essential-addons.command.lag-spike
```

# EssentialAddons Settings
## broadcastToAll
Broadcasts all OP messages to everyone  
* Type: `Boolean`  
* Default value: `false`  
* Allowed options: `true`, `false`  
* Categories: `ESSENTIAL`, `SURVIVAL`  
  
## cakeAlwaysEat
Allows you to always eat cake, even when you are not hungry  
* Type: `Boolean`  
* Default value: `false`  
* Allowed options: `true`, `false`  
* Categories: `ESSENTIAL`, `EXPERIMENTAL`, `FEATURE`  
  
## cameraModeCommandName
Specifies the alias for the camera command, 'cs' by default  
* Type: `String`  
* Default value: `cs`  
* Categories: `ESSENTIAL`, `SURVIVAL`, `FEATURE`  
  
## cameraModeRestoreLocation
Restores player location back to original location in survival, similar to the cs script by Kdender  
Saves location even after server restart  
* Type: `Boolean`  
* Default value: `true`  
* Allowed options: `true`, `false`  
* Categories: `ESSENTIAL`, `SURVIVAL`, `FEATURE`  
  
## cameraModeSurvivalRestrictions
Ports cameraModeSurvivalRestrictions from carpet 1.12 into commandCameraMode  
Does not allow you to use camera mode if you are in danger  
* Type: `Boolean`  
* Default value: `false`  
* Allowed options: `true`, `false`  
* Categories: `ESSENTIAL`, `EXPERIMENTAL`, `SURVIVAL`, `FEATURE`  
  
## cameraModeTeamTeleportBlacklist
Prevents players from teleporting to players on specific teams in spectator  
Use the `/team-teleport-blacklist` command to specify teams  
* Type: `Boolean`  
* Default value: `false`  
* Allowed options: `true`, `false`  
* Categories: `ESSENTIAL`, `SURVIVAL`, `COMMAND`  
* Additional notes:  
  * It has an accompanying command  
  
## cameraModeTeleportBlacklist
Prevents players from teleporting to specific players in spectator  
Use the `/subscribe` command to enable/disable teleport blacklist per player  
* Type: `Boolean`  
* Default value: `false`  
* Allowed options: `true`, `false`  
* Categories: `ESSENTIAL`, `SURVIVAL`, `COMMAND`  
* Additional notes:  
  * It has an accompanying command  
  
## combinePotionDuration
Combines the duration of potions  
* Type: `Boolean`  
* Default value: `false`  
* Allowed options: `true`, `false`  
* Categories: `ESSENTIAL`, `EXPERIMENTAL`, `FEATURE`  
  
## commandBackup
Allows you to backup regions in an area to the backups folder in the world folder  
* Type: `String`  
* Default value: `false`  
* Allowed options: `true`, `false`, `ops`, `0`, `1`, `2`, `3`, `4`  
* Categories: `ESSENTIAL`, `COMMAND`, `SURVIVAL`, `FEATURE`  
* Additional notes:  
  * It has an accompanying command  
  * Can be limited to 'ops' only, true/false for everyone/no one, or a custom permission level  
  
## commandCameraMode
Survival friendly spectator mode, puts the player in and out of spectator mode. By default the command is `/cs`  
Allows for saving location after server reset using rule cameraModeRestoreLocation and adds functionality for cameraModeSurvivalRestrictions  
* Type: `String`  
* Default value: `false`  
* Allowed options: `true`, `false`, `ops`, `0`, `1`, `2`, `3`, `4`  
* Categories: `ESSENTIAL`, `COMMAND`, `EXPERIMENTAL`, `SURVIVAL`, `FEATURE`  
* Additional notes:  
  * It has an accompanying command  
  * Can be limited to 'ops' only, true/false for everyone/no one, or a custom permission level  
  
## commandDefuse
Enables `/defuse` to be used to stop any tnt from exploding within a given range  
* Type: `String`  
* Default value: `false`  
* Allowed options: `true`, `false`, `ops`, `0`, `1`, `2`, `3`, `4`  
* Categories: `ESSENTIAL`, `COMMAND`, `CREATIVE`  
* Additional notes:  
  * It has an accompanying command  
  * Can be limited to 'ops' only, true/false for everyone/no one, or a custom permission level  
  
## commandEnderChest
Allows you to open your enderchest with `/enderchest`  
* Type: `String`  
* Default value: `false`  
* Allowed options: `true`, `false`, `ops`, `0`, `1`, `2`, `3`, `4`  
* Categories: `ESSENTIAL`, `COMMAND`, `CREATIVE`  
* Additional notes:  
  * It has an accompanying command  
  * Can be limited to 'ops' only, true/false for everyone/no one, or a custom permission level  
  
## commandExtinguish
Allows `/extinguish` to be used to extinguish the player  
* Type: `String`  
* Default value: `false`  
* Allowed options: `true`, `false`, `ops`, `0`, `1`, `2`, `3`, `4`  
* Categories: `ESSENTIAL`, `COMMAND`  
* Additional notes:  
  * It has an accompanying command  
  * Can be limited to 'ops' only, true/false for everyone/no one, or a custom permission level  
  
## commandFly
Toggles the ability to fly while in survival mode using `/fly`  
Using this also disables fall damage  
* Type: `String`  
* Default value: `false`  
* Allowed options: `true`, `false`, `ops`, `0`, `1`, `2`, `3`, `4`  
* Categories: `ESSENTIAL`, `COMMAND`  
* Additional notes:  
  * It has an accompanying command  
  * Can be limited to 'ops' only, true/false for everyone/no one, or a custom permission level  
  
## commandGM
Allows `/gmc`, `/gms`, `/gmsp`, and `/gma` to be used  
* Type: `String`  
* Default value: `false`  
* Allowed options: `true`, `false`, `ops`, `0`, `1`, `2`, `3`, `4`  
* Categories: `ESSENTIAL`, `COMMAND`, `CREATIVE`  
* Additional notes:  
  * It has an accompanying command  
  * Can be limited to 'ops' only, true/false for everyone/no one, or a custom permission level  
  
## commandGod
Toggles invulnerability using `/god`  
Can be buggy if used while in creative mode  
* Type: `String`  
* Default value: `false`  
* Allowed options: `true`, `false`, `ops`, `0`, `1`, `2`, `3`, `4`  
* Categories: `ESSENTIAL`, `COMMAND`  
* Additional notes:  
  * It has an accompanying command  
  * Can be limited to 'ops' only, true/false for everyone/no one, or a custom permission level  
  
## commandHat
Allows you to equip your held item to your head slot using `/hat`  
* Type: `String`  
* Default value: `false`  
* Allowed options: `true`, `false`, `ops`, `0`, `1`, `2`, `3`, `4`  
* Categories: `ESSENTIAL`, `COMMAND`, `CREATIVE`  
* Additional notes:  
  * It has an accompanying command  
  * Can be limited to 'ops' only, true/false for everyone/no one, or a custom permission level  
  
## commandHeal
Allows `/heal` to be used to heal and feed the player  
* Type: `String`  
* Default value: `false`  
* Allowed options: `true`, `false`, `ops`, `0`, `1`, `2`, `3`, `4`  
* Categories: `ESSENTIAL`, `COMMAND`  
* Additional notes:  
  * It has an accompanying command  
  * Can be limited to 'ops' only, true/false for everyone/no one, or a custom permission level  
  
## commandLagSpike
Allows you to simulate a lag spike during a specified tick phase using `/lag-spike`  
* Type: `String`  
* Default value: `false`  
* Allowed options: `true`, `false`, `ops`, `0`, `1`, `2`, `3`, `4`  
* Categories: `ESSENTIAL`, `COMMAND`, `CREATIVE`  
* Additional notes:  
  * It has an accompanying command  
  * Can be limited to 'ops' only, true/false for everyone/no one, or a custom permission level  
  
## commandMods
Allows you to do `/mods`, it lists all the mods running on the server  
* Type: `String`  
* Default value: `ops`  
* Allowed options: `true`, `false`, `ops`, `0`, `1`, `2`, `3`, `4`  
* Categories: `ESSENTIAL`, `COMMAND`  
* Additional notes:  
  * It has an accompanying command  
  * Can be limited to 'ops' only, true/false for everyone/no one, or a custom permission level  
  
## commandMore
Allows `/more` to be used to give a full stack of whatever item the player is holding  
* Type: `String`  
* Default value: `false`  
* Allowed options: `true`, `false`, `ops`, `0`, `1`, `2`, `3`, `4`  
* Categories: `ESSENTIAL`, `COMMAND`  
* Additional notes:  
  * It has an accompanying command  
  * Can be limited to 'ops' only, true/false for everyone/no one, or a custom permission level  
  
## commandNear
Lists other players near you within the specified range  
* Type: `String`  
* Default value: `false`  
* Allowed options: `true`, `false`, `ops`, `0`, `1`, `2`, `3`, `4`  
* Categories: `ESSENTIAL`, `COMMAND`, `CREATIVE`  
* Additional notes:  
  * It has an accompanying command  
  * Can be limited to 'ops' only, true/false for everyone/no one, or a custom permission level  
  
## commandNightVision
Toggles night vision  
* Type: `String`  
* Default value: `false`  
* Allowed options: `true`, `false`, `ops`, `0`, `1`, `2`, `3`, `4`  
* Categories: `ESSENTIAL`, `COMMAND`, `CREATIVE`  
* Additional notes:  
  * It has an accompanying command  
  * Can be limited to 'ops' only, true/false for everyone/no one, or a custom permission level  
  
## commandPublicKick
Allows anyone to use the `/kick` command  
* Type: `Boolean`  
* Default value: `false`  
* Allowed options: `true`, `false`  
* Categories: `ESSENTIAL`, `COMMAND`, `SURVIVAL`  
* Additional notes:  
  * It has an accompanying command  
  
## commandPublicOp
Allows anyone to use the `/op` command  
* Type: `Boolean`  
* Default value: `false`  
* Allowed options: `true`, `false`  
* Categories: `ESSENTIAL`, `COMMAND`, `CREATIVE`  
* Additional notes:  
  * It has an accompanying command  
  
## commandPublicSaveAll
Allows anyone to use the command `/save-all`  
* Type: `Boolean`  
* Default value: `false`  
* Allowed options: `true`, `false`  
* Categories: `ESSENTIAL`, `COMMAND`, `SURVIVAL`  
* Additional notes:  
  * It has an accompanying command  
  
## commandPublicScoreboard
Allows anyone to use a subset of the `/scoreboard` commands  
* Type: `Boolean`  
* Default value: `false`  
* Allowed options: `true`, `false`  
* Categories: `ESSENTIAL`, `COMMAND`, `SURVIVAL`  
* Additional notes:  
  * It has an accompanying command  
  
## commandPublicTeam
Allows anyone to use the `/team` command  
* Type: `Boolean`  
* Default value: `false`  
* Allowed options: `true`, `false`  
* Categories: `ESSENTIAL`, `COMMAND`, `SURVIVAL`  
* Additional notes:  
  * It has an accompanying command  
  
## commandRename
Allows you to rename items with a command  
* Type: `Boolean`  
* Default value: `false`  
* Allowed options: `true`, `false`  
* Categories: `ESSENTIAL`, `COMMAND`, `CREATIVE`  
* Additional notes:  
  * It has an accompanying command  
  
## commandRepair
Allows `/repair` to be used to repair any item the player is holding  
* Type: `String`  
* Default value: `false`  
* Allowed options: `true`, `false`, `ops`, `0`, `1`, `2`, `3`, `4`  
* Categories: `ESSENTIAL`, `COMMAND`  
* Additional notes:  
  * It has an accompanying command  
  * Can be limited to 'ops' only, true/false for everyone/no one, or a custom permission level  
  
## commandStrength
Toggles strength  
* Type: `String`  
* Default value: `false`  
* Allowed options: `true`, `false`, `ops`, `0`, `1`, `2`, `3`, `4`  
* Categories: `ESSENTIAL`, `COMMAND`, `CREATIVE`  
* Additional notes:  
  * It has an accompanying command  
  * Can be limited to 'ops' only, true/false for everyone/no one, or a custom permission level  
  
## commandTop
Teleports the player up using the `/top` command  
* Type: `Boolean`  
* Default value: `false`  
* Allowed options: `true`, `false`  
* Categories: `ESSENTIAL`, `COMMAND`, `CREATIVE`  
* Additional notes:  
  * It has an accompanying command  
  
## commandViewDistance
Allows `/view-distance` for changing the server's view distance  
* Type: `Boolean`  
* Default value: `false`  
* Allowed options: `true`, `false`  
* Categories: `ESSENTIAL`, `COMMAND`, `SURVIVAL`  
* Additional notes:  
  * It has an accompanying command  
  
## commandWarp
Allows players create and teleport to warps using `/warp`  
* Type: `String`  
* Default value: `false`  
* Allowed options: `true`, `false`, `ops`, `0`, `1`, `2`, `3`, `4`  
* Categories: `ESSENTIAL`, `COMMAND`, `CREATIVE`  
* Additional notes:  
  * It has an accompanying command  
  * Can be limited to 'ops' only, true/false for everyone/no one, or a custom permission level  
  
## commandWorkbench
Allows you to open a crafting table with `/workbench`  
* Type: `String`  
* Default value: `false`  
* Allowed options: `true`, `false`, `ops`, `0`, `1`, `2`, `3`, `4`  
* Categories: `ESSENTIAL`, `COMMAND`, `CREATIVE`  
* Additional notes:  
  * It has an accompanying command  
  * Can be limited to 'ops' only, true/false for everyone/no one, or a custom permission level  
  
## essentialCarefulBreak
Mining blocks while crouching will put mined blocks straight into your inventory, requires players to subscribe to careful break using the `/subscribe` command  
Same as wholmT's implementation in carpetAddons but works with stackable shulkers`  
* Type: `Boolean`  
* Default value: `false`  
* Allowed options: `true`, `false`  
* Categories: `ESSENTIAL`, `COMMAND`, `SURVIVAL`, `FEATURE`  
* Additional notes:  
  * It has an accompanying command  
  
## essentialCarefulDrop
Killing entities while crouching will put dropped items straight into your inventory, requires players to subscribe to careful drop using the `/subscribe` command  
* Type: `Boolean`  
* Default value: `false`  
* Allowed options: `true`, `false`  
* Categories: `ESSENTIAL`, `COMMAND`, `SURVIVAL`, `FEATURE`  
* Additional notes:  
  * It has an accompanying command  
  
## fakePlayerDropInventoryOnKill
Automatically drop the fake player's inventory on kill  
* Type: `Boolean`  
* Default value: `false`  
* Allowed options: `true`, `false`  
* Categories: `ESSENTIAL`, `SURVIVAL`, `FEATURE`  
  
## infiniteItems
Prevents items from being consumed in survival or adventure mode  
* Type: `Boolean`  
* Default value: `false`  
* Allowed options: `true`, `false`  
* Categories: `ESSENTIAL`, `CREATIVE`, `EXPERIMENTAL`  
  
## minecartBoosting
Re-implements minecart boosting  
* Type: `Boolean`  
* Default value: `false`  
* Allowed options: `true`, `false`  
* Categories: `ESSENTIAL`, `FEATURE`, `EXPERIMENTAL`  
  
## phantomsObeyMobcaps
Makes phantoms unable to spawn unless the mobcap allows for them to  
* Type: `Boolean`  
* Default value: `false`  
* Allowed options: `true`, `false`  
* Categories: `ESSENTIAL`, `FEATURE`, `SURVIVAL`  
  
## reloadFakePlayers
Automatically respawns fake players on server restart  
* Type: `Boolean`  
* Default value: `false`  
* Allowed options: `true`, `false`  
* Categories: `ESSENTIAL`, `SURVIVAL`, `FEATURE`  
  
## removeItemEntitiesAfterThreshold
Removes all item entities after set amount of item entities is reached in a world, set to 0 to disable  
* Type: `Integer`  
* Default value: `0`  
* Suggested options: `0`, `200`, `500`, `1000`  
* Categories: `ESSENTIAL`, `CREATIVE`, `FEATURE`  
  
## removeWarnMismatchBlockPos
Removes the warning 'Mismatch in destroy block pos...' in console and logs  
* Type: `Boolean`  
* Default value: `false`  
* Allowed options: `true`, `false`  
* Categories: `ESSENTIAL`, `SURVIVAL`, `FEATURE`  
  
## removeWarnOversizedChunk
Removes the warning 'Saving oversized chunk...' in console and logs  
* Type: `Boolean`  
* Default value: `false`  
* Allowed options: `true`, `false`  
* Categories: `ESSENTIAL`, `SURVIVAL`, `FEATURE`  
  
## removeWarnRemovedEntity
Removes the warning 'Fetching packet for removed entity...' in console and logs  
* Type: `Boolean`  
* Default value: `false`  
* Allowed options: `true`, `false`  
* Categories: `ESSENTIAL`, `SURVIVAL`, `FEATURE`  
  
## removeXpEntitiesAfterThreshold
Removes all xp entities after set amount of xp entities is reached in a world, set to 0 to disable  
* Type: `Integer`  
* Default value: `0`  
* Suggested options: `0`, `100`, `250`, `500`  
* Categories: `ESSENTIAL`, `CREATIVE`, `FEATURE`  
  
## savePlayerActions
Saves all fake player actions and restarts them when players re-log  
* Type: `Boolean`  
* Default value: `false`  
* Allowed options: `true`, `false`  
* Categories: `ESSENTIAL`, `SURVIVAL`, `FEATURE`, `EXPERIMENTAL`  
  
## sensitiveBamboo
Makes bamboo break if next to a solid block - like cactus  
* Type: `Boolean`  
* Default value: `false`  
* Allowed options: `true`, `false`  
* Categories: `ESSENTIAL`, `SURVIVAL`, `FEATURE`  
  
## sensitiveSugarCane
Makes sugar cane break if next to a solid block - like cactus  
* Type: `Boolean`  
* Default value: `false`  
* Allowed options: `true`, `false`  
* Categories: `ESSENTIAL`, `SURVIVAL`, `FEATURE`  
  
## shulkerception
Allows you to put shulker boxes inside of other shulkers  
* Type: `Boolean`  
* Default value: `false`  
* Allowed options: `true`, `false`  
* Categories: `ESSENTIAL`, `EXPERIMENTAL`, `FEATURE`  
  
## spectatorPotionNoCountdown
Stops potion effects from ticking when in spectator  
* Type: `Boolean`  
* Default value: `false`  
* Allowed options: `true`, `false`  
* Categories: `ESSENTIAL`, `SURVIVAL`, `FEATURE`  
  
## stackableShulkerComparatorOverloadFix
Fixes stacked shulkers overloading comparators  
* Type: `Boolean`  
* Default value: `false`  
* Allowed options: `true`, `false`  
* Categories: `ESSENTIAL`, `SURVIVAL`, `FEATURE`  
  
## stackableShulkersInPlayerInventories
This will always allow you to stack shulkers manually in your inventory and on the ground, but cannot be stacked by hoppers.  
Disable all other `stackableShulker` rules (unless you are using stackableShulkersWithItems)  
* Type: `Boolean`  
* Default value: `false`  
* Allowed options: `true`, `false`  
* Categories: `ESSENTIAL`, `EXPERIMENTAL`, `FEATURE`  
  
## stackableShulkersWithItems
Shulker boxes stack with items inside will stack with other shulkers with the same items  
This rule requires stackableShulkersInPlayerInventories to be enabled  
* Type: `Boolean`  
* Default value: `false`  
* Allowed options: `true`, `false`  
* Categories: `ESSENTIAL`, `EXPERIMENTAL`, `FEATURE`  
  
## structureBlockKillEntities
Kills all entities (bar players) within the structure bounding box when loading a structure  
Requires ignore entities to be set to false  
* Type: `Boolean`  
* Default value: `false`  
* Allowed options: `true`, `false`  
* Categories: `ESSENTIAL`, `CREATIVE`, `FEATURE`  
  
## structureBlockReplaceFluids
Replaces existing fluids when pasting. Why would you want to keep existing fluids anyways???  
* Type: `Boolean`  
* Default value: `false`  
* Allowed options: `true`, `false`  
* Categories: `ESSENTIAL`, `CREATIVE`, `FEATURE`  
  
