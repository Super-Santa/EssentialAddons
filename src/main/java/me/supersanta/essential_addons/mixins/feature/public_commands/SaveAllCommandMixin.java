package me.supersanta.essential_addons.mixins.feature.public_commands;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import me.supersanta.essential_addons.EssentialSettings;
import net.minecraft.server.commands.SaveAllCommand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(SaveAllCommand.class)
public class SaveAllCommandMixin {
    @ModifyReturnValue(
        method = "method_13554",
        at = @At("RETURN")
    )
    private static boolean getRequirements(boolean original) {
        return original || EssentialSettings.commandPublicSaveAll;
    }
}
