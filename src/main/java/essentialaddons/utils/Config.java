package essentialaddons.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import essentialaddons.EssentialAddons;
import essentialaddons.EssentialUtils;
import net.fabricmc.api.EnvType;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.util.WorldSavePath;
import org.apache.commons.lang3.exception.ExceptionUtils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static essentialaddons.EssentialAddons.*;

public interface Config {
	Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();

	String getConfigName();

	Path getConfigPath();

	JsonArray getSaveData();

	void readConfig();

	default Path getConfigRootPath() {
		FabricLoader fabricLoader = FabricLoader.getInstance();
		Path root = (fabricLoader.getEnvironmentType() == EnvType.SERVER ?
			fabricLoader.getConfigDir() : server.getSavePath(WorldSavePath.ROOT)).resolve("EssentialAddons");
		if (!Files.exists(root)) {
			EssentialUtils.throwAsRuntime(() -> Files.createDirectory(root));
		}
		return root;
	}

	default JsonArray getConfigData() {
		Path configPath = this.getConfigPath();
		if (Files.isRegularFile(configPath)) {
			try (BufferedReader reader = Files.newBufferedReader(configPath)) {
				return this.GSON.fromJson(reader, JsonArray.class);
			}
			catch (IOException e) {
				LOGGER.error("Failed to read '{}': {}", this.getConfigName(), e);
			}
		}
		return new JsonArray(0);
	}

	default void saveConfig() {
		Path configPath = this.getConfigPath();
		try (BufferedWriter writer = Files.newBufferedWriter(configPath)) {
			this.GSON.toJson(this.getSaveData(), writer);
		}
		catch (IOException e) {
			LOGGER.error("Failed to save '{}': {}", this.getConfigName(), e);
		}
	}
}
