package essentialaddons.mixins.commands;

import carpet.commands.PlayerCommand;
import carpet.utils.Messenger;
import com.mojang.brigadier.context.CommandContext;
import essentialaddons.commands.CommandPlayerFake;
import essentialaddons.feature.PlayerFakeEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.command.ServerCommandSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerCommand.class)
public class PlayerCommandMixin {
    @Inject(method = "cantManipulate", at = @At(value = "HEAD"), cancellable = true, remap = false)
    private static void checkFakeFake(CommandContext<ServerCommandSource> context, CallbackInfoReturnable<Boolean> cir) {
        PlayerEntity playerEntity = CommandPlayerFake.getPlayer(context);
        if (playerEntity instanceof PlayerFakeEntity) {
            Messenger.m(context.getSource(), "r Cannot manipulate this player");
            cir.setReturnValue(true);
        }
    }
}
