package me.supersanta.essential_addons

import carpet.api.settings.Rule
import carpet.api.settings.RuleCategory.*
import me.supersanta.essential_addons.feature.camera_command.CameraCommandValidator
import me.supersanta.essential_addons.feature.stackable_shulkers.StackableShulkerValidator

object EssentialSettings {
    private const val ESSENTIAL = "essential"
    
    @Rule(categories = [ESSENTIAL, SURVIVAL])
    @JvmField var broadcastToAll: Boolean = false
    
    @Rule(categories = [ESSENTIAL, EXPERIMENTAL, FEATURE])
    @JvmField var cakeAlwaysEat: Boolean = false

    @Rule(categories = [ESSENTIAL, SURVIVAL, FEATURE])
    @JvmField var cameraModeRestoreLocation: Boolean = true

    @Rule(
        categories = [ESSENTIAL, SURVIVAL, FEATURE],
        validators = [CameraCommandValidator::class]
    )
    @JvmField var cameraModeCommandName: String = "cs"

    @Rule(categories = [ESSENTIAL, EXPERIMENTAL, SURVIVAL, FEATURE])
    @JvmField var cameraModeSurvivalRestrictions: Boolean = false

    @Rule(categories = [ESSENTIAL, SURVIVAL, COMMAND])
    @JvmField var cameraModeTeleportBlacklist: Boolean = false

    @Rule(categories = [ESSENTIAL, SURVIVAL, COMMAND])
    @JvmField var cameraModeTeamTeleportBlacklist: Boolean = false

    @Rule(categories = [ESSENTIAL, EXPERIMENTAL, FEATURE])
    @JvmField var combinePotionDuration: Boolean = false

    @Rule(categories = [ESSENTIAL, COMMAND, SURVIVAL, FEATURE])
    @JvmField var commandBackup: String = "false"

    @Rule(categories = [ESSENTIAL, COMMAND, EXPERIMENTAL, SURVIVAL, FEATURE])
    @JvmField var commandCameraMode: String = "false"

    @Rule(categories = [ESSENTIAL, COMMAND, CREATIVE])
    @JvmField var commandDefuse: String = "false"

    @Rule(categories = [ESSENTIAL, COMMAND, CREATIVE])
    @JvmField var commandEnderChest: String = "false"

    @Rule(categories = [ESSENTIAL, COMMAND])
    @JvmField var commandExtinguish: String = "false"

    @Rule(categories = [ESSENTIAL, COMMAND])
    @JvmField var commandFly: String = "false"

    @Rule(categories = [ESSENTIAL, COMMAND, CREATIVE])
    @JvmField var commandGM: String = "false"

    @Rule(categories = [ESSENTIAL, COMMAND])
    @JvmField var commandGod: String = "false"

    @Rule(categories = [ESSENTIAL, COMMAND, CREATIVE])
    @JvmField var commandHat: String = "false"

    @Rule(categories = [ESSENTIAL, COMMAND])
    @JvmField var commandHeal: String = "false"

    @Rule(categories = [ESSENTIAL, COMMAND, CREATIVE])
    @JvmField var commandLagSpike: String = "false"

    @Rule(categories = [ESSENTIAL, COMMAND])
    @JvmField var commandMods: String = "ops"

    @Rule(categories = [ESSENTIAL, COMMAND])
    @JvmField var commandMore: String = "false"

    @Rule(categories = [ESSENTIAL, COMMAND, CREATIVE])
    @JvmField var commandNear: String = "false"

    @Rule(categories = [ESSENTIAL, COMMAND, CREATIVE])
    @JvmField var commandNightVision: String = "false"

    @Rule(categories = [ESSENTIAL, COMMAND, SURVIVAL])
    @JvmField var commandPublicKick: Boolean = false

    @Rule(categories = [ESSENTIAL, COMMAND, CREATIVE])
    @JvmField var commandPublicOp: Boolean = false

    @Rule(categories = [ESSENTIAL, COMMAND, SURVIVAL])
    @JvmField var commandPublicSaveAll: Boolean = false

    @Rule(categories = [ESSENTIAL, COMMAND, SURVIVAL])
    @JvmField var commandPublicScoreboard: Boolean = false

    @Rule(categories = [ESSENTIAL, COMMAND, SURVIVAL])
    @JvmField var commandPublicTeam: Boolean = false

    @Rule(categories = [ESSENTIAL, COMMAND, SURVIVAL])
    @JvmField var commandViewDistance: Boolean = false

