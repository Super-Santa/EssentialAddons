package essentialaddons.mixins.gameRuleSync;

import essentialaddons.feature.GameRuleNetworkHandler;
import essentialaddons.utils.ducks.IRuleType;
import net.minecraft.world.GameRules;
import net.minecraft.world.GameRules.Category;
import net.minecraft.world.GameRules.Key;
import net.minecraft.world.GameRules.Type;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(GameRules.class)
public class GameRulesMixin {
	@ModifyVariable(method = "register", at = @At("STORE"), ordinal = 0)
	private static Key<?> onMakeKey(Key<?> key) {
		GameRuleNetworkHandler.INSTANCE.addGameRuleKey(key);
		return key;
	}

	@Inject(method = "register", at = @At("HEAD"))
	private static <T extends GameRules.Rule<T>> void onRegister(String name, Category category, Type<T> type, CallbackInfoReturnable<Key<T>> cir) {
		((IRuleType) type).setName(name);
	}
}
