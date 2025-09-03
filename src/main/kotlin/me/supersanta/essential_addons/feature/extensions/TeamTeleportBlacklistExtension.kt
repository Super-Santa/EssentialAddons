package me.supersanta.essential_addons.feature.extensions

import me.supersanta.essential_addons.EssentialAddons
import net.casual.arcade.events.GlobalEventHandler
import net.casual.arcade.events.ListenerRegistry.Companion.register
import net.casual.arcade.extensions.DataExtension
import net.casual.arcade.extensions.event.TeamExtensionEvent
import net.casual.arcade.extensions.utils.getExtension
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.level.storage.ValueInput
import net.minecraft.world.level.storage.ValueOutput
import net.minecraft.world.scores.PlayerTeam

class TeamTeleportBlacklistExtension: DataExtension {
    private var blacklisted: Boolean = false

    override fun getId(): ResourceLocation {
        return EssentialAddons.id("teleport_blacklist")
    }

    override fun deserialize(input: ValueInput) {
        this.blacklisted = input.getBooleanOr("blacklisted", false)
    }

    override fun serialize(output: ValueOutput) {
        output.putBoolean("blacklisted", this.blacklisted)
    }

    companion object {
        @JvmStatic
        var PlayerTeam.blacklistedFromTeleportingTo: Boolean
            set(value) { this.getExtension<TeamTeleportBlacklistExtension>().blacklisted = value }
            get() = this.getExtension<TeamTeleportBlacklistExtension>().blacklisted

        internal fun registerEvents() {
            GlobalEventHandler.Server.register<TeamExtensionEvent> {
                it.addExtension(TeamTeleportBlacklistExtension())
            }
        }
    }
}