    @Rule(categories = [ESSENTIAL, COMMAND, CREATIVE])
    @JvmField var commandRename: Boolean = false

    @Rule(categories = [ESSENTIAL, COMMAND])
    @JvmField var commandRepair: String = "false"

    @Rule(categories = [ESSENTIAL, COMMAND, CREATIVE])
    @JvmField var commandStrength: String = "false"

    @Rule(categories = [ESSENTIAL, COMMAND, CREATIVE])
    @JvmField var commandTop: Boolean = false

    @Rule(categories = [ESSENTIAL, COMMAND, CREATIVE])
    @JvmField var commandWarp: String = "false"

    @Rule(categories = [ESSENTIAL, COMMAND, CREATIVE])
    @JvmField var commandWorkbench: String = "false"

    @Rule(categories = [ESSENTIAL, COMMAND, SURVIVAL, FEATURE])
    @JvmField var essentialCarefulBreak: Boolean = false

    @Rule(categories = [ESSENTIAL, COMMAND, SURVIVAL, FEATURE])
    @JvmField var essentialCarefulDrop: Boolean = false

    @Rule(categories = [ESSENTIAL, SURVIVAL, FEATURE])
    @JvmField var fakePlayerDropInventoryOnKill: Boolean = false

    @Rule(categories = [ESSENTIAL, CREATIVE, EXPERIMENTAL])
    @JvmField var infiniteItems: Boolean = false

    @Rule(categories = [ESSENTIAL, FEATURE, EXPERIMENTAL])
    @JvmField var minecartBoosting: Boolean = false

    @Rule(categories = [ESSENTIAL, FEATURE, SURVIVAL])
    @JvmField var phantomsObeyMobcaps: Boolean = false

    @Rule(categories = [ESSENTIAL, SURVIVAL, FEATURE])
    @JvmField var reloadFakePlayers: Boolean = false

    @Rule(categories = [ESSENTIAL, SURVIVAL, FEATURE, EXPERIMENTAL])
    @JvmField var savePlayerActions: Boolean = false

    @Rule(
        options = ["0", "200", "500", "1000"],
        strict = false,
        categories = [ESSENTIAL, CREATIVE, FEATURE]
    )
    @JvmField var removeItemEntitiesAfterThreshold: Int = 0

    @Rule(categories = [ESSENTIAL, SURVIVAL, FEATURE])
    @JvmField var removeWarnOversizedChunk: Boolean = false

    @Rule(categories = [ESSENTIAL, SURVIVAL, FEATURE])
    @JvmField var removeWarnMismatchBlockPos: Boolean = false

    @Rule(categories = [ESSENTIAL, SURVIVAL, FEATURE])
    @JvmField var removeWarnRemovedEntity: Boolean = false

    @Rule(
        options = ["0", "100", "250", "500"],
        strict = false,
        categories = [ESSENTIAL, CREATIVE, FEATURE]
    )
    @JvmField var removeXpEntitiesAfterThreshold: Int = 0

    @Rule(categories = [ESSENTIAL, SURVIVAL, FEATURE])
    @JvmField var sensitiveBamboo: Boolean = false

    @Rule(categories = [ESSENTIAL, SURVIVAL, FEATURE])
    @JvmField var sensitiveSugarCane: Boolean = false

    @Rule(categories = [ESSENTIAL, EXPERIMENTAL, FEATURE])
    @JvmField var shulkerception: Boolean = false

    @Rule(categories = [ESSENTIAL, SURVIVAL, FEATURE])
    @JvmField var spectatorPotionNoCountdown: Boolean = false

    @Rule(categories = [ESSENTIAL, SURVIVAL, FEATURE])
    @JvmField var stackableShulkerComparatorOverloadFix: Boolean = false

    @Rule(
        categories = [ESSENTIAL, EXPERIMENTAL, FEATURE],
        validators = [StackableShulkerValidator::class]
    )
    @JvmField var stackableShulkersInPlayerInventories: Boolean = false

    @Rule(categories = [ESSENTIAL, EXPERIMENTAL, FEATURE])
    @JvmField var stackableShulkersWithItems: Boolean = false

    @Rule(categories = [ESSENTIAL, CREATIVE, FEATURE])
    @JvmField var structureBlockKillEntities: Boolean = false

    @Rule(categories = [ESSENTIAL, CREATIVE, FEATURE])
    @JvmField var structureBlockReplaceFluids: Boolean = false
}