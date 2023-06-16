package essentialaddons.commands;

import com.mojang.brigadier.CommandDispatcher;
import essentialaddons.EssentialSettings;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;

import static essentialaddons.EssentialUtils.enabled;
import static net.minecraft.server.command.CommandManager.literal;

public class CommandHat {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(literal("hat").requires(enabled(() -> EssentialSettings.commandHat, "essentialaddons.command.hat"))
            .executes(context -> {
                ServerPlayerEntity playerEntity = context.getSource().getPlayerOrThrow();
                ItemStack hat = playerEntity.getEquippedStack(EquipmentSlot.HEAD);
                ItemStack stack = playerEntity.getMainHandStack();
                ItemStack stackCopy = stack.copy();
                stackCopy.setCount(1);
                playerEntity.equipStack(EquipmentSlot.HEAD, stackCopy);
                if (!playerEntity.isCreative()) {
                    stack.decrement(1);
                    if (playerEntity.getInventory().getEmptySlot() < 0) {
                        ItemEntity itemEntity = new ItemEntity(playerEntity.getWorld(), playerEntity.getX(), playerEntity.getY()+1.0, playerEntity.getZ(), hat.copy());
                        itemEntity.setToDefaultPickupDelay();
                        playerEntity.getWorld().spawnEntity(itemEntity);
                    }
                    else {
                        playerEntity.getInventory().insertStack(hat.copy());
                    }
                }
                playerEntity.playerScreenHandler.sendContentUpdates();
                return 1;
            })
        );
    }
}
