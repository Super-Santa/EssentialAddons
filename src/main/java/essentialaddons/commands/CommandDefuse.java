package essentialaddons.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import essentialaddons.EssentialSettings;
import essentialaddons.EssentialUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.TntEntity;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.Box;

import java.util.Collection;

import static essentialaddons.EssentialUtils.enabled;
import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public class CommandDefuse {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(literal("defuse").requires(enabled(() -> EssentialSettings.commandDefuse, "essentialaddons.command.defuse"))
            .then(argument("range", IntegerArgumentType.integer(1))
                .executes(CommandDefuse::defuse)
            ));
    }

    private static Collection<TntEntity> getTntEntities(ServerCommandSource commandSource, int range) throws CommandSyntaxException {
        ServerPlayerEntity player = commandSource.getPlayerOrThrow();
        double x = player.getX(), y = player.getY(), z = player.getZ();
        Box nearPlayer = new Box(x - range,y - range,z - range,x + range,y + range, z + range);
        return player.getServerWorld().getEntitiesByClass(TntEntity.class, nearPlayer, tnt -> true);
    }

    private static int defuse(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        Collection<TntEntity> tntEntities = getTntEntities(context.getSource(), context.getArgument("range", Integer.class));
        tntEntities.forEach(Entity::kill);
        EssentialUtils.sendRawFeedback(context.getSource(),true, "§a"+ tntEntities.size() + " §6TNT entities have been defused");
        return 1;
    }
}
