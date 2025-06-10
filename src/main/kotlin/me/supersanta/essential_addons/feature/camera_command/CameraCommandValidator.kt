package me.supersanta.essential_addons.feature.camera_command

import carpet.api.settings.CarpetRule
import carpet.api.settings.Validator
import carpet.utils.CommandHelper
import com.mojang.brigadier.StringReader
import me.supersanta.essential_addons.EssentialAddons
import me.supersanta.essential_addons.EssentialSettings
import net.casual.arcade.utils.ServerUtils
import net.minecraft.commands.CommandSourceStack

class CameraCommandValidator: Validator<String>() {
    override fun validate(
        source: CommandSourceStack?,
        rule: CarpetRule<String>,
        value: String,
        raw: String
    ): String? {
        val server = ServerUtils.getServerOrNull() ?: return value
        if (!value.all(StringReader::isAllowedInUnquotedString)) {
            return null
        }

        if (EssentialSettings.cameraModeCommandName != value) {
            EssentialSettings.cameraModeCommandName = value
            EssentialAddons.registerCommands(server)
            CommandHelper.notifyPlayersCommandsChanged(server)
        }
        return value
    }
}