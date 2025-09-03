package me.supersanta.essential_addons.utils

import carpet.utils.CommandHelper
import com.mojang.brigadier.builder.ArgumentBuilder
import com.mojang.serialization.MapCodec
import me.lucko.fabric.api.permissions.v0.Permissions
import me.supersanta.essential_addons.EssentialAddons
import me.supersanta.essential_addons.feature.extensions.PlayerSubscriptionsExtension.Companion.hasAvailableSubscription
import me.supersanta.essential_addons.feature.extensions.PlayerSubscriptionsExtension.Companion.hasSubscription
import me.supersanta.essential_addons.feature.subscription.EssentialSubscriptions
import me.supersanta.essential_addons.mixins.MinecraftServerAccessor
import net.minecraft.commands.CommandSourceStack
import net.minecraft.network.chat.Component
import net.minecraft.server.MinecraftServer
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.ShulkerBoxBlock
import net.minecraft.world.level.storage.LevelResource
import net.minecraft.world.level.storage.LevelStorageSource.LevelStorageAccess
import net.minecraft.world.level.storage.ValueInput
import java.nio.file.Path
import java.util.Optional

val MinecraftServer.storageAccess: LevelStorageAccess
    get() = (this as MinecraftServerAccessor).storageSource

val MinecraftServer.essentialAddonsPath: Path
    get() = this.getWorldPath(LevelResource.ROOT).resolve("essential-addons")

fun ArgumentBuilder<CommandSourceStack, *>.requires(
    setting: () -> Any,
    permission: String
) {
    this.requires { source ->
        source.has(setting.invoke(), permission)
    }
}

fun CommandSourceStack.has(setting: Any, permission: String): Boolean {
    return CommandHelper.canUseCommand(this, setting) || Permissions.check(this, EssentialAddons.permission(permission))
}

fun ServerPlayer.sendToActionBar(component: Component) {
    this.sendSystemMessage(component, true)
}

fun ServerPlayer.hasCarefulBreak(): Boolean {
    return this.hasAvailableSubscription(EssentialSubscriptions.CAREFUL_BREAK) &&
        (this.isShiftKeyDown || this.hasSubscription(EssentialSubscriptions.ALWAYS_CAREFUL))
}

fun ServerPlayer.hasCarefulDrop(): Boolean {
    return this.hasAvailableSubscription(EssentialSubscriptions.CAREFUL_DROP) &&
        (this.isShiftKeyDown || this.hasSubscription(EssentialSubscriptions.ALWAYS_CAREFUL))
}

fun ItemStack.isShulkerBox(): Boolean {
    return Block.byItem(this.item) is ShulkerBoxBlock
}