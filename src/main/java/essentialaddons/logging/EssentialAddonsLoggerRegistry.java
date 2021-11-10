package essentialaddons.logging;

import carpet.logging.HUDLogger;
import carpet.logging.Logger;
import carpet.logging.LoggerRegistry;
import essentialaddons.logging.loggers.autosave.AutosaveHUDLogger;

import java.lang.reflect.Field;

public class EssentialAddonsLoggerRegistry {

    public static boolean __autosave;

    public static void registerLoggers() {

        LoggerRegistry.registerLogger(AutosaveHUDLogger.NAME, standardHUDLogger(AutosaveHUDLogger.NAME, null, null));

    }

    public static Field getLoggerField(String logName) {
        try {
            return EssentialAddonsLoggerRegistry.class.getField("__" + logName);
        } catch (NoSuchFieldException e)
        {
            throw  new RuntimeException();
        }
    }
    public static Logger standardLogger(String logName, String def, String[] options)
    {
        return new Logger(getLoggerField(logName), logName, def, options);
    }

    public static HUDLogger standardHUDLogger(String logName, String def, String [] options)
    {
        return new HUDLogger(getLoggerField(logName), logName, def, options);
    }


}