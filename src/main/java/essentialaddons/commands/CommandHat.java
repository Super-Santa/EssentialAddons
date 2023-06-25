package essentialaddons.commands;

import com.mojang.brigadier.CommandDispatcher;
import essentialaddons.EssentialSettings;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;

import static essentialaddons.EssentialUtils.enabled;
import static essentialaddons.EssentialUtils.getWorld;
import static net.minecraft.server.command.CommandManager.literal;

public class CommandHat {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(literal("hat").requires(enabled(() -> EssentialSettings.commandHat, "essentialaddons.command.hat"))
            .executes(context -> {
                ServerPlayerEntity player = context.getSource().getPlayerOrThrow();
                ItemStack hat = player.getEquippedStack(EquipmentSlot.HEAD);
                ItemStack stack = player.getMainHandStack();
                ItemStack stackCopy = stack.copy();
                stackCopy.setCount(1);
                player.equipStack(EquipmentSlot.HEAD, stackCopy);
                if (!player.isCreative()) {
                    stack.decrement(1);
                    if (player.getInventory().getEmptySlot() < 0) {
                        ServerWorld world = getWorld(player);
                        ItemEntity itemEntity = new ItemEntity(world, player.getX(), player.getY() + 1.0, player.getZ(), hat.copy());
                        itemEntity.setToDefaultPickupDelay();
                        world.spawnEntity(itemEntity);
                    } else {
                        player.getInventory().insertStack(hat.copy());
                    }
                }
                player.playerScreenHandler.sendContentUpdates();
                return 1;
            })
        );
    }
}
