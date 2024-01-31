package essentialaddons.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import essentialaddons.EssentialSettings;
import essentialaddons.EssentialUtils;
import net.minecraft.item.ItemStack;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

import static essentialaddons.EssentialUtils.enabled;
import static net.minecraft.server.command.CommandManager.literal;

public class CommandRepair {
    private static final SimpleCommandExceptionType CANNOT_REPAIR = new SimpleCommandExceptionType(Text.literal("Cannot repair Item"));

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(literal("repair").requires(enabled(() -> EssentialSettings.commandRepair, "essentialaddons.command.repair"))
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
