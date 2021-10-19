package essentialaddons.logging.loggers;

import essentialaddons.translations.TranslatableBase;

public abstract class AbstractLogger extends TranslatableBase {
    protected final static String MULTI_OPTION_SEG_REG = "[,.]";

    public AbstractLogger(String name) {
        super("logger", name);
    }
}