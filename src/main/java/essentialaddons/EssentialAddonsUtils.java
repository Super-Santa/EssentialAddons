package essentialaddons;

import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.LiteralText;

public class EssentialAddonsUtils {

    public static void sendToActionBar(ServerPlayerEntity playerEntity, String msg) {
        playerEntity.sendMessage(new LiteralText(msg), true);
    }
}
