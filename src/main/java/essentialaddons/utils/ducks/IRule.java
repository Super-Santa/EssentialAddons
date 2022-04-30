package essentialaddons.utils.ducks;

import net.minecraft.server.network.ServerPlayerEntity;

public interface IRule {
	void ruleChanged(ServerPlayerEntity player);
}
