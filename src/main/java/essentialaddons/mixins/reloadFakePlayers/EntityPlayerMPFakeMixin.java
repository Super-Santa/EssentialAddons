package essentialaddons.mixins.reloadFakePlayers;

import carpet.patches.EntityPlayerMPFake;
import com.mojang.authlib.GameProfile;
import essentialaddons.EssentialSettings;
import essentialaddons.utils.ConfigFakePlayerData;
import net.minecraft.network.packet.c2s.common.SyncedClientOptions;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableTextContent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = EntityPlayerMPFake.class)
public abstract class EntityPlayerMPFakeMixin extends ServerPlayerEntity {
	public EntityPlayerMPFakeMixin(MinecraftServer server, ServerWorld world, GameProfile profile, SyncedClientOptions clientOptions) {
		super(server, world, profile, clientOptions);
	}

	@Inject(method = "<init>", at = @At("TAIL"), remap = false)
	private void onCreateFakePlayer(
		MinecraftServer server,
		ServerWorld worldIn,
		GameProfile profile,
		SyncedClientOptions cli,
		boolean shadow,
		CallbackInfo ci
	) {
		ConfigFakePlayerData.INSTANCE.addFakePlayer((EntityPlayerMPFake) (Object) this);
	}

	@Inject(method = "kill(Lnet/minecraft/text/Text;)V", at = @At("HEAD"))
	private void onPlayerKill(Text reason, CallbackInfo ci) {
		ConfigFakePlayerData.INSTANCE.removeFakePlayer((EntityPlayerMPFake) (Object) this);
		if (EssentialSettings.fakePlayerDropInventoryOnKill && !(reason instanceof MutableText text && text.getContent() instanceof TranslatableTextContent content && content.getKey().equals("multiplayer.disconnect.duplicate_login"))) {
			this.dropInventory();
		}
	}
}
