package essentialaddons.mixins.phantomsObeyMobcap;

import essentialaddons.EssentialSettings;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.spawner.PhantomSpawner;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

//#if MC < 11800
//$$import org.spongepowered.asm.mixin.injection.Inject;
//$$import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
//#endif

@Mixin(PhantomSpawner.class)
public class PhantomSpawnerMixin {
	//#if MC >= 11800
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
	//#else
	// For pre 1.18 we can actually cancel earlier and avoid iterating players
	//$$@Inject(method = "spawn", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/world/ServerWorld;getPlayers()Ljava/util/List;", shift = At.Shift.BEFORE), cancellable = true)
	//$$private void onPhantomSpawn(ServerWorld world, boolean spawnMonsters, boolean spawnAnimals, CallbackInfoReturnable<Integer> cir) {
	//$$	if (EssentialSettings.phantomsObeyMobcaps) {
	//$$		InfoInvoker info = ((InfoInvoker) world.getChunkManager().getSpawnInfo());
	//$$		if (info != null && !info.isBelowCap(SpawnGroup.MONSTER)) {
	//$$			cir.setReturnValue(0);
	//$$		}
	//$$	}
	//$$}
	//#endif
}
