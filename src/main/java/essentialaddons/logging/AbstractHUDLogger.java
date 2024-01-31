package essentialaddons.logging;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;

public abstract class AbstractHUDLogger {
    private final String name;

    public AbstractHUDLogger(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public abstract Text[] onHudUpdate(String option, PlayerEntity playerEntity);
}