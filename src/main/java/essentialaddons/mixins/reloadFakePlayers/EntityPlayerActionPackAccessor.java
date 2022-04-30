package essentialaddons.mixins.reloadFakePlayers;

import carpet.helpers.EntityPlayerActionPack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Map;

@Mixin(value = EntityPlayerActionPack.class, remap = false)
public interface EntityPlayerActionPackAccessor {
	@Accessor("actions")
	Map<EntityPlayerActionPack.ActionType, EntityPlayerActionPack.Action> getActions();

	@Accessor("sneaking")
	boolean isSneaking();

	@Accessor("sprinting")
	boolean isSprinting();

	@Accessor("forward")
	float getForward();

	@Accessor("strafing")
	float getStrafing();
}
