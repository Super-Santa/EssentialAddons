package essentialaddons.helpers;

public class LagSpikeHelper {

    //private static @Nullable DimensionType pendingLagDimension;
    private static TickPhase pendingLagPhase;
    private static Enum<?> pendingLagSubPhase;
    private static long pendingLagTime;

    //@Nullable DimensionType dimension
    public static int addLagSpike(TickPhase phase, Enum<?> subPhase, long millis) {
        //pendingLagDimension = dimension;
        pendingLagPhase = phase;
        pendingLagSubPhase = subPhase;
        pendingLagTime = millis;
        return 0;
    }

    /**
     * Safe to call without carpet rule as {@link #pendingLagPhase} is always {@code null} in vanilla
     */
    //@Nullable World world,
    public static void processLagSpikes(TickPhase phase, Enum<?> subPhase) {
        if (phase == pendingLagPhase && subPhase == pendingLagSubPhase) {
            try {
                Thread.sleep(pendingLagTime);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            pendingLagPhase = null;
            pendingLagSubPhase = null;
            //pendingLagDimension = null;
        }
    }

    public enum TickPhase {
        PLAYER(PrePostSubPhase.class, PrePostSubPhase.PRE),
        MOB_SPAWNING(PrePostSubPhase.class, PrePostSubPhase.PRE),
        CHUNK_UNLOADING(PrePostSubPhase.class, PrePostSubPhase.PRE),
        TILE_TICK(PrePostSubPhase.class, PrePostSubPhase.PRE),
        RANDOM_TICK(PrePostSubPhase.class, PrePostSubPhase.PRE),
        BLOCK_EVENT(PrePostSubPhase.class, PrePostSubPhase.PRE),
        ENTITY(PrePostSubPhase.class, PrePostSubPhase.PRE),
        OTHER_ENTITY(PrePostSubPhase.class, PrePostSubPhase.PRE),
        TILE_ENTITY(PrePostSubPhase.class, PrePostSubPhase.PRE),
        AUTOSAVE(PrePostSubPhase.class, PrePostSubPhase.PRE),

        TICK(PrePostSubPhase.class, PrePostSubPhase.PRE);

        private final Class<? extends Enum<?>> subPhaseClass;
        private final Enum<?> defaultSubPhase;
        <T extends Enum<T>> TickPhase(Class<T> subPhaseClass, T defaultSubPhase) {
            this.subPhaseClass = subPhaseClass;
            this.defaultSubPhase = defaultSubPhase;
        }

        public boolean isDimensionApplicable() {
            return this != PLAYER && this != AUTOSAVE && this != TICK;
        }

        public <T extends Enum<?>> Class<T> getSubPhaseClass() {
            return (Class<T>) subPhaseClass;
        }

        public Enum<?> getDefaultSubPhase() {
            return defaultSubPhase;
        }
    }

    public enum PrePostSubPhase {
        PRE, POST
    }

    /*public enum EntitySubPhase {
        PRE, POST_WEATHER, POST_NORMAL, POST_PLAYERS
    }*/
}