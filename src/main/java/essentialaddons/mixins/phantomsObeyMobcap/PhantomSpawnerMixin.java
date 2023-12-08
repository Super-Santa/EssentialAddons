package essentialaddons.mixins.phantomsObeyMobcap;

import essentialaddons.EssentialSettings;
import essentialaddons.utils.ducks.IInfo;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.spawner.PhantomSpawner;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(PhantomSpawner.class)
public class PhantomSpawnerMixin {
	@Redirect(
		method = "spawn",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/server/network/ServerPlayerEntity;isSpectator()Z"
		),
		require = 0
	)
	private boolean shouldNotSpawnPhantom(
		ServerPlayerEntity player,
		ServerWorld world,
		boolean spawnMonsters,
		boolean spawnAnimals
	) {
		if (player.isSpectator()) {
			return true;
		}
		if (EssentialSettings.phantomsObeyMobcaps) {
			IInfo info = ((IInfo) world.getChunkManager().getSpawnInfo());
			return info != null && !info.essentialaddons$isBelowMobcap(SpawnGroup.MONSTER, player.getChunkPos());
		}
		return false;
	}
}
