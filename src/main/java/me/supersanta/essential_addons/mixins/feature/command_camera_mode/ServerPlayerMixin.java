package me.supersanta.essential_addons.mixins.feature.command_camera_mode;

import me.supersanta.essential_addons.EssentialSettings;
import me.supersanta.essential_addons.utils.EssentialUtilsKt;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.GameType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ServerPlayer.class)
public abstract class ServerPlayerMixin {
    @Shadow public abstract CommandSourceStack createCommandSourceStack();

    @Inject(
        method = "calculateGameModeForNewPlayer",
        at = @At("HEAD"),
        cancellable = true
    )
    private void onCalculateNewGamemode(GameType gameType, CallbackInfoReturnable<GameType> cir) {
        ServerPlayer self = (ServerPlayer) (Object) this;
        if (self.isSpectator()) {
            CommandSourceStack source = this.createCommandSourceStack();
            if (EssentialUtilsKt.has(source, EssentialSettings.commandCameraMode, "command.cs")) {
                cir.setReturnValue(GameType.SPECTATOR);
            }
        }
    }
}
