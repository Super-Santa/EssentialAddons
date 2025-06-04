package me.supersanta.essential_addons.mixins.feature.public_commands;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import me.supersanta.essential_addons.EssentialSettings;
import net.minecraft.server.commands.KickCommand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(KickCommand.class)
public class KickCommandMixin {
    @ModifyReturnValue(
        method = "method_13413",
        at = @At("RETURN")
    )
    private static boolean getRequirements(boolean original) {
        return original || EssentialSettings.commandPublicKick;
    }
}
