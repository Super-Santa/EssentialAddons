package essentialaddons.mixins.phantomsObeyMobcap;

import essentialaddons.utils.ducks.IInfo;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.SpawnHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(SpawnHelper.Info.class)
public abstract class InfoInvoker implements IInfo {
	@Shadow abstract boolean isBelowCap(SpawnGroup par1, ChunkPos par2);

	public boolean essentialaddons$isBelowMobcap(SpawnGroup group, ChunkPos chunkPos) {
		return this.isBelowCap(group, chunkPos);
	}
}
