package essentialaddons.commands;

import carpet.patches.EntityPlayerMPFake;
import carpet.settings.SettingsManager;
import carpet.utils.Messenger;
import com.google.common.collect.Sets;
import com.mojang.authlib.GameProfile;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import essentialaddons.EssentialAddonsSettings;
import essentialaddons.helpers.PlayerFakeHelper;
import net.minecraft.command.CommandSource;
import net.minecraft.command.argument.DimensionArgumentType;
import net.minecraft.command.argument.Vec3ArgumentType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.LiteralText;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.World;

import java.util.Arrays;
import java.util.Collection;
import java.util.Set;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public class CommandPlayerFake {

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(literal("playerfake").requires((player) -> SettingsManager.canUseCommand(player, EssentialAddonsSettings.commandPlayerFake))
                .then(argument("player", StringArgumentType.word())
                        .suggests((context, builder) -> CommandSource.suggestMatching(getPlayers(context.getSource()), builder))
                        .then(literal("spawn")
                                .executes(context -> fakePlayerSpawn(context, StringArgumentType.getString(context, "player"), context.getSource().getPlayer().getPos(), null))
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
                                    ServerPlayerEntity playerEntity = context.getSource().getMinecraftServer().getPlayerManager().getPlayer(username);
                                    if (!(playerEntity instanceof EntityPlayerMPFake || playerEntity instanceof PlayerFakeHelper)) {
                                        Messenger.m(context.getSource(), "r Cannot kill this player");
                                        return 0;
                                    }
                                    playerEntity.getServerWorld().getChunkManager().loadEntity(playerEntity);
                                    playerEntity.kill();
                                    return 0;
                                })
                        )
                        /*
                        .then(literal("add").requires(serverCommandSource -> serverCommandSource.hasPermissionLevel(2))
                                .executes(context -> {
                                    String username = StringArgumentType.getString(context, "player");
                                    ServerPlayerEntity playerEntity = context.getSource().getMinecraftServer().getPlayerManager().getPlayer(username);
                                    if (!(playerEntity instanceof EntityPlayerMPFake || playerEntity instanceof PlayerFakeHelper))
                                        Messenger.m(context.getSource(), "r Cannot change this player");
                                    else
                                        playerEntity.getServerWorld().getChunkManager().unloadEntity(playerEntity);
                                    return 0;
                                })
                        )
                        /* Removed because this is kinda bonked and not needed
                        .then(literal("remove").requires(serverCommandSource -> serverCommandSource.hasPermissionLevel(2))
                                .executes(context -> {
                                    String username = StringArgumentType.getString(context, "player");
                                    ServerPlayerEntity playerEntity = context.getSource().getMinecraftServer().getPlayerManager().getPlayer(username);
                                    if (!(playerEntity instanceof EntityPlayerMPFake || playerEntity instanceof PlayerFakeHelper))
                                        Messenger.m(context.getSource(), "r Cannot change this player");
                                    else
                                        playerEntity.getServerWorld().getChunkManager().loadEntity(playerEntity);
                                    return 0;
                                })
                        )
                         */
                )
        );
    }
    private static int fakePlayerSpawn(CommandContext<ServerCommandSource> context, String username, Vec3d pos, RegistryKey<World> dim) throws CommandSyntaxException {
        if (cantSpawn(context))
            return 0;
        ServerCommandSource source = context.getSource();
        dim = dim == null ? source.getWorld().getRegistryKey() : dim;
        ServerPlayerEntity playerEntity = PlayerFakeHelper.createFake(username, source.getMinecraftServer(), pos.x, pos.y, pos.z, source.getPlayer().yaw, source.getPlayer().pitch, dim);
        if (playerEntity == null) {
            source.sendFeedback(new LiteralText("Failed to spawn player"), false);
        }

        return 0;
    }

    private static boolean cantSpawn(CommandContext<ServerCommandSource> context) {
        String playerName = StringArgumentType.getString(context, "player");
        MinecraftServer server = context.getSource().getMinecraftServer();
        PlayerManager manager = server.getPlayerManager();
        PlayerEntity player = manager.getPlayer(playerName);
        if (player != null) {
            Messenger.m(context.getSource(), "r Player ", "rb " + playerName, "r  is already logged on");
            return true;
        }
        GameProfile profile = server.getUserCache().findByName(playerName);
        if (profile == null) {
            Messenger.m(context.getSource(), "r Player "+playerName+" is either banned by Mojang, or auth servers are down. " +
                    "Banned players can only be summoned in Singleplayer and in servers in off-line mode.");
            return true;
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
        MinecraftServer server = context.getSource().getMinecraftServer();
        return server.getPlayerManager().getPlayer(playerName);
    }
}
