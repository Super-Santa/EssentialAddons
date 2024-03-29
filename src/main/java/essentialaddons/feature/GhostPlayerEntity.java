package essentialaddons.feature;

import carpet.patches.FakeClientConnection;
import carpet.utils.Messenger;
import com.mojang.authlib.GameProfile;
import essentialaddons.EssentialUtils;
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
import net.minecraft.world.GameMode;
import net.minecraft.world.World;

import java.util.concurrent.atomic.AtomicReference;

//#if MC >= 11900 && MC < 11903
//$$import net.minecraft.network.encryption.PlayerPublicKey;
//#endif

//#if MC >= 11903
import net.minecraft.registry.RegistryKey;
//#else
//$$import net.minecraft.util.registry.RegistryKey;
//#endif

public class GhostPlayerEntity extends ServerPlayerEntity {
    //#if MC >= 11900 && MC < 11903
    //$$public GhostPlayerEntity(MinecraftServer server, ServerWorld world, GameProfile profile, PlayerPublicKey publicKey) {
    //$$    super(server, world, profile, publicKey);
    //$$}
    //#else
    public GhostPlayerEntity(MinecraftServer server, ServerWorld world, GameProfile profile) {
        super(server, world, profile);
    }
    //#endif

    public static GhostPlayerEntity createFake(String username, MinecraftServer server, double d0, double d1, double d2, double yaw, double pitch, RegistryKey<World> dimensionId) {
        ServerWorld worldIn = server.getWorld(dimensionId);
        UserCache.setUseRemote(false);
        GameProfile gameprofile;
        try {
            UserCache cache = server.getUserCache();
            if (cache != null) {
                gameprofile = cache.findByName(username).orElse(null);
            } else {
                gameprofile = null;
            }
        } finally {
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
        //#if MC >= 11900 && MC < 11903
        //$$GhostPlayerEntity instance = new GhostPlayerEntity(server, worldIn, gameprofile, null);
        //#else
        GhostPlayerEntity instance = new GhostPlayerEntity(server, worldIn, gameprofile);
        //#endif
        server.getPlayerManager().onPlayerConnect(new FakeClientConnection(NetworkSide.SERVERBOUND), instance);
        instance.teleport(worldIn, d0, d1, d2, (float) yaw, (float) pitch);
        instance.setHealth(20.0F);
        instance.unsetRemoved();
        //#if MC >= 11904
        instance.setStepHeight(0.6F);
        //#else
        //$$instance.stepHeight = 0.6F;
        //#endif
        instance.interactionManager.changeGameMode(GameMode.SPECTATOR);
        server.getPlayerManager().sendToDimension(new EntitySetHeadYawS2CPacket(instance, (byte) (instance.headYaw * 256 / 360)), dimensionId);
        server.getPlayerManager().sendToDimension(new EntityPositionS2CPacket(instance), dimensionId);
        //#if MC < 12000
        //$$instance.getWorld().getChunkManager().updatePosition(instance);
        //#endif
        instance.dataTracker.set(PLAYER_MODEL_PARTS, (byte) 0x7f);
        EssentialUtils.getWorld(instance).getChunkManager().unloadEntity(instance);
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
