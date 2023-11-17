package essentialaddons.feature;

import carpet.fakes.ServerPlayerInterface;
import carpet.helpers.EntityPlayerActionPack;
import carpet.helpers.EntityPlayerActionPack.ActionType;
import carpet.patches.EntityPlayerMPFake;
import carpet.patches.FakeClientConnection;
import com.mojang.authlib.GameProfile;
import essentialaddons.EssentialSettings;
import essentialaddons.mixins.reloadFakePlayers.EntityInvoker;
import essentialaddons.mixins.reloadFakePlayers.EntityPlayerActionPackAccessor;
import essentialaddons.mixins.reloadFakePlayers.EntityPlayerMPFakeInvoker;
import essentialaddons.mixins.reloadFakePlayers.SkullBlockEntityInvoker;
import essentialaddons.utils.ConfigFakePlayerData;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.NetworkSide;
import net.minecraft.network.packet.c2s.common.SyncedClientOptions;
import net.minecraft.network.packet.s2c.play.EntityPositionS2CPacket;
import net.minecraft.network.packet.s2c.play.EntitySetHeadYawS2CPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ConnectedClientData;
import net.minecraft.util.UserCache;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import static carpet.helpers.EntityPlayerActionPack.Action;

public abstract class ReloadFakePlayers extends PlayerEntity {
    public ReloadFakePlayers(World world, BlockPos pos, float yaw, GameProfile gameProfile) {
        super(world, pos, yaw, gameProfile);
    }

    public static void loadFakePlayers(MinecraftServer server) {
        ConfigFakePlayerData.INSTANCE.readConfig(server);
    }

    public static void loadPlayer(
        MinecraftServer server,
        UUID uuid,
        String username,
        boolean sneaking,
        boolean sprinting,
        float forward,
        float strafing,
        Map<ActionType, Action> actionMap
    ) {
        UserCache.setUseRemote(false);
        GameProfile mutable;
        try {
            mutable = Objects.requireNonNull(server.getUserCache()).findByName(username).orElse(null);
        } finally {
            UserCache.setUseRemote(server.isDedicated() && server.isOnlineMode());
        }

        if (mutable == null) {
			mutable = new GameProfile(uuid, username);
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
            if (EssentialSettings.reloadFakePlayerActions) {
                EntityPlayerActionPack actionPack = ((ServerPlayerInterface) instance).getActionPack();
                actionPack.setSneaking(sneaking).setSprinting(sprinting).setForward(forward).setStrafing(strafing);
                ((EntityPlayerActionPackAccessor) actionPack).getActions().putAll(actionMap);
            }

            server.getPlayerManager().onPlayerConnect(
                new FakeClientConnection(NetworkSide.SERVERBOUND),
                instance,
                new ConnectedClientData(profile, 0, instance.getClientOptions())
            );

            ((EntityInvoker) instance).unsetRemoved();
            instance.setStepHeight(0.6F);
            server.getPlayerManager().sendToDimension(
                new EntitySetHeadYawS2CPacket(instance, (byte) (instance.headYaw * 256 / 360)), instance.getServerWorld().getRegistryKey()
            );
            server.getPlayerManager().sendToDimension(
                new EntityPositionS2CPacket(instance), instance.getServerWorld().getRegistryKey()
            );
            instance.getDataTracker().set(PLAYER_MODEL_PARTS, (byte) 0x7f);
        });
    }
}
