package essentialaddons.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import essentialaddons.utils.ConfigCamera;
import net.minecraft.server.command.ServerCommandSource;

import static essentialaddons.EssentialUtils.op;
import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public class CommandConfig {
	public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
		dispatcher.register(literal("config").requires(op("essentialaddons.command.config"))
			.then(literal("camera")
				.then(argument("name", StringArgumentType.word())
					.executes(c -> {
						ConfigCamera.INSTANCE.commandName = c.getArgument("name", String.class);
						return 1;
					})
				)
			)
		);
	}
}
