package essentialaddons.mixins.gameRuleSync;

import essentialaddons.EssentialSettings;
import essentialaddons.EssentialUtils;
import essentialaddons.feature.GameRuleNetworkHandler;
import essentialaddons.utils.ducks.IRule;
import essentialaddons.utils.ducks.IRuleType;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.world.GameRules;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameRules.Rule.class)
public abstract class RuleMixin<T extends GameRules.Rule<T>> implements IRule {
	@Shadow
	@Final
	protected GameRules.Type<T> type;

	@Shadow
	public abstract String serialize();

	@Shadow
	protected abstract void changed(@Nullable MinecraftServer server);

	@Inject(method = "changed", at = @At("HEAD"))
	private void onChanged(MinecraftServer server, CallbackInfo ci) {
		if (EssentialSettings.gameRuleSync) {
			GameRuleNetworkHandler.INSTANCE.onRuleChange(((IRuleType) this.type).essentialaddons$getName(), this.serialize());
		}
	}

	@Override
	public void essentialaddons$ruleChanged(ServerPlayerEntity player) {
		Text text = EssentialUtils.literal("Set Game Rule %s to %s".formatted(((IRuleType) this.type).essentialaddons$getName(), this.serialize()));
		player.server.getPlayerManager().broadcast(text, false);
		this.changed(player.server);
	}
}
