package essentialaddons.utils;

//#if MC >= 11900
import carpet.api.settings.SettingsManager;
import carpet.utils.Translations;
import joptsimple.OptionParser;
import joptsimple.OptionSet;
import joptsimple.OptionSpec;
import net.fabricmc.loader.api.FabricLoader;

import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
//#endif

import net.fabricmc.api.ModInitializer;

// Ripped from Carpet
public class EssentialRuleGenerator implements ModInitializer {
	private static final String START =
	"""
	# EssentialAddons
	
	[![Discord](https://badgen.net/discord/online-members/gn99m4QRY4?icon=discord&label=Discord&list=what)](https://discord.gg/gn99m4QRY4)
	[![GitHub downloads](https://img.shields.io/github/downloads/super-santa/essentialaddons/total?label=Github%20downloads&logo=github)](https://github.com/Super-Santa/EssentialAddons/releases)
	
	[Fabric Carpet](https://github.com/gnembon/fabric-carpet) extension that adds  things from the Spigot plugin Essentials, or other features I think are needed for Minecraft.
	
	This mod is currently supporting **1.17.1**, **1.18.2**, and **1.19.3**
	
	Feel free to contribute by adding as many features as you want!
	
	""";

	@Override
	public void onInitialize() {
		//#if MC >= 11900
		String[] launchArgs = FabricLoader.getInstance().getLaunchArguments(true);

		// Prepare an OptionParser for our parameters
		OptionParser parser = new OptionParser();
		OptionSpec<String> pathSpec = parser.accepts("generate").withRequiredArg();

		// Minecraft may need more stuff later that we don't want to special-case
		parser.allowsUnrecognizedOptions();
		OptionSet options = parser.parse(launchArgs);

		// If our flag isn't set, continue regular launch
		if (!options.has(pathSpec)) {
			return;
		}

		PrintStream outputStream;
		try {
			Path path = Path.of(options.valueOf(pathSpec));
			Files.createDirectories(path.getParent());
			outputStream = new PrintStream(Files.newOutputStream(path));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		Translations.updateLanguage();
		SettingsManager manager = new SettingsManager("1.2.0", "essentialaddons", "EssentialAddons");
		outputStream.println(START);
		manager.dumpAllRulesToStream(outputStream, null);
		outputStream.close();
		System.exit(0);
		//#endif
	}
}
