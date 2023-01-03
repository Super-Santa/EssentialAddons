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


	//#if MC >= 11800
	@Shadow abstract boolean isBelowCap(SpawnGroup par1, ChunkPos par2);
	public boolean isBelowMobcap(SpawnGroup group, ChunkPos chunkPos) {
		return this.isBelowCap(group, chunkPos);
	}
	//#else
	//$$@Shadow abstract boolean isBelowCap(SpawnGroup par1);
	//$$public boolean isBelowMobcap(SpawnGroup group) {
	//$$return this.isBelowCap(group);
	//$$}
	//#endif
}
