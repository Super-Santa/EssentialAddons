package me.supersanta.essential_addons.commands

import com.mojang.brigadier.arguments.DoubleArgumentType
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import com.mojang.brigadier.context.CommandContext
import me.supersanta.essential_addons.EssentialSettings
import me.supersanta.essential_addons.utils.requires
import net.casual.arcade.commands.CommandTree
import net.casual.arcade.commands.argument
import net.casual.arcade.commands.locationWithLevel
import net.casual.arcade.commands.success
import net.casual.arcade.utils.ComponentUtils.lime
import net.minecraft.commands.CommandBuildContext
import net.minecraft.commands.CommandSourceStack
import net.minecraft.network.chat.Component
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.item.PrimedTnt
import net.minecraft.world.phys.AABB

object DefuseCommand: CommandTree {
    override fun create(buildContext: CommandBuildContext): LiteralArgumentBuilder<CommandSourceStack> {
        return CommandTree.buildLiteral("defuse") {
            requires(EssentialSettings::commandDefuse, "command.defuse")
            argument("range", DoubleArgumentType.doubleArg(0.0)) {
                executes(::defuseTnt)
            }
        }
    }

    private fun defuseTnt(context: CommandContext<CommandSourceStack>): Int {
        val location = context.source.locationWithLevel
        val range = DoubleArgumentType.getDouble(context, "range")
        val aabb = AABB.ofSize(location.position, range, range, range)
        val entities = location.level.getEntitiesOfClass(PrimedTnt::class.java, aabb) { tnt ->
            tnt.distanceToSqr(location.position) < range * range
        }
        for (tnt in entities) {
            tnt.remove(Entity.RemovalReason.KILLED)
        }
        return context.source.success(
            Component.literal("Successfully defused ${entities.size} TNT entities").lime()
        )
    }
}