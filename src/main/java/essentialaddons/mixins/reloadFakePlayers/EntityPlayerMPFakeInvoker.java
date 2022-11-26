package essentialaddons.mixins.reloadFakePlayers;

import carpet.patches.EntityPlayerMPFake;
import com.mojang.authlib.GameProfile;
//#if MC >= 11900
import net.minecraft.network.encryption.PlayerPublicKey;
//#endif
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(value = EntityPlayerMPFake.class, remap = false)
public interface EntityPlayerMPFakeInvoker {
	//#if MC >= 11900
	@SuppressWarnings("unused")
	@Invoker("<init>")
	static EntityPlayerMPFake init(MinecraftServer server, ServerWorld worldIn, GameProfile profile, boolean shadow, PlayerPublicKey profilePublicKey) {
		throw new AssertionError();
	}
	//#else
	//$$@Invoker("<init>")
	//$$static EntityPlayerMPFake init(MinecraftServer server, ServerWorld worldIn, GameProfile profile, boolean shadow) {
	//$$	throw new AssertionError();
	//$$}
	//#endif
}
