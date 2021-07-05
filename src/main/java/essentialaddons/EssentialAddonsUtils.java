package essentialaddons;

import carpet.CarpetServer;
import net.minecraft.network.packet.c2s.play.HandSwingC2SPacket;
import net.minecraft.network.packet.s2c.play.TitleS2CPacket;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.LiteralText;

import java.util.*;

public class EssentialAddonsUtils {

    public static void sendToActionBar(ServerPlayerEntity playerEntity, String msg) {
        playerEntity.sendMessage(new LiteralText(msg), true);
    }
}
