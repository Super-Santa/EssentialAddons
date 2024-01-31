package essentialaddons.commands;

import com.mojang.brigadier.CommandDispatcher;
import essentialaddons.EssentialSettings;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

import java.util.Collection;
import java.util.LinkedList;

import static essentialaddons.EssentialUtils.enabled;
import static net.minecraft.server.command.CommandManager.literal;

public class CommandMods {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(literal("mods").requires(enabled(() -> EssentialSettings.commandMods, "essentialaddons.command.mods"))
            .executes(context -> {
                ServerPlayerEntity playerEntity = context.getSource().getPlayerOrThrow();
                Collection<ModContainer> mods = FabricLoader.getInstance().getAllMods();
                Collection<ModContainer> usefulMods = new LinkedList<>();
                for (ModContainer mod : mods) {
                    if (!mod.getMetadata().getType().equals("builtin") && !mod.getMetadata().getId().equalsIgnoreCase("fabricloader") && !mod.getMetadata().containsCustomValue("fabric-api:module-lifecycle")) {
                        usefulMods.add(mod);
                    }
                }
                String[] modNames = new String[usefulMods.size()];
                int i = 0;
                for (ModContainer mod : usefulMods) {
                    String modName = mod.getMetadata().getName();
                    modNames[i] = modName;
                    i++;
                }
                Text message = Text.literal(modNames.length == 0 ? "There are no mods installed" : "§6Installed Mods: §a" + String.join(", ", modNames));
                playerEntity.sendMessage(message, false);
                return 0;
            })
        );
    }
}
