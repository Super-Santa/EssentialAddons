package me.supersanta.essential_addons.mixins.feature.public_commands;

import me.supersanta.essential_addons.EssentialSettings;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.server.commands.KickCommand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import java.util.function.Predicate;

@Mixin(KickCommand.class)
public class KickCommandMixin {
    @ModifyArg(
        method = "register",
        at = @At(
            value = "INVOKE",
            target = "Lcom/mojang/brigadier/builder/LiteralArgumentBuilder;requires(Ljava/util/function/Predicate;)Lcom/mojang/brigadier/builder/ArgumentBuilder;",
            remap = false
        )
    )
    private static Predicate<CommandSourceStack> getRequirements(Predicate<CommandSourceStack> predicate) {
        return predicate.or(s -> EssentialSettings.commandPublicKick);
    }
}
