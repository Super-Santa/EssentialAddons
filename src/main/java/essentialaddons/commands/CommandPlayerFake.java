package essentialaddons.commands;

import carpet.CarpetSettings;
import carpet.patches.EntityPlayerMPFake;
import carpet.utils.Messenger;
import com.google.common.collect.Sets;
import com.mojang.authlib.GameProfile;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import essentialaddons.EssentialSettings;
import essentialaddons.EssentialUtils;
import essentialaddons.feature.GhostPlayerEntity;
import net.minecraft.command.CommandSource;
import net.minecraft.command.argument.DimensionArgumentType;
import net.minecraft.command.argument.Vec3ArgumentType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.dynamic.DynamicSerializableUuid;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.World;

import java.util.Arrays;
import java.util.Collection;
import java.util.Set;

//#if MC >= 11900
import static carpet.utils.CommandHelper.canUseCommand;
//#else
// import static carpet.settings.SettingsManager.canUseCommand;
//#endif
import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public class CommandPlayerFake {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(literal("ghostplayer").requires((player) -> canUseCommand(player, EssentialSettings.commandGhostPlayer))
            .then(argument("player", StringArgumentType.word())
                .suggests((context, builder) -> CommandSource.suggestMatching(getPlayers(context.getSource()), builder))
                .then(literal("spawn")
                    .executes(context -> fakePlayerSpawn(context, StringArgumentType.getString(context, "player"), context.getSource().getPlayerOrThrow().getPos(), null))
                    .then(literal("at")
                        .then(argument("pos", Vec3ArgumentType.vec3())
                            .executes(context -> fakePlayerSpawn(context, StringArgumentType.getString(context, "player"), Vec3ArgumentType.getVec3(context, "pos"), null))
                            .then(literal("in")
                                .then(argument("dimension", DimensionArgumentType.dimension())
                                    .executes(context -> fakePlayerSpawn(context, StringArgumentType.getString(context, "player"), Vec3ArgumentType.getVec3(context, "pos"), DimensionArgumentType.getDimensionArgument(context, "dimension").getRegistryKey()))
                                )
                            )
                        )
                    )
                )
                .then(literal("kill")
                    .executes(context -> {
                        String username = StringArgumentType.getString(context, "player");
                        ServerPlayerEntity playerEntity = context.getSource().getServer().getPlayerManager().getPlayer(username);
                        if (!(playerEntity instanceof EntityPlayerMPFake || playerEntity instanceof GhostPlayerEntity)) {
                            Messenger.m(context.getSource(), "r Cannot kill this player");
                            return 0;
                        }
                        playerEntity.getWorld().getChunkManager().loadEntity(playerEntity);
                        playerEntity.kill();
                        return 0;
                    })
                )
            )
        );
    }

    private static int fakePlayerSpawn(CommandContext<ServerCommandSource> context, String username, Vec3d pos, RegistryKey<World> dim) throws CommandSyntaxException {
        if (cantSpawn(context))
            return 0;
        ServerCommandSource source = context.getSource();
        dim = dim == null ? source.getWorld().getRegistryKey() : dim;
        ServerPlayerEntity player = source.getPlayerOrThrow();
        ServerPlayerEntity playerEntity = GhostPlayerEntity.createFake(username, source.getServer(), pos.x, pos.y, pos.z, player.getYaw(), player.getPitch(), dim);
        if (playerEntity == null) {
            source.sendFeedback(EssentialUtils.literal("Failed to spawn player"), false);
        }

        return 0;
    }

    private static boolean cantSpawn(CommandContext<ServerCommandSource> context) {
        String playerName = StringArgumentType.getString(context, "player");
        MinecraftServer server = context.getSource().getServer();
        PlayerManager manager = server.getPlayerManager();
        PlayerEntity player = manager.getPlayer(playerName);
        if (player != null) {
            Messenger.m(context.getSource(), "r Player ", "rb " + playerName, "r  is already logged on");
            return true;
        }
        GameProfile profile = server.getUserCache().findByName(playerName).orElse(null);
        if (profile == null) {
            if (!CarpetSettings.allowSpawningOfflinePlayers) {
                Messenger.m(context.getSource(), "r Player "+playerName+" is either banned by Mojang, or auth servers are down. " +
                    "Banned players can only be summoned in Singleplayer and in servers in off-line mode.");
                return true;
            }
            profile = new GameProfile(DynamicSerializableUuid.getOfflinePlayerUuid(playerName), playerName);
        }
        if (manager.getUserBanList().contains(profile)) {
            Messenger.m(context.getSource(), "r Player ", "rb " + playerName, "r  is banned on this server");
            return true;
        }
        if (manager.isWhitelistEnabled() && manager.isWhitelisted(profile) && !context.getSource().hasPermissionLevel(2)) {
            Messenger.m(context.getSource(), "r Whitelisted players can only be spawned by operators");
            return true;
        }
        return false;
    }

    private static Collection<String> getPlayers(ServerCommandSource source) {
        Set<String> players = Sets.newLinkedHashSet(Arrays.asList("Steve", "Alex"));
        players.addAll(source.getPlayerNames());
        return players;
    }

    public static ServerPlayerEntity getPlayer(CommandContext<ServerCommandSource> context) {
        String playerName = StringArgumentType.getString(context, "player");
        MinecraftServer server = context.getSource().getServer();
        return server.getPlayerManager().getPlayer(playerName);
    }
}
