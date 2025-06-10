package me.supersanta.essential_addons.mixins.feature.command_camera_mode;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.server.level.ServerPlayerGameMode;
import net.minecraft.world.level.GameType;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ServerPlayerGameMode.class)
public class ServerPlayerGameModeMixin {
    @Shadow private GameType gameModeForPlayer;

    @ModifyExpressionValue(
        method = "changeGameModeForPlayer",
        at = @At(
            value = "FIELD",
            target = "Lnet/minecraft/server/level/ServerPlayerGameMode;previousGameModeForPlayer:Lnet/minecraft/world/level/GameType;",
            opcode = Opcodes.GETFIELD
        )
    )
    private GameType setPreviousGameMode(GameType original) {
        // Fixes a bug where mojang assigns the previous game mode
        // as the new previous game mode, instead of the current game mode
        return this.gameModeForPlayer;
    }
}
