package essentialaddons.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import essentialaddons.EssentialSettings;
import essentialaddons.EssentialUtils;
import essentialaddons.mixins.core.MinecraftServerAccessor;
import net.minecraft.command.CommandSource;
import net.minecraft.command.argument.DimensionArgumentType;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;

import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import static essentialaddons.EssentialUtils.enabled;
import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public class CommandBackup {
	// Ngl one of the worst looking command trees...
	public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
		dispatcher.register(literal("backup").requires(enabled(() -> EssentialSettings.commandBackup, "essentialaddons.command.backup"))
			.then(argument("regionxfrom", IntegerArgumentType.integer())
				.suggests((c, b) -> getPlayerRegion(c, b, true))
				.then(argument("regionzfrom", IntegerArgumentType.integer())
					.suggests((c, b) -> getPlayerRegion(c, b, false))
					.executes(context -> {
						int x = context.getArgument("regionxfrom", Integer.class);
						int z = context.getArgument("regionzfrom", Integer.class);
						saveRegions(context, x, z, x, z, context.getSource().getWorld(), false);
						return 1;
					})
					.then(argument("regionxto", IntegerArgumentType.integer())
						.suggests((c, b) -> getPlayerRegion(c, b, true))
						.then(argument("regionzto", IntegerArgumentType.integer())
							.suggests((c, b) -> getPlayerRegion(c, b, false))
							.executes(context -> {
								int x1 = context.getArgument("regionxfrom", Integer.class);
								int z1 = context.getArgument("regionzfrom", Integer.class);
								int x2 = context.getArgument("regionxto", Integer.class);
								int z2 = context.getArgument("regionzto", Integer.class);
								saveRegions(context, x1, z1, x2, z2, context.getSource().getWorld(), false);
								return 1;
							})
							.then(argument("world", DimensionArgumentType.dimension())
								.executes(context -> {
									int x1 = context.getArgument("regionxfrom", Integer.class);
									int z1 = context.getArgument("regionzfrom", Integer.class);
									int x2 = context.getArgument("regionxto", Integer.class);
									int z2 = context.getArgument("regionzto", Integer.class);
									ServerWorld world = DimensionArgumentType.getDimensionArgument(context, "world");
									saveRegions(context, x1, z1, x2, z2, world, false);
									return 1;
								})
								.then(argument("replace", BoolArgumentType.bool())
									.executes(context -> {
										int x1 = context.getArgument("regionxfrom", Integer.class);
										int z1 = context.getArgument("regionzfrom", Integer.class);
										int x2 = context.getArgument("regionxto", Integer.class);
										int z2 = context.getArgument("regionzto", Integer.class);
										boolean shouldOverwrite = context.getArgument("replace", Boolean.class);
										ServerWorld world = DimensionArgumentType.getDimensionArgument(context, "world");
										saveRegions(context, x1, z1, x2, z2, world, shouldOverwrite);
										return 1;
									})
								)
							)
						)
					)
				)
			)
		);
	}

	private static CompletableFuture<Suggestions> getPlayerRegion(CommandContext<ServerCommandSource> context, SuggestionsBuilder builder, boolean xAxis) throws CommandSyntaxException {
		ServerPlayerEntity playerEntity = context.getSource().getPlayerOrThrow();
		int position = (int) Math.floor((xAxis ? playerEntity.getX() : playerEntity.getZ()) / 512);
		return CommandSource.suggestMatching(List.of(String.valueOf(position)), builder);
	}

	private static void saveRegions(CommandContext<ServerCommandSource> context, int fromX, int fromZ, int toX, int toZ, ServerWorld world, boolean overwrite) {
		ServerCommandSource source = context.getSource();
		MinecraftServer server = context.getSource().getServer();
		server.save(true, true, true);

		Path savePath = EssentialUtils.getSavePath().resolve("backups").resolve(LocalDate.now().toString()).resolve(world.getRegistryKey().getValue().getPath());
		if (!Files.exists(savePath)) {
			EssentialUtils.throwAsRuntime(() -> Files.createDirectories(savePath));
		}

		Path fromPath = ((MinecraftServerAccessor) server).getSession().getWorldDirectory(world.getRegistryKey()).resolve("region");

		if (!Files.exists(fromPath)) {
			EssentialUtils.sendRawFeedback(source, false, "World has no such regions");
			return;
		}

		DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH.mm.ss");

		boolean successful = false;
		Iterable<BlockPos> iterable = BlockPos.iterate(new BlockPos(fromX, 0, fromZ), new BlockPos(toX, 0, toZ));
		for (BlockPos pos : iterable) {
			int x = pos.getX();
			int z = pos.getZ();
			String fileName = "r.%d.%d.mca".formatted(x, z);
			Path region = fromPath.resolve(fileName);
			if (Files.exists(region)) {
				Path savePathRegion = savePath.resolve(fileName);
				EssentialUtils.throwAsRuntime(() -> {
					if (Files.exists(savePathRegion)) {
						if (overwrite) {
							Files.deleteIfExists(savePathRegion);
						} else {
							Files.move(savePathRegion, savePath.resolve(LocalTime.now().format(timeFormatter) + "_" + fileName ));
						}
					}
					Files.copy(region, savePath.resolve(fileName));
				});
				successful = true;
			}
		}

		if (successful) {
			source.sendFeedback(() -> {
				return Text.literal("Successfully saved regions to: ").formatted(Formatting.GOLD)
					.append(Text.literal(savePath.toString()).formatted(Formatting.GREEN));
			}, true);
		} else {
			source.sendFeedback(() -> {
				return Text.literal("Failed to save regions").formatted(Formatting.RED);
			}, true);
		}
	}
}
