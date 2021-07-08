package essentialaddons;

import carpet.CarpetServer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.packet.c2s.play.HandSwingC2SPacket;
import net.minecraft.network.packet.s2c.play.TitleS2CPacket;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.LiteralText;

import java.util.*;

public class EssentialAddonsUtils {

    public static void sendToActionBar(ServerPlayerEntity playerEntity, String msg) {
        playerEntity.sendMessage(new LiteralText(msg), true);
    }
    public static boolean shulkerBoxHasItems(ItemStack stack)
    {
        CompoundTag tag = stack.getTag();
        if (tag == null || !tag.contains("BlockEntityTag", 10))
            return false;
        CompoundTag bet = tag.getCompound("BlockEntityTag");
        return bet.contains("Items", 9) && !bet.getList("Items", 10).isEmpty();
    }
}
