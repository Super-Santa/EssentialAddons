package essentialaddons.feature;

public class LagSpike {
    private static TickPhase pendingLagPhase;
    private static PrePostSubPhase pendingLagSubPhase;
    private static long pendingLagTime;

    public static int addLagSpike(TickPhase phase, PrePostSubPhase subPhase, long millis) {
        pendingLagPhase = phase;
        pendingLagSubPhase = subPhase;
        pendingLagTime = millis;
        return 0;
    }

    /**
     * Safe to call without carpet rule as {@link #pendingLagPhase} is always {@code null} in vanilla
     */
    public static void processLagSpikes(TickPhase phase, Enum<?> subPhase) {
        if (phase == pendingLagPhase && subPhase == pendingLagSubPhase) {
            try {
                Thread.sleep(pendingLagTime);
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }
            pendingLagPhase = null;
            pendingLagSubPhase = null;
        }
    }

    public enum TickPhase {
        PLAYER,
        MOB_SPAWNING,
        CHUNK_UNLOADING,
        TILE_TICK,
        RANDOM_TICK,
        BLOCK_EVENT,
        ENTITY,
        OTHER_ENTITY,
        TILE_ENTITY,
        AUTOSAVE,
        TICK
    }

    public enum PrePostSubPhase {
        PRE, POST
    }
}