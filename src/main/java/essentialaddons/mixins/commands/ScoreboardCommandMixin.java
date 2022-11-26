package essentialaddons.mixins.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import essentialaddons.EssentialSettings;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ScoreboardCommand;
import net.minecraft.server.command.ServerCommandSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ScoreboardCommand.class)
public class ScoreboardCommandMixin {
    @Inject(method = "register", at = @At("HEAD"))
    private static void register(CommandDispatcher<ServerCommandSource> dispatcher, CallbackInfo info) {
        dispatcher.register(CommandManager.literal("scoreboard").requires((serverCommandSource) -> EssentialSettings.commandPublicScoreboard || serverCommandSource.hasPermissionLevel(2)));
    }

    @Redirect(method = "register", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/command/CommandManager;literal(Ljava/lang/String;)Lcom/mojang/brigadier/builder/LiteralArgumentBuilder;"))
    private static LiteralArgumentBuilder<ServerCommandSource> requirements(String literal) {
        LiteralArgumentBuilder<ServerCommandSource> builder = CommandManager.literal(literal);
        return switch (literal) {
            case "players", "teams", "remove", "modify" -> builder.requires(serverCommandSource -> serverCommandSource.hasPermissionLevel(2));
            default -> builder;
        };
    }
}
