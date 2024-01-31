package essentialaddons.utils.ducks;

import net.minecraft.entity.SpawnGroup;
import net.minecraft.util.math.ChunkPos;

public interface IInfo {
    boolean essentialaddons$isBelowMobcap(SpawnGroup group, ChunkPos chunkPos);
}
