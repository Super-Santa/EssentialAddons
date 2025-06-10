package me.supersanta.essential_addons.mixins.feature.public_commands;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import me.supersanta.essential_addons.EssentialSettings;
import net.minecraft.server.commands.OpCommand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(OpCommand.class)
public class OpCommandMixin {
    @ModifyReturnValue(
        method = "method_13470",
        at = @At("RETURN")
    )
    private static boolean getRequirements(boolean original) {
        return original || EssentialSettings.commandPublicOp;
    }
}
