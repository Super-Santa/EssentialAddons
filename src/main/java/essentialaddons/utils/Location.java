package essentialaddons.utils;

import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.World;

public record Location(RegistryKey<World> worldRegistry, Vec3d position, float yaw, float pitch) {
	public Location(ServerPlayerEntity player) {
		this(player.getWorld().getRegistryKey(), player.getPos(), player.getYaw(), player.getPitch());
	}
}
