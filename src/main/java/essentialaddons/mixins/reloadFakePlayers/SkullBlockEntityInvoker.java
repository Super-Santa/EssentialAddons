package essentialaddons.mixins.reloadFakePlayers;

import com.mojang.authlib.GameProfile;
import net.minecraft.block.entity.SkullBlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Mixin(SkullBlockEntity.class)
public interface SkullBlockEntityInvoker {
	@Invoker("fetchProfile")
	static CompletableFuture<Optional<GameProfile>> fetchProfile(String name) {
		throw new AssertionError();
	}
}
