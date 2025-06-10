package me.supersanta.essential_addons.mixins.feature.reload_fake_players;

import carpet.patches.EntityPlayerMPFake;
import com.mojang.authlib.GameProfile;
import me.supersanta.essential_addons.EssentialSettings;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ClientInformation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = EntityPlayerMPFake.class)
public abstract class EntityPlayerMPFakeMixin extends ServerPlayer {
	@Unique private static final String DUPLICATE_LOGIN_KEY = "multiplayer.disconnect.duplicate_login";

	public EntityPlayerMPFakeMixin(
		MinecraftServer server,
		ServerLevel level,
		GameProfile gameProfile,
		ClientInformation clientInformation
	) {
		super(server, level, gameProfile, clientInformation);
	}

	@Inject(method = "kill(Lnet/minecraft/network/chat/Component;)V", at = @At("HEAD"))
	private void onPlayerKill(Component reason, CallbackInfo ci) {
		if (EssentialSettings.fakePlayerDropInventoryOnKill) {
			if (!(reason.getContents() instanceof TranslatableContents content && content.getKey().equals(DUPLICATE_LOGIN_KEY))) {
				this.dropEquipment(this.serverLevel());
			}
		}
	}
}
