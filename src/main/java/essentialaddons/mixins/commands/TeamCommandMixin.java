package essentialaddons.mixins.commands;

import com.mojang.brigadier.CommandDispatcher;
import essentialaddons.EssentialSettings;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.command.TeamCommand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TeamCommand.class)
public class TeamCommandMixin {
	@Inject(method = "register", at = @At("HEAD"))
	private static void register(CommandDispatcher<ServerCommandSource> dispatcher, CallbackInfo info) {
		dispatcher.register(CommandManager.literal("team").requires((serverCommandSource) -> EssentialSettings.commandPublicTeam || serverCommandSource.hasPermissionLevel(2)));
	}
}
