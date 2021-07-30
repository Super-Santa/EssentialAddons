package essentialaddons.mixins.reloadFakePlayers;

import carpet.commands.PlayerCommand;
import carpet.helpers.EntityPlayerActionPack;
import carpet.patches.EntityPlayerMPFake;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import essentialaddons.EssentialAddonsSettings;
import essentialaddons.helpers.ReloadFakePlayers;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerCommand.class)
public abstract class PlayerCommandMixin {

    @Inject(method = "action", at = @At("HEAD"), remap = false)
    private static void action(CommandContext<ServerCommandSource> context, EntityPlayerActionPack.ActionType type, EntityPlayerActionPack.Action action, CallbackInfoReturnable<Integer> cir) {
        if (EssentialAddonsSettings.reloadFakePlayers) {
            MinecraftServer server = context.getSource().getServer();
            String username = StringArgumentType.getString(context, "player");
            ServerPlayerEntity playerEntity = server.getPlayerManager().getPlayer(username);
            if (playerEntity instanceof EntityPlayerMPFake)
                ReloadFakePlayers.passAction(username, type, action);
        }
    }
    @Redirect(method = "stop", at = @At(value = "INVOKE", target = "Lcarpet/helpers/EntityPlayerActionPack;stopAll()Lcarpet/helpers/EntityPlayerActionPack;"), remap = false)
    private static EntityPlayerActionPack onStopAll(EntityPlayerActionPack entityPlayerActionPack, CommandContext<ServerCommandSource> context) {
        if (EssentialAddonsSettings.reloadFakePlayers) {
            MinecraftServer server = context.getSource().getServer();
            String username = StringArgumentType.getString(context, "player");
            ServerPlayerEntity playerEntity = server.getPlayerManager().getPlayer(username);
            if (playerEntity instanceof EntityPlayerMPFake)
                ReloadFakePlayers.passStop(username);
        }
        return entityPlayerActionPack.stopAll();
    }
}
