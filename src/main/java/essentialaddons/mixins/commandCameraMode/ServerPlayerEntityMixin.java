package essentialaddons.mixins.commandCameraMode;

import com.mojang.authlib.GameProfile;
import essentialaddons.EssentialSettings;
import essentialaddons.utils.ConfigCameraData;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
//#if MC >= 11900 && MC < 11903
import net.minecraft.network.encryption.PlayerPublicKey;
//#endif
import net.minecraft.network.packet.s2c.play.EntityStatusEffectS2CPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.GameMode;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ServerPlayerEntity.class)
public abstract class ServerPlayerEntityMixin extends PlayerEntity {
	@Shadow
	public ServerPlayNetworkHandler networkHandler;
	@Shadow
	@Final
	public MinecraftServer server;

	//#if MC >= 11900 && MC < 11903
	//$$public ServerPlayerEntityMixin(World world, BlockPos pos, float yaw, GameProfile gameProfile, PlayerPublicKey publicKey) {
	//$$	super(world, pos, yaw, gameProfile, publicKey);
	//$$}
	//#else
	public ServerPlayerEntityMixin(World world, BlockPos pos, float yaw, GameProfile gameProfile) {
		super(world, pos, yaw, gameProfile);
	}
	//#endif


	@Inject(method = "getServerGameMode", at = @At("HEAD"), cancellable = true)
	private void onGetGameMode(GameMode backupGameMode, CallbackInfoReturnable<GameMode> cir) {
		if (EssentialSettings.commandCameraMode && ConfigCameraData.INSTANCE.hasPlayerLocation((ServerPlayerEntity) (Object) this)) {
			cir.setReturnValue(GameMode.SPECTATOR);
		}
	}

	@Override
	protected void tickStatusEffects() {
		if (EssentialSettings.commandCameraMode && this.isSpectator()) {
			if (this.server.getTicks() % 20 == 0){
				for (StatusEffectInstance statusEffectInstance : this.getStatusEffects()) {
					this.networkHandler.sendPacket(new EntityStatusEffectS2CPacket(this.getId(), statusEffectInstance));
				}
			}
			return;
		}
		super.tickStatusEffects();
	}
}
