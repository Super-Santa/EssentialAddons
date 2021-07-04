package essentialaddons;

import carpet.CarpetExtension;
import carpet.CarpetServer;
import com.mojang.brigadier.CommandDispatcher;
import essentialaddons.commands.*;
import net.fabricmc.api.ModInitializer;
import net.minecraft.server.command.ServerCommandSource;

public class EssentialAddonsServer implements CarpetExtension, ModInitializer {

    @Override
    public String version() { return "essentialaddons"; }

    @Override
    public void onInitialize() { CarpetServer.manageExtension(new EssentialAddonsServer()); }

    @Override
    public void onGameStarted() { CarpetServer.settingsManager.parseSettingsClass(EssentialAddonsSettings.class); }

    @Override
    public void registerCommands(CommandDispatcher<ServerCommandSource> dispatcher) {
        CommandFly.register(dispatcher);
        CommandRepair.register(dispatcher);
        CommandGM.register(dispatcher);
        CommandHeal.register(dispatcher);
        CommandExtinguish.register(dispatcher);
        CommandGod.register(dispatcher);
        CommandDefuse.register(dispatcher);
        CommandMore.register(dispatcher);
        CommandStrength.register(dispatcher);
        CommandNightVision.register(dispatcher);
        CommandDimensions.register(dispatcher);
        CommandWarp.register(dispatcher);
        CommandCameraMode.register(dispatcher);
    }
}
