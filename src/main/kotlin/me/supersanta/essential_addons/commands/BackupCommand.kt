package me.supersanta.essential_addons.commands

import com.mojang.brigadier.builder.LiteralArgumentBuilder
import com.mojang.brigadier.context.CommandContext
import kotlinx.io.IOException
import me.supersanta.essential_addons.EssentialAddons
import me.supersanta.essential_addons.EssentialSettings
import me.supersanta.essential_addons.utils.requires
import me.supersanta.essential_addons.utils.storageAccess
import net.casual.arcade.commands.CommandTree
import net.casual.arcade.commands.argument
import net.casual.arcade.commands.fail
import net.casual.arcade.commands.success
import net.casual.arcade.utils.component.lime
import net.casual.arcade.utils.toIdString
import net.minecraft.commands.CommandBuildContext
import net.minecraft.commands.CommandSourceStack
import net.minecraft.commands.arguments.DimensionArgument
import net.minecraft.commands.arguments.coordinates.ColumnPosArgument
import net.minecraft.network.chat.Component
import net.minecraft.server.level.ColumnPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.level.storage.LevelResource
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.io.path.copyTo
import kotlin.io.path.createDirectories
import kotlin.io.path.notExists
import kotlin.math.max
import kotlin.math.min

object BackupCommand: CommandTree {
    private val DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy.MM.dd-HH:mm:ss")

    override fun create(buildContext: CommandBuildContext): LiteralArgumentBuilder<CommandSourceStack> {
        return CommandTree.buildLiteral("backup") {
            requires(EssentialSettings::commandBackup, "command.backup")
            argument("from", ColumnPosArgument.columnPos()) {
                executes(::backupSingleRegion)
                argument("to", ColumnPosArgument.columnPos()) {
                    executes { context -> backupRegions(context, level = context.source.level) }
                    argument("dimension", DimensionArgument.dimension()) {
                        executes(::backupRegions)
                    }
                }
            }
        }
    }

    private fun backupSingleRegion(context: CommandContext<CommandSourceStack>): Int {
        val pos = ColumnPosArgument.getColumnPos(context, "from")
        return this.backupRegions(context, pos, pos, context.source.level)
    }

    private fun backupRegions(
        context: CommandContext<CommandSourceStack>,
        from: ColumnPos = ColumnPosArgument.getColumnPos(context, "from"),
        to: ColumnPos = ColumnPosArgument.getColumnPos(context, "to"),
        level: ServerLevel = DimensionArgument.getDimension(context, "dimension")
    ): Int {
        val server = context.source.server
        server.saveAllChunks(true, true, true)

        val dimension = level.dimension()
        val regionPath = server.storageAccess.getDimensionPath(dimension).resolve("region")
        val worldPath = server.getWorldPath(LevelResource.ROOT)

        val date = DATE_FORMAT.format(LocalDateTime.now())
        val backupPath = worldPath.resolve("backups").resolve(date)
            .resolve(dimension.toIdString())
        backupPath.createDirectories()

        if (regionPath.notExists()) {
            return context.source.fail("Dimension ${dimension.identifier()} has no regions")
        }

        try {
            val regionXFrom = Math.floorDiv(from.x, 512)
            val regionZFrom = Math.floorDiv(from.z, 512)
            val regionXTo = Math.floorDiv(to.x, 512)
            val regionZTo = Math.floorDiv(to.z, 512)
            for (x in min(regionXFrom, regionXTo)..max(regionXFrom, regionXTo)) {
                for (z in min(regionZFrom, regionZTo)..max(regionZFrom, regionZTo)) {
                    val fileName = "r.${x}.${z}.mca"
                    val region = regionPath.resolve(fileName)
                    if (region.notExists()) {
                        continue
                    }

                    region.copyTo(backupPath.resolve(fileName))
                }
            }
            return context.source.success(Component.literal("Successfully created backup").lime())
        } catch (e: IOException) {
            EssentialAddons.logger.error("Failed to create backup", e)
            return context.source.fail("Failed to create backup")
        }
    }
}