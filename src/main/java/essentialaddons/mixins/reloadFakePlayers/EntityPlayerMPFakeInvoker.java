package essentialaddons.mixins.reloadFakePlayers;

import carpet.patches.EntityPlayerMPFake;
import com.mojang.authlib.GameProfile;
import net.minecraft.network.packet.c2s.common.SyncedClientOptions;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(value = EntityPlayerMPFake.class, remap = false)
public interface EntityPlayerMPFakeInvoker {
	@SuppressWarnings("unused")
	@Invoker("<init>")
	static EntityPlayerMPFake init(MinecraftServer server, ServerWorld worldIn, GameProfile profile, SyncedClientOptions cli, boolean shadow) {
		throw new AssertionError();
	}
}
