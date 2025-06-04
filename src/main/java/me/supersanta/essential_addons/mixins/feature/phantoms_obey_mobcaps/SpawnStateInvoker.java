package me.supersanta.essential_addons.mixins.feature.phantoms_obey_mobcaps;

import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.NaturalSpawner;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(NaturalSpawner.SpawnState.class)
public interface SpawnStateInvoker {
    @Invoker("canSpawnForCategoryLocal")
    boolean isBelowLocalMobcap(MobCategory category, ChunkPos chunkPos);
}
