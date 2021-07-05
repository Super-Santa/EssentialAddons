package essentialaddons.mixins;

import com.mojang.brigadier.CommandDispatcher;
import essentialaddons.EssentialAddonsSettings;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.KickCommand;
import net.minecraft.server.command.ServerCommandSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import carpet.settings.SettingsManager;

@Mixin(KickCommand.class)
public class KickCommandMixin {
    @Inject(method = "register", at = @At("HEAD"))
    private static void register(CommandDispatcher<ServerCommandSource> dispatcher, CallbackInfo info) {
        dispatcher.register(CommandManager.literal("kick").requires((serverCommandSource) -> SettingsManager.canUseCommand(serverCommandSource, EssentialAddonsSettings.commandPublicKick) || serverCommandSource.hasPermissionLevel(3)));
    }
}
