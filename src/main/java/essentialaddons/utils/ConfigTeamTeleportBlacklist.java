package essentialaddons.utils;

import com.google.gson.JsonArray;
import com.google.gson.JsonPrimitive;

import java.nio.file.Path;
import java.util.HashSet;

public class ConfigTeamTeleportBlacklist implements Config {
	public static final ConfigTeamTeleportBlacklist INSTANCE = new ConfigTeamTeleportBlacklist();

	private final HashSet<String> teams = new HashSet<>();

	public void addBlacklistedTeam(String teamName) {
		this.teams.add(teamName);
	}

	public boolean removeBlacklistedTeam(String teamName) {
		return this.teams.remove(teamName);
	}

	public boolean isTeamBlacklisted(String teamName) {
		return this.teams.contains(teamName);
	}

	@Override
	public String getConfigName() {
		return "TeamTeleportBlacklist";
	}

	@Override
	public Path getConfigPath() {
		return this.getConfigRootPath().resolve("teamteleportblacklist.json");
	}

	@Override
	public JsonArray getSaveData() {
		return this.teams.stream()
			.map(JsonPrimitive::new)
			.collect(JsonArray::new, JsonArray::add, JsonArray::addAll);
	}

	@Override
	public void readConfig(JsonArray configData) {
		configData.forEach(element -> {
			if (element.isJsonPrimitive()) {
				this.teams.add(element.getAsString());
			}
		});
	}
}
