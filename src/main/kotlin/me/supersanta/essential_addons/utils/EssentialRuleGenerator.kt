package me.supersanta.essential_addons.utils

import carpet.api.settings.SettingsManager
import carpet.utils.Translations
import joptsimple.OptionParser
import me.supersanta.essential_addons.EssentialSettings
import net.fabricmc.api.DedicatedServerModInitializer
import net.fabricmc.loader.api.FabricLoader
import org.apache.logging.log4j.LogManager
import java.io.IOException
import java.io.PrintStream
import java.nio.file.Files
import java.nio.file.Path
import java.util.*
import kotlin.system.exitProcess

// Ripped from Carpet
class EssentialRuleGenerator: DedicatedServerModInitializer {
    override fun onInitializeServer() {
        val args = Arrays.stream(FabricLoader.getInstance().getLaunchArguments(true))
            .filter { opt -> opt != "--" }.toList().toTypedArray()

        // Prepare an OptionParser for our parameters
        val parser = OptionParser()
        val pathSpec = parser.accepts("generate").withRequiredArg()

        // Minecraft may need more stuff later that we don't want to special-case
        parser.allowsUnrecognizedOptions()
        val options = parser.parse(*args)

        // If our flag isn't set, continue regular launch
        if (!options.has(pathSpec)) {
            return
        }

        val logger = LogManager.getLogger("EssentialRuleGenerator")

        val outputStream: PrintStream
        try {
            val path = Path.of(options.valueOf(pathSpec))
            logger.info("Generating Rules for Path: {}", path.toString())
            Files.createDirectories(path.parent)
            outputStream = PrintStream(Files.newOutputStream(path))
        } catch (e: IOException) {
            throw RuntimeException(e)
        }

        Translations.updateLanguage()
        val manager = SettingsManager("1.0.0", "carpet", "EssentialAddons")
        manager.parseSettingsClass(EssentialSettings::class.java)
        logger.info("Rule Count: {}", manager.carpetRules.size)
        outputStream.println(START)
        manager.dumpAllRulesToStream(outputStream, null)
        outputStream.close()
        logger.info("Complete")
        exitProcess(0)
    }

    companion object {
        private val START = """
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
        
        """.trimIndent()
    }
}