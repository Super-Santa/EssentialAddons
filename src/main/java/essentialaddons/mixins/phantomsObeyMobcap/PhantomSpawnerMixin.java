package essentialaddons.mixins.phantomsObeyMobcap;

import essentialaddons.EssentialSettings;
import essentialaddons.utils.ducks.IInfo;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
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
	@Redirect(
		method = "spawn",
		at = @At(
			value = "INVOKE",
			//#if MC >= 12000
			target = "Lnet/minecraft/server/network/ServerPlayerEntity;isSpectator()Z"
			//#else
			//$$target = "Lnet/minecraft/entity/player/PlayerEntity;isSpectator()Z"
			//#endif
		),
		require = 0
	)
	private boolean shouldNotSpawnPhantom(
		//#if MC >= 12000
		ServerPlayerEntity player,
		//#else
		//$$PlayerEntity player,
		//#endif
		ServerWorld world,
		boolean spawnMonsters,
		boolean spawnAnimals
	) {
		if (player.isSpectator()) {
			return true;
		}
		if (EssentialSettings.phantomsObeyMobcaps) {
			IInfo info = ((IInfo) world.getChunkManager().getSpawnInfo());
			return info != null && !info.isBelowMobcap(SpawnGroup.MONSTER, player.getChunkPos());
		}
		return false;
	}
	//#else
	// For pre 1.18 we can actually cancel earlier and avoid iterating players
	//$$@Inject(method = "spawn", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/world/ServerWorld;getPlayers()Ljava/util/List;", shift = At.Shift.BEFORE), cancellable = true)
	//$$private void onPhantomSpawn(ServerWorld world, boolean spawnMonsters, boolean spawnAnimals, CallbackInfoReturnable<Integer> cir) {
	//$$	if (EssentialSettings.phantomsObeyMobcaps) {
	//$$		IInfo info = ((IInfo) world.getChunkManager().getSpawnInfo());
	//$$		if (info != null && !info.isBelowMobcap(SpawnGroup.MONSTER)) {
	//$$			cir.setReturnValue(0);
	//$$		}
	//$$	}
	//$$}
	//#endif
}
