package essentialaddons.mixins.phantomsObeyMobcap;

import net.minecraft.entity.SpawnGroup;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.SpawnHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(SpawnHelper.Info.class)
public interface InfoInvoker {
	@Invoker("isBelowCap")
	//#if MC >= 11800
	boolean isBelowCap(SpawnGroup group, ChunkPos chunkPos);
	//#else
	//$$boolean isBelowCap(SpawnGroup group);
	//#endif
}
