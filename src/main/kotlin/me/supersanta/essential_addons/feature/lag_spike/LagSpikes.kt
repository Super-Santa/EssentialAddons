package me.supersanta.essential_addons.feature.lag_spike

import com.google.common.collect.HashMultimap
import me.supersanta.essential_addons.EssentialAddons

object LagSpikes {
    private val pending = HashMultimap.create<ExactPhase, LagSpike>()

    fun submit(spike: LagSpike) {
        this.pending.put(ExactPhase(spike.phase, spike.sub), spike)
    }

    @JvmStatic
    fun process(phase: LagSpike.TickPhase, sub: LagSpike.SubPhase) {
        val spikes = this.pending.removeAll(ExactPhase(phase, sub))
        for (spike in spikes) {
            try {
                Thread.sleep(spike.duration.inWholeMilliseconds)
            } catch (e: InterruptedException) {
                EssentialAddons.logger.error("Interrupted during lag spike")
                break
            }
        }
    }

    private data class ExactPhase(val phase: LagSpike.TickPhase, val sub: LagSpike.SubPhase)
}