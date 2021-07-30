package essentialaddons.helpers;

import carpet.fakes.ServerPlayerEntityInterface;
import carpet.helpers.EntityPlayerActionPack;
import carpet.patches.EntityPlayerMPFake;
import essentialaddons.EssentialAddonsServer;
import essentialaddons.EssentialAddonsSettings;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.WorldSavePath;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.GameMode;
import net.minecraft.world.World;
import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class ReloadFakePlayers {

    public static Map<String, String[]> fakePlayerData = new HashMap<>();
    //attack , attackInterval , use , useInterval , jump , jumpInterval , sneaking , sprinting, forward, strafing
    //private static final String[] DEFAULT_DATA = {"f", "0", "f", "0", "f", "0", "f", "f", "0.0", "0.0"};

    public static void loadFakePlayers(MinecraftServer server) {
        Path fakePlayerFile = getFile(server);
        if (!Files.exists(fakePlayerFile))
            return;
        try {
            BufferedReader reader = Files.newBufferedReader(fakePlayerFile);
            String line;
            while ((line = reader.readLine()) != null) {
                reloadFakePlayer(line, server);
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void saveFakePlayers(MinecraftServer server) {
        Path fakePlayerFile = getFile(server);
        List<ServerPlayerEntity> playerEntities = server.getPlayerManager().getPlayerList();
        List<ServerPlayerEntity> fakePlayerEntities = new LinkedList<>();
        for (ServerPlayerEntity playerEntity: playerEntities) {
            if (playerEntity instanceof EntityPlayerMPFake) {
                fakePlayerEntities.add(playerEntity);
            }
        }
        if (fakePlayerEntities.isEmpty()) {
            try {
                Files.deleteIfExists(fakePlayerFile);
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
        else {
            try (BufferedWriter writer = Files.newBufferedWriter(fakePlayerFile)) {
                for (ServerPlayerEntity playerEntity : fakePlayerEntities) {
                    String username = playerEntity.getEntityName();
                    fakePlayerData.putIfAbsent(username, new String[]{"f", "0", "f", "0", "f", "0", "f", "f", "0.0", "0.0"});
                    addAction(username, 6, -1, playerEntity.isSneaking());
                    addAction(username, 7, -1, playerEntity.isSprinting());
                    writer.write(username + "/" + StringUtils.join(fakePlayerData.get(username), ":") + "\n");
                }
            }
            catch (IOException e) {
                e.printStackTrace();
                EssentialAddonsServer.LOGGER.error("Failed to write fakeplayer data file");
            }
        }
    }
    private static void reloadFakePlayer(String line, MinecraftServer server) {
        String[] data = line.split("/");
        String[] fakeData = data[1].split(":");
        String username = data[0];
        BlockPos spawnPoint = server.getOverworld().getSpawnPos();

        if (server.getPlayerManager().getPlayer(username) != null)
            return;
        EntityPlayerMPFake.createFake(username, server, spawnPoint.getX(), 512, spawnPoint.getZ(), 0, 0, server.getOverworld().getRegistryKey(), GameMode.SURVIVAL, false);
        ServerPlayerEntity playerEntity = server.getPlayerManager().getPlayer(username);
        if (playerEntity == null)
            return;
        NbtCompound playerData = server.getPlayerManager().loadPlayerData(playerEntity);
        if (playerData == null)
            return;

        String[] pos = Objects.requireNonNull(playerData.get("Pos")).asString().replaceAll("d", "").replace("[", "").replace("]", "").split(",");
        String[] rotation = Objects.requireNonNull(playerData.get("Rotation")).asString().replaceAll("f", "").replace("[", "").replace("]", "").split(",");
        ServerWorld world = checkWorld(Objects.requireNonNull(playerData.get("Dimension")).asString(), server);

        playerEntity.teleport(world, Double.parseDouble(pos[0]), Double.parseDouble(pos[1]), Double.parseDouble(pos[2]), Float.parseFloat(rotation[0]), Float.parseFloat(rotation[1]));
        playerEntity.changeGameMode(checkMode(playerData.getInt("playerGameType")));
        playerEntity.getServerWorld().getChunkManager().updatePosition(playerEntity);

        if (playerEntity.getX() == spawnPoint.getX() && playerEntity.getZ() == spawnPoint.getZ() && playerEntity.getY() == 512) {
            EssentialAddonsServer.LOGGER.warn("Failed to load location for " + username);
            playerEntity.kill();
            return;
        }
        if (EssentialAddonsSettings.reloadFakePlayerActions)
            reloadActions(playerEntity, fakeData);
        EssentialAddonsServer.LOGGER.info("Loaded location successfully for " + username);
    }
    private static void reloadActions(ServerPlayerEntity playerEntity, String[] fakeData) {
        fakePlayerData.put(playerEntity.getEntityName(), fakeData);
        EntityPlayerActionPack actionPack = ((ServerPlayerEntityInterface) playerEntity).getActionPack();
        if (fakeData[0].equals("t") && Integer.parseInt(fakeData[1]) > 0)
            actionPack.start(EntityPlayerActionPack.ActionType.ATTACK, EntityPlayerActionPack.Action.interval(Integer.parseInt(fakeData[1])));
        if (fakeData[2].equals("t") && Integer.parseInt(fakeData[3]) > 0)
            actionPack.start(EntityPlayerActionPack.ActionType.USE, EntityPlayerActionPack.Action.interval(Integer.parseInt(fakeData[3])));
        if (fakeData[4].equals("t") && Integer.parseInt(fakeData[5]) > 0)
            actionPack.start(EntityPlayerActionPack.ActionType.JUMP, EntityPlayerActionPack.Action.interval(Integer.parseInt(fakeData[5])));
        if (fakeData[6].equals("t"))
            actionPack.setSneaking(true);
        if (fakeData[7].equals("t"))
            actionPack.setSprinting(true);
        if (!fakeData[8].equals("0.0"))
            actionPack.setForward(Float.parseFloat(fakeData[8]));
        if (!fakeData[9].equals("0.0"))
            actionPack.setStrafing(Float.parseFloat(fakeData[9]));
    }

    private static ServerWorld checkWorld(String dimension, MinecraftServer server){
        return switch (dimension) {
            case "minecraft:the_nether" -> server.getWorld(World.NETHER);
            case "minecraft:the_end" -> server.getWorld(World.END);
            default -> server.getWorld(World.OVERWORLD);
        };
    }
    private static GameMode checkMode (int gameMode) {
        return switch (gameMode) {
            case 1 -> GameMode.CREATIVE;
            case 2 -> GameMode.ADVENTURE;
            case 3 -> GameMode.SPECTATOR;
            default -> GameMode.SURVIVAL;
        };
    }
    private static Path getFile(MinecraftServer server) {
        if (server.isDedicated())
            return server.getSavePath(WorldSavePath.ROOT).getParent().getParent().resolve("config").resolve("fakeplayer.conf");
        else
            return server.getSavePath(WorldSavePath.ROOT).resolve("fakeplayer.conf");
    }
    public static void passAction(String username, EntityPlayerActionPack.ActionType type, EntityPlayerActionPack.Action action) {
        if (action.limit != -1)
            return;
        switch (type) {
            case ATTACK -> addAction(username, 0, action.interval, true);
            case USE -> addAction(username, 2, action.interval, true);
            case JUMP -> addAction(username, 4, action.interval, true);
        }
    }
    public static void passStop(String username) {
        fakePlayerData.put(username, new String[]{"f", "0", "f", "0", "f", "0", "f", "f", "0.0", "0.0"});
    }
    private static void addAction(String username, int action, int interval, boolean bool) {
        String[] data = fakePlayerData.get(username);
        if (data == null)
            data = new String[]{"f", "0", "f", "0", "f", "0", "f", "f", "0.0", "0.0"};
        if (bool) {
            data[action] = "t";
            if (interval != -1)
                data[action + 1] = String.valueOf(interval);
        }
        else
            data[action] = "f";
        fakePlayerData.put(username, data);
    }

    public static void addMovement(String username, int action, float value) {
        String[] data = fakePlayerData.remove(username);
        if (data == null)
            data = new String[]{"f", "0", "f", "0", "f", "0", "f", "f", "0.0", "0.0"};
        data[action] = String.valueOf(value);
        fakePlayerData.put(username, data);
    }
}
