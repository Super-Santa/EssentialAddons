package essentialaddons.commands;

import carpet.utils.CommandHelper;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import essentialaddons.EssentialSettings;
import essentialaddons.EssentialUtils;
import net.minecraft.item.ItemStack;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;

import static net.minecraft.server.command.CommandManager.literal;

public class CommandRepair {
    private static final SimpleCommandExceptionType CANNOT_REPAIR = new SimpleCommandExceptionType(EssentialUtils.literal("Cannot repair Item"));

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(literal("repair").requires((player) -> CommandHelper.canUseCommand(player, EssentialSettings.commandRepair))
            .executes(context -> {
                ServerPlayerEntity playerEntity = context.getSource().getPlayerOrThrow();
                ItemStack itemStack = playerEntity.getMainHandStack();
                if (itemStack.isDamaged()) {
                    itemStack.setDamage(0);
                    EssentialUtils.sendToActionBar(playerEntity, "§6Item was repaired");
                    return 1;
                }
                throw CANNOT_REPAIR.create();
            })
        );
    }
}
