package essentialaddons.feature;

import carpet.CarpetSettings;
import carpet.patches.EntityPlayerMPFake;
import carpet.patches.FakeClientConnection;
import carpet.utils.Messenger;
import com.mojang.authlib.GameProfile;
import essentialaddons.mixins.reloadFakePlayers.EntityInvoker;
import essentialaddons.mixins.reloadFakePlayers.EntityPlayerMPFakeInvoker;
import essentialaddons.mixins.reloadFakePlayers.SkullBlockEntityInvoker;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.NetworkSide;
import net.minecraft.network.packet.c2s.common.SyncedClientOptions;
import net.minecraft.registry.RegistryKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.ServerTask;
import net.minecraft.server.network.ConnectedClientData;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.UserCache;
import net.minecraft.util.Uuids;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.GameMode;
import net.minecraft.world.World;

import java.util.Objects;

public class GhostPlayerEntity extends ServerPlayerEntity {
    public GhostPlayerEntity(
        MinecraftServer server,
        ServerWorld world,
        GameProfile profile,
        SyncedClientOptions clientOptions
    ) {
        super(server, world, profile, clientOptions);
    }

    public static void createFake(
        String username,
        MinecraftServer server,
        Vec3d pos,
        double yaw,
        double pitch,
        RegistryKey<World> dimensionId,
        Runnable onError
    ) {
        UserCache.setUseRemote(false);
        GameProfile mutable;
        try {
            mutable = Objects.requireNonNull(server.getUserCache()).findByName(username).orElse(null);
        } finally {
            UserCache.setUseRemote(server.isDedicated() && server.isOnlineMode());
        }

        if (mutable == null) {
            if (!CarpetSettings.allowSpawningOfflinePlayers) {
                onError.run();
                return;
            }
            mutable = new GameProfile(Uuids.getOfflinePlayerUuid(username), username);
        }
        GameProfile immutable = mutable;

        SkullBlockEntityInvoker.fetchProfile(immutable.getName()).thenAccept(optional -> {
            GameProfile profile = optional.isPresent() ? optional.get() : immutable;

            EntityPlayerMPFake instance = EntityPlayerMPFakeInvoker.init(
                server,
                server.getOverworld(),
                profile,
                SyncedClientOptions.createDefault(),
                false
            );

            ServerWorld worldIn = server.getWorld(dimensionId);
            server.getPlayerManager().onPlayerConnect(
                new FakeClientConnection(NetworkSide.SERVERBOUND),
                instance,
                new ConnectedClientData(profile, 0, instance.getClientOptions())
            );
            instance.teleport(worldIn, pos.x, pos.y, pos.z, (float) yaw, (float) pitch);
            instance.setHealth(20.0F);
            ((EntityInvoker) instance).unsetRemoved();
            instance.interactionManager.changeGameMode(GameMode.SPECTATOR);
        });
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
