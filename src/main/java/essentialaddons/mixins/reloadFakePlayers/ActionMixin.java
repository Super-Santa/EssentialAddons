package essentialaddons.mixins.reloadFakePlayers;

import carpet.helpers.EntityPlayerActionPack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(value = EntityPlayerActionPack.Action.class, remap = false)
public interface ActionMixin {
	@SuppressWarnings("unused")
	@Invoker("<init>")
	static EntityPlayerActionPack.Action init(int limit, int interval, int offset, boolean continuous) {
		throw new AssertionError();
	}

	@Accessor("count")
	int getCount();

	@Accessor("next")
	int getNext();

	@Accessor("isContinuous")
	boolean isContinuous();

	@Accessor("count")
	void setCount(int count);

	@Accessor("next")
	void setNext(int next);
}
