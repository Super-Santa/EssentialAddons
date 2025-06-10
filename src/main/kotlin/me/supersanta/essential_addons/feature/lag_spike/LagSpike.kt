package me.supersanta.essential_addons.feature.lag_spike

import kotlin.time.Duration

data class LagSpike(
    val phase: TickPhase,
    val sub: SubPhase,
    val duration: Duration
) {
    enum class TickPhase(val pretty: String) {
        Player("player_movement"),
        MobSpawning("mob_spawning"),
        ChunkUnloading("chunk_unloading"),
        BlockTick("block_ticking"),
        RandomTick("random_ticking"),
        BlockEvent("block_events"),
        EntityTick("entity_ticking"),
        Autosave("autosave"),
        Tick("ticking")
    }

    enum class SubPhase {
        Pre, Post
    }
}
