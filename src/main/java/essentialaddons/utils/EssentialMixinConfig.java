package essentialaddons.utils;

import net.fabricmc.loader.api.FabricLoader;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import java.util.List;
import java.util.Set;

public class EssentialMixinConfig implements IMixinConfigPlugin {
	private boolean modInstalled(String id) {
		return FabricLoader.getInstance().isModLoaded(id);
	}

	@Override
	public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
		if (mixinClassName.startsWith("essentialaddons.mixins.combinePotionDuration")) {
			return !this.modInstalled("RPGStats");
		}
		if (mixinClassName.startsWith("essentialaddons.mixins.lithium")) {
			return this.modInstalled("lithium");
		}

		return switch (mixinClassName) {
			case "essentialaddons.mixins.broadcastToAll.ServerCommandSourceMixin" -> !this.modInstalled("player_roles");
			default -> true;
		};
	}

	@Override
	public String getRefMapperConfig() {
		return null;
	}

	@Override
	public List<String> getMixins() {
		return null;
	}

	@Override
	public void onLoad(String mixinPackage) { }

	@Override
	public void acceptTargets(Set<String> myTargets, Set<String> otherTargets) { }

	@Override
	public void preApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) { }

	@Override
	public void postApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) { }
}
