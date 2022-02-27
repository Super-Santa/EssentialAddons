package essentialaddons.logging;

import carpet.logging.LoggerRegistry;

public class EssentialAddonsHUDController {

    public static void updateHUD() {
        doHudLogging(EssentialAddonsLoggerRegistry.__autosave, AutosaveHUDLogger.getInstance());
    }

    private static void doHudLogging(boolean condition, AbstractHUDLogger logger) {
        if (condition) {
            LoggerRegistry.getLogger(logger.getName()).log(logger::onHudUpdate);
        }
    }
}