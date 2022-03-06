package essentialaddons.feature;

import carpet.patches.FakeClientConnection;
import carpet.utils.Messenger;
import com.mojang.authlib.GameProfile;
import net.minecraft.block.entity.SkullBlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.NetworkSide;
import net.minecraft.network.packet.s2c.play.EntityPositionS2CPacket;
import net.minecraft.network.packet.s2c.play.EntitySetHeadYawS2CPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.ServerTask;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.UserCache;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.GameMode;
import net.minecraft.world.World;

import java.util.concurrent.atomic.AtomicReference;

public class PlayerFakeEntity extends ServerPlayerEntity {

    public Runnable fixStartingPosition = () -> { };

    public PlayerFakeEntity(MinecraftServer server, ServerWorld world, GameProfile profile) {
        super(server, world, profile);
    }

    public static PlayerFakeEntity createFake(String username, MinecraftServer server, double d0, double d1, double d2, double yaw, double pitch, RegistryKey<World> dimensionId) {
        ServerWorld worldIn = server.getWorld(dimensionId);
        UserCache.setUseRemote(false);
        GameProfile gameprofile;
        try {
            gameprofile = server.getUserCache().findByName(username).orElse(null);
        }
        finally {
            UserCache.setUseRemote(server.isDedicated() && server.isOnlineMode());
        }
        if (gameprofile == null) {
            return null;
        }
        if (gameprofile.getProperties().containsKey("textures")) {
            AtomicReference<GameProfile> result = new AtomicReference<>();
            SkullBlockEntity.loadProperties(gameprofile, result::set);
            gameprofile = result.get();
        }
        PlayerFakeEntity instance = new PlayerFakeEntity(server, worldIn, gameprofile);
        instance.fixStartingPosition = () -> instance.refreshPositionAndAngles(d0, d1, d2, (float) yaw, (float) pitch);
        server.getPlayerManager().onPlayerConnect(new FakeClientConnection(NetworkSide.SERVERBOUND), instance);
        instance.teleport(worldIn, d0, d1, d2, (float) yaw, (float) pitch);
        instance.setHealth(20.0F);
        instance.unsetRemoved();
        instance.stepHeight = 0.6F;
        instance.interactionManager.changeGameMode(GameMode.SPECTATOR);
        server.getPlayerManager().sendToDimension(new EntitySetHeadYawS2CPacket(instance, (byte) (instance.headYaw * 256 / 360)), dimensionId);
        server.getPlayerManager().sendToDimension(new EntityPositionS2CPacket(instance), dimensionId);
        instance.getWorld().getChunkManager().updatePosition(instance);
        instance.dataTracker.set(PLAYER_MODEL_PARTS, (byte) 0x7f);
        instance.getWorld().getChunkManager().unloadEntity(instance);
        return instance;
    }

    @Override
    public void kill() {
        this.kill(Messenger.s("Killed"));
    }

    public void kill(Text reason) {
        this.shakeOff();
        this.server.send(new ServerTask(this.server.getTicks(), () -> {
            this.networkHandler.onDisconnected(reason);
        }));
    }

    private void shakeOff() {
        if (this.getVehicle() instanceof PlayerEntity) {
            this.stopRiding();
        }
        for (Entity passenger : this.getPassengersDeep()) {
            if (passenger instanceof PlayerEntity) {
                passenger.stopRiding();
            }
        }
    }
}
