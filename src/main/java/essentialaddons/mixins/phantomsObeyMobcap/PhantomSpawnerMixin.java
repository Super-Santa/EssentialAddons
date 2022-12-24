package essentialaddons.mixins.phantomsObeyMobcap;

import essentialaddons.EssentialSettings;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.spawner.PhantomSpawner;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(PhantomSpawner.class)
public class PhantomSpawnerMixin {
	@Redirect(method = "spawn", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerEntity;isSpectator()Z"), require = 0)
	private boolean shouldNotSpawnPhantom(PlayerEntity player, ServerWorld world, boolean spawnMonsters, boolean spawnAnimals) {
		if (player.isSpectator()) {
			return true;
		}
		if (EssentialSettings.phantomsObeyMobcaps) {
			InfoInvoker info = ((InfoInvoker) world.getChunkManager().getSpawnInfo());
			return info != null && !info.isBelowCap(SpawnGroup.MONSTER, player.getChunkPos());
		}
		return false;
	}
}
