package essentialaddons.commands;

import carpet.settings.SettingsManager;
import com.mojang.brigadier.CommandDispatcher;
import essentialaddons.EssentialAddonsSettings;
import essentialaddons.EssentialAddonsUtils;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;

import static net.minecraft.server.command.CommandManager.literal;

public class CommandHat {

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(literal("hat").requires((player) -> SettingsManager.canUseCommand(player, EssentialAddonsSettings.commandHat))
                .executes(context -> {
                    ServerPlayerEntity playerEntity = context.getSource().getPlayer();
                    ItemStack hat = playerEntity.getEquippedStack(EquipmentSlot.HEAD);
                    ItemStack stack = playerEntity.getMainHandStack();
                    ItemStack stackcopy = stack.copy();
                    stackcopy.setCount(1);
                    playerEntity.equip(103, stackcopy);
                    if (!playerEntity.isCreative()) {
                        stack.decrement(1);
                        if (playerEntity.inventory.getEmptySlot() < 0) {
                            ItemEntity itemEntity = new ItemEntity(playerEntity.world, playerEntity.getX(), playerEntity.getY()+1.0, playerEntity.getZ(), hat.copy());
                            itemEntity.setToDefaultPickupDelay();
                            playerEntity.world.spawnEntity(itemEntity);
                        } else {
                            playerEntity.inventory.insertStack(hat.copy());
                        }
                    }
                    playerEntity.playerScreenHandler.sendContentUpdates();
                    return 1;
                }));
    }
}
