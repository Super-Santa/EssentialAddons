package essentialaddons.logging;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.BaseText;
public abstract class AbstractHUDLogger {
    private final String NAME;

    public AbstractHUDLogger(String name) {
        this.NAME = name;
    }

    public String getName() {
        return this.NAME;
    }

    public abstract BaseText[] onHudUpdate(String option, PlayerEntity playerEntity);
}