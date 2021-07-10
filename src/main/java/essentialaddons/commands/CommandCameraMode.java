package essentialaddons.commands;

import essentialaddons.EssentialAddonsSettings;
import essentialaddons.EssentialAddonsUtils;
import carpet.settings.SettingsManager;
import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Box;
import net.minecraft.world.GameMode;
import net.minecraft.world.World;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import static net.minecraft.server.command.CommandManager.literal;

public class CommandCameraMode {

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(literal("cs").requires((player) -> SettingsManager.canUseCommand(player, EssentialAddonsSettings.commandCameraMode)
        ).executes(context -> {
            //variables
            ServerPlayerEntity playerEntity = context.getSource().getPlayer();
            UUID playerUUID = playerEntity.getUuid();
            ServerWorld overworld = context.getSource().getMinecraftServer().getWorld(World.OVERWORLD);
            ServerWorld nether = context.getSource().getMinecraftServer().getWorld(World.NETHER);
            ServerWorld end = context.getSource().getMinecraftServer().getWorld(World.END);
            if (!playerEntity.isSpectator()){
                //checks for cameraModeRestoreLocation
                if (!SettingsManager.canUseCommand(null, EssentialAddonsSettings.cameraModeRestoreLocation)) {
                    playerEntity.setGameMode(GameMode.SPECTATOR);
                    EssentialAddonsUtils.sendToActionBar(playerEntity, "§6You have been put into §aSPECTATOR");
                    return 0;
                }
                //checks for survivalRestrictions carpet rule and whether the player is in danger
                if (SettingsManager.canUseCommand(null, EssentialAddonsSettings.cameraModeSurvivalRestrictions) && playerEntity.interactionManager.getGameMode() == GameMode.SURVIVAL && isInDanger(playerEntity))
                    return 0;
                //sets location to HashMap and File
                setLocationToHashMap(playerEntity, playerUUID, overworld, nether);
                setLocationToFile(playerEntity);
            }
            //checks for cameraModeSurvivalRestrictions
            else if (playerEntity.isSpectator() && !SettingsManager.canUseCommand(null, EssentialAddonsSettings.cameraModeRestoreLocation)) {
                playerEntity.setGameMode(playerEntity.interactionManager.getPreviousGameMode());
                EssentialAddonsUtils.sendToActionBar(playerEntity, "§6You have been put into §a" + playerEntity.interactionManager.getPreviousGameMode());
            }
                //checks if hashmap has location data before accessing file for data
            else if (playerEntity.isSpectator() && dimension.get(playerUUID) != null && x.get(playerUUID) != null && y.get(playerUUID) != null && z.get(playerUUID) != null && yaw.get(playerUUID) != null && pitch.get(playerUUID) != null)
                returnToPreviousGamemode(playerEntity, playerUUID);
            //if hashmap is empty then it will try and get data from file
            else if (playerEntity.isSpectator()) {
                getLocationFromFile(playerEntity, playerUUID, overworld, nether, end);
                returnToPreviousGamemode(playerEntity, playerUUID);
            }
            else {
                //this should never happen
                EssentialAddonsUtils.sendToActionBar(playerEntity, "§c[ERROR] Something went very wrong");
            }
            return 0;
        }));
    }
    //unchecked cast warning
    @SuppressWarnings("unchecked")
    private static void getLocationFromFile(ServerPlayerEntity playerEntity, UUID playerUUID, ServerWorld overworld, ServerWorld nether, ServerWorld end) {
        try {
            EssentialAddonsUtils.directoryExists("world/playerdata/cs");
            FileInputStream fis = new FileInputStream("world/playerdata/cs/" + playerUUID + ".cs");
            ObjectInputStream ois = new ObjectInputStream(fis);
            dim = (HashMap<UUID, String>) ois.readObject();
            x = (HashMap<UUID, Double>) ois.readObject();
            y = (HashMap<UUID, Double>) ois.readObject();
            z = (HashMap<UUID, Double>) ois.readObject();
            yaw = (HashMap<UUID, Float>) ois.readObject();
            pitch = (HashMap<UUID, Float>) ois.readObject();
            ois.close();
            fis.close();
        }
        //this should only happen if player was previously in spectator before rule was enabled
        catch (IOException | ClassNotFoundException e) {
            System.out.println("getLocationFromFileCatch");
            e.printStackTrace();
            EssentialAddonsUtils.sendToActionBar(playerEntity, "§c[ERROR] Unable to get survival location");
            playerEntity.setGameMode(GameMode.SURVIVAL);
        }
        //this just double checks to see if HashMap is empty, error can occur when other HashMaps are saved and it tries to read an non-existent HashMap
        if (dim.get(playerUUID) == null || x.get(playerUUID) == null || y.get(playerUUID) == null || z.get(playerUUID) == null || yaw.get(playerUUID) == null || pitch.get(playerUUID) == null){
            EssentialAddonsUtils.sendToActionBar(playerEntity, "§c[ERROR] Unable to get survival location");
            playerEntity.setGameMode(GameMode.SURVIVAL);
        }
        //decoding the dimension data from earlier
        if (dim.get(playerUUID).equals("overworld"))
            dimension.put(playerUUID, overworld);
        else if (dim.get(playerUUID).equals("nether"))
            dimension.put(playerUUID, nether);
        else
            dimension.put(playerUUID, end);
    }
    private static void setLocationToFile(ServerPlayerEntity playerEntity) {
        try {
            EssentialAddonsUtils.directoryExists("world/playerdata/cs");
            //serialises and saves HashMap into a file
            FileOutputStream fos = new FileOutputStream("world/playerdata/cs/" + playerEntity.getUuid() + ".cs");
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(dim);
            oos.writeObject(x);
            oos.writeObject(y);
            oos.writeObject(z);
            oos.writeObject(yaw);
            oos.writeObject(pitch);
            oos.close();
            fos.close();
            //sets player gamemode to spectator
            playerEntity.setGameMode(GameMode.SPECTATOR);
            EssentialAddonsUtils.sendToActionBar(playerEntity, "§6You have been put into §aSPECTATOR");
        }
        catch (IOException ioe) {
            ioe.printStackTrace();
            EssentialAddonsUtils.sendToActionBar(playerEntity, "§c[ERROR] Unable to save survival location");
        }
    }
    private static void setLocationToHashMap(ServerPlayerEntity playerEntity, UUID playerUUID, ServerWorld overworld, ServerWorld nether) {
        //gets the location of the player and puts it in a hashmap
        dimension.put(playerUUID,playerEntity.getServerWorld());
        x.put(playerUUID,playerEntity.getX());
        y.put(playerUUID,playerEntity.getY());
        z.put(playerUUID,playerEntity.getZ());
        yaw.put(playerUUID,playerEntity.getYaw(1));
        pitch.put(playerUUID,playerEntity.getPitch(1));
        //converting the ServerWorld to a string because it doesn't serialise (probably a much better way of doing this)
        if (playerEntity.getServerWorld() == overworld)
            dim.put(playerUUID, "overworld");
        else if (playerEntity.getServerWorld() == nether)
            dim.put(playerUUID, "nether");
        else
            dim.put(playerUUID, "end");
    }
    private static void returnToPreviousGamemode(ServerPlayerEntity playerEntity, UUID playerUUID) {
        GameMode previousGameMode = playerEntity.interactionManager.getPreviousGameMode();
        playerEntity.teleport(dimension.get(playerUUID),x.get(playerUUID),y.get(playerUUID),z.get(playerUUID),yaw.get(playerUUID),pitch.get(playerUUID));
        playerEntity.setGameMode(previousGameMode);
        EssentialAddonsUtils.sendToActionBar(playerEntity, "§6You have been put into §a" + previousGameMode);
    }
    private static boolean isInDanger(ServerPlayerEntity playerEntity) {
        Box nearPlayer = new Box(playerEntity.getX()-4,playerEntity.getY()-4,playerEntity.getZ()-4,playerEntity.getX()+4,playerEntity.getY()+4,playerEntity.getZ()+4);
        List<HostileEntity> list = playerEntity.world.getEntitiesByClass(HostileEntity.class, nearPlayer, hostileEntity -> true);
        String reason;
        if (playerEntity.isOnFire())
            reason = "you are on fire";
        else if (playerEntity.hasStatusEffect(StatusEffects.POISON) || playerEntity.hasStatusEffect(StatusEffects.WITHER) || playerEntity.hasStatusEffect(StatusEffects.LEVITATION))
            reason = "you have a negative status effect";
        else if (playerEntity.fallDistance > 0)
            reason = "you are falling";
        else if (playerEntity.isFallFlying())
            reason = "you are flying";
        else if (!list.isEmpty())
            reason = "there are mobs nearby";
        else
            return false;
        EssentialAddonsUtils.sendToActionBar(playerEntity, "§cYou cannot enter spectator because " + reason);
        return true;
    }
    //Hashmaps that store player locations
    private static final HashMap<UUID, ServerWorld> dimension = new HashMap<>();
    private static HashMap<UUID, String> dim = new HashMap<>();
    private static HashMap<UUID, Double> x = new HashMap<>();
    private static HashMap<UUID, Double> y = new HashMap<>();
    private static HashMap<UUID, Double> z = new HashMap<>();
    private static HashMap<UUID, Float> yaw = new HashMap<>();
    private static HashMap<UUID, Float> pitch = new HashMap<>();
}
