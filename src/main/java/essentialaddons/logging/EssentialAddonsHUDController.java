package essentialaddons.logging;

import carpet.logging.LoggerRegistry;
import essentialaddons.logging.loggers.AbstractHUDLogger;
import essentialaddons.logging.loggers.autosave.AutosaveHUDLogger;
import net.minecraft.server.MinecraftServer;

public class EssentialAddonsHUDController {

    public static void updateHUD(MinecraftServer server) {

        doHudLogging(EssentialAddonsLoggerRegistry.__autosave, AutosaveHUDLogger.NAME, AutosaveHUDLogger.getInstance());

    }

    private static void doHudLogging(boolean condition, String loggerName, AbstractHUDLogger logger) {

        if (condition) {
            LoggerRegistry.getLogger(loggerName).log(logger::onHudUpdate);
        }

    }

}