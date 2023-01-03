package essentialaddons.utils.ducks;

import net.minecraft.entity.SpawnGroup;
import net.minecraft.util.math.ChunkPos;

public interface IInfo {
    // Weird @Invoker stuff

    //#if MC >= 11800
    boolean isBelowMobcap(SpawnGroup group, ChunkPos chunkPos);
    //#else
    //$$boolean isBelowMobcap(SpawnGroup group);
    //#endif

}
