package essentialaddons.commands;

import carpet.settings.SettingsManager;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import essentialaddons.EssentialAddonsSettings;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.entity.TntEntity;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.LiteralText;
import net.minecraft.util.Util;

import java.util.Collection;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public class CommandDefuse {

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(literal("defuse").requires((player) -> SettingsManager.canUseCommand(player, EssentialAddonsSettings.commandDefuse))
                .then(argument("range", IntegerArgumentType.integer(1))
                        .executes(context -> defuse(context, getTntEntities(context.getSource(), context.getArgument("range", Integer.class))))));
    }

    private static Collection<TntEntity> getTntEntities(ServerCommandSource commandSource, int range) throws CommandSyntaxException {
        return (Collection<TntEntity>) EntityArgumentType.entities().parse(new StringReader("@e[type=tnt" + (range == -1 ? "" : ",distance=.." + range) + "]")).getEntities(commandSource);
    }

    private static int defuse(CommandContext<ServerCommandSource> context, Collection<TntEntity> tntEntities) throws CommandSyntaxException {
        for (TntEntity tntEntity : tntEntities) {
            tntEntity.kill();
            // This is commented out because Process told me to
            //tntEntity.getEntityWorld().spawnEntity(new ItemEntity(tntEntity.getEntityWorld(), tntEntity.getX(), tntEntity.getY(), tntEntity.getZ(), new ItemStack(Registry.ITEM.get(new Identifier("minecraft:tnt")), 1)));
        }
        String numTNT = String.valueOf(tntEntities.size());
        context.getSource().getPlayer().sendSystemMessage(new LiteralText("§a"+ numTNT + " §6tnt entities have been defused"), Util.NIL_UUID);
        return tntEntities.size();
    }
}
