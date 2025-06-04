package me.supersanta.essential_addons.feature.stackable_shulkers

import carpet.api.settings.CarpetRule
import carpet.api.settings.Validator
import me.supersanta.essential_addons.EssentialSettings
import net.casual.arcade.utils.ServerUtils
import net.minecraft.commands.CommandSourceStack

class StackableShulkerValidator: Validator<Boolean>() {
    override fun validate(
        source: CommandSourceStack?,
        rule: CarpetRule<Boolean>,
        value: Boolean,
        raw: String
    ): Boolean {
        val server = ServerUtils.getServerOrNull() ?: return value
        EssentialSettings.stackableShulkersInPlayerInventories = value
        for (player in server.playerList.players) {
            // Resend all container data
            player.containerMenu.sendAllDataToRemote()
        }
        return value
    }
}