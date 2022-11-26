package essentialaddons.logging;

import carpet.CarpetServer;
import carpet.utils.Messenger;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;

//#if MC < 11900
//$$import net.minecraft.text.BaseText;
//#endif

public class AutosaveHUDLogger extends AbstractHUDLogger {
    public static final String NAME = "autosave";

    private static final AutosaveHUDLogger INSTANCE = new AutosaveHUDLogger();

    private AutosaveHUDLogger() {
        super(NAME);
    }

    public static AutosaveHUDLogger getInstance() {
        return INSTANCE;
    }

    @Override
    //#if MC >= 11900
    public Text[] onHudUpdate(String option, PlayerEntity playerEntity) {
        //#else
        //$$public BaseText[] onHudUpdate(String option, PlayerEntity playerEntity) {
        //#endif
        int gameTick = CarpetServer.minecraft_server.getTicks();
        int previous = gameTick % 6000;

        if (gameTick != 0 && previous == 0) {
            previous = 6000;
        }
        int next = 6000 - previous;

        String color =  next <= 100 ? "§d" : next <= 500 ? "§c" : next <= 1000 ? "§e" : "§2";

        //#if MC >= 11900
        return new Text[] {
            //#else
            //$$return new BaseText[] {
            //#endif
            Messenger.c(String.format("g Prev:%s %d §rNext:%s %d",color, previous, color, next))
        };
    }
}