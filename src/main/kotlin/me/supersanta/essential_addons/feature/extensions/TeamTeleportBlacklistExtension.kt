package me.supersanta.essential_addons.feature.extensions

import me.supersanta.essential_addons.EssentialAddons
import net.casual.arcade.events.GlobalEventHandler
import net.casual.arcade.events.ListenerRegistry.Companion.register
import net.casual.arcade.extensions.DataExtension
import net.casual.arcade.extensions.event.TeamExtensionEvent
import net.casual.arcade.extensions.event.TeamExtensionEvent.Companion.getExtension
import net.minecraft.nbt.ByteTag
import net.minecraft.nbt.Tag
import net.minecraft.world.scores.PlayerTeam

class TeamTeleportBlacklistExtension: DataExtension {
    private var blacklisted: Boolean = false

    override fun getName(): String {
        return "${EssentialAddons.MOD_ID}_teleport_blacklist_extension"
    }

    override fun deserialize(element: Tag) {
        this.blacklisted = element.asBoolean().orElse(false)
    }

    override fun serialize(): Tag? {
        return ByteTag.valueOf(this.blacklisted)
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