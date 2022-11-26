package essentialaddons.logging;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;

//#if MC < 11900
//$$import net.minecraft.text.BaseText;
//#endif

public abstract class AbstractHUDLogger {
    private final String NAME;

    public AbstractHUDLogger(String name) {
        this.NAME = name;
    }

    public String getName() {
        return this.NAME;
    }

    //#if MC >= 11900
    public abstract Text[] onHudUpdate(String option, PlayerEntity playerEntity);
    //#else
    //$$public abstract BaseText[] onHudUpdate(String option, PlayerEntity playerEntity);
    //#endif
}