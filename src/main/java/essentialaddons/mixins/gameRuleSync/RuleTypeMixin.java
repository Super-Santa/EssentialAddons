package essentialaddons.mixins.gameRuleSync;

import essentialaddons.utils.ducks.IRuleType;
import net.minecraft.world.GameRules;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(GameRules.Type.class)
public class RuleTypeMixin implements IRuleType {
	@Unique
	private String ruleName;

	@Override
	public void setName(String name) {
		this.ruleName = name;
	}

	@Override
	public String getName() {
		return this.ruleName;
	}
}
