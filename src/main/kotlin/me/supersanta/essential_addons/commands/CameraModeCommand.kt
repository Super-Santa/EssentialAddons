package me.supersanta.essential_addons.commands

import com.mojang.brigadier.Command
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import com.mojang.brigadier.context.CommandContext
import me.supersanta.essential_addons.EssentialSettings
import me.supersanta.essential_addons.feature.extensions.PlayerCameraModeExtension.Companion.cameraModeLocation
import me.supersanta.essential_addons.utils.requires
import me.supersanta.essential_addons.utils.sendToActionBar
import net.casual.arcade.commands.CommandTree
import net.casual.arcade.utils.component.gold
import net.casual.arcade.utils.component.lime
import net.casual.arcade.utils.component.red
import net.casual.arcade.utils.entity.teleportTo
import net.casual.arcade.utils.math.location.locationWithLevel
import net.minecraft.commands.CommandBuildContext
import net.minecraft.commands.CommandSourceStack
import net.minecraft.network.chat.Component
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.effect.MobEffectCategory
import net.minecraft.world.entity.monster.Monster
import net.minecraft.world.level.GameType
import net.minecraft.world.phys.AABB

object CameraModeCommand: CommandTree<CommandSourceStack> {
    override fun create(buildContext: CommandBuildContext): LiteralArgumentBuilder<CommandSourceStack> {
        return CommandTree.buildLiteral(EssentialSettings.cameraModeCommandName) {
            requires(EssentialSettings::commandCameraMode, "command.cs")
            executes(::toggleCameraMode)
        }
    }

    private fun toggleCameraMode(context: CommandContext<CommandSourceStack>): Int {
        val player = context.source.playerOrException
        if (player.isSpectator) {
            this.disableCameraMode(player)
        } else {
            this.enableCameraMode(player)
        }
        return Command.SINGLE_SUCCESS
    }

    private fun enableCameraMode(player: ServerPlayer) {
        if (EssentialSettings.cameraModeSurvivalRestrictions) {
            val danger = this.getPotentialDanger(player)
            if (danger != null) {
                player.sendToActionBar(
                    Component.literal("You cannot enter spectator because ${danger.message}").red()
                )
                return
            }
        }

        player.cameraModeLocation = player.locationWithLevel

        player.setGameMode(GameType.SPECTATOR)
        player.sendToActionBar(
            Component.literal("You have been put in ").append(Component.literal("SPECTATOR").lime()).gold()
        )
    }

    private fun disableCameraMode(player: ServerPlayer) {
        var previous = player.gameMode.previousGameModeForPlayer
        if (previous == null || previous == GameType.SPECTATOR) {
            previous = GameType.SURVIVAL
        }

        player.setGameMode(previous)
        if (EssentialSettings.cameraModeRestoreLocation) {
            val location = player.cameraModeLocation
            if (location == null) {
                player.sendToActionBar(
                    Component.literal("Failed to retrieve previous location").red()
                )
                return
            }
            player.teleportTo(location)
        }

        player.sendToActionBar(
            Component.literal("You have been put in ").append(Component.literal(previous.name).lime()).gold()
        )
    }

    private fun getPotentialDanger(player: ServerPlayer): PotentialDanger? {
        if (player.isInvulnerable) {
            return null
        }

        when {
            player.isOnFire -> return PotentialDanger.OnFire
            player.fallDistance > 0 -> return PotentialDanger.Falling
            player.isFallFlying -> return PotentialDanger.Flying
            player.isUnderWater -> return PotentialDanger.UnderWater
        }

        for (effect in player.activeEffects) {
            if (effect.effect.value().category == MobEffectCategory.HARMFUL) {
                return PotentialDanger.StatusEffect
            }
        }

        val aabb = AABB.ofSize(player.position(), 4.0, 4.0, 4.0)
        val monsters = player.level().getEntitiesOfClass(Monster::class.java, aabb)
        if (monsters.isNotEmpty()) {
            return PotentialDanger.HostileMobs
        }
        return null
    }

    private enum class PotentialDanger(val message: String) {
        HostileMobs("there are mobs nearby"),
        OnFire("you are on fire"),
        Falling("you are falling"),
        Flying("you are flying"),
        UnderWater("you are underwater"),
        StatusEffect("you have a negative status effect")
    }
}