package essentialaddons.utils;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import essentialaddons.EssentialAddons;

import java.nio.file.Path;

public class ConfigCamera implements Config {
	public static final ConfigCamera INSTANCE = new ConfigCamera();

	public String commandName = "cs";

	private ConfigCamera() {

	}

	@Override
	public String getConfigName() {
		return "CameraModeConfig";
	}

	@Override
	public Path getConfigPath() {
		return this.getConfigRootPath().resolve("cameramodeconfig.json");
	}

	@Override
	public JsonArray getSaveData() {
		JsonArray array = new JsonArray();
		JsonObject object = new JsonObject();
		object.addProperty("commandName", this.commandName);
		array.add(object);
		return array;
	}

	@Override
	public void readConfig(JsonArray configData) {
		try {
			if (configData.size() != 0) {
				this.commandName = configData.get(0).getAsJsonObject().get("commandName").getAsString();
			}
		} catch (RuntimeException e) {
			EssentialAddons.LOGGER.error(e);
		}
	}
}
