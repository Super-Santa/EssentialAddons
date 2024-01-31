package essentialaddons.commands;

import com.mojang.brigadier.CommandDispatcher;
import essentialaddons.EssentialSettings;
import essentialaddons.utils.ConfigTeamTeleportBlacklist;
import net.minecraft.command.argument.TeamArgumentType;
import net.minecraft.scoreboard.Team;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public class CommandTeamTeleportBlacklist {
	public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
		dispatcher.register(literal("teamteleportblacklist").requires(player -> EssentialSettings.cameraModeTeamTeleportBlacklist && player.hasPermissionLevel(4))
			.then(literal("add")
				.then(argument("team", TeamArgumentType.team())
					.executes(context -> {
						Team team = TeamArgumentType.getTeam(context, "team");
						ConfigTeamTeleportBlacklist.INSTANCE.addBlacklistedTeam(team.getName());
						context.getSource().sendFeedback(() -> {
							return Text.literal("Successfully added team: " + team.getName()).formatted(team.getColor());
						}, false);
						return 1;
					})
				)
			)
			.then(literal("remove")
				.then(argument("team", TeamArgumentType.team())
					.executes(context -> {
						Team team = TeamArgumentType.getTeam(context, "team");
						context.getSource().sendFeedback(() -> {
							if (ConfigTeamTeleportBlacklist.INSTANCE.removeBlacklistedTeam(team.getName())) {
								return Text.literal("Successfully removed team: " + team.getName()).formatted(team.getColor());
							}
							return Text.literal("Failed to remove team: " + team.getName()).formatted(team.getColor());
						}, false);
						return 0;
					})
				)
			)
		);
	}
}
