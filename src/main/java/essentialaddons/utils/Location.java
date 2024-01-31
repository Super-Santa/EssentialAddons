package essentialaddons.utils;

import net.minecraft.entity.Entity;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public record Location(RegistryKey<World> worldRegistry, Vec3d position, float yaw, float pitch) {
	public Location(Entity player) {
		this(player.getWorld().getRegistryKey(), player.getPos(), player.getYaw(), player.getPitch());
	}
}
