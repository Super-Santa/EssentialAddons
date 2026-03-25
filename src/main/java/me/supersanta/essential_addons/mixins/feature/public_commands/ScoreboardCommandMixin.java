package me.supersanta.essential_addons.mixins.feature.public_commands;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import me.supersanta.essential_addons.EssentialSettings;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.server.commands.ScoreboardCommand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import java.util.function.Predicate;

@Mixin(ScoreboardCommand.class)
public class ScoreboardCommandMixin {
    @ModifyArg(
        method = "register",
        at = @At(
            value = "INVOKE",
            target = "Lcom/mojang/brigadier/builder/LiteralArgumentBuilder;requires(Ljava/util/function/Predicate;)Lcom/mojang/brigadier/builder/ArgumentBuilder;",
            remap = false
        )
    )
    private static Predicate<CommandSourceStack> getRequirements(Predicate<CommandSourceStack> requirement) {
        return requirement.or(_ -> EssentialSettings.commandPublicScoreboard);
    }

    @ModifyExpressionValue(
        method = "register",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/commands/Commands;literal(Ljava/lang/String;)Lcom/mojang/brigadier/builder/LiteralArgumentBuilder;"
        )
    )
    private static LiteralArgumentBuilder<CommandSourceStack> onLiteralNode(
        LiteralArgumentBuilder<CommandSourceStack> original
    ) {
        switch (original.getLiteral()) {
            case "players", "teams", "remove", "modify" -> {
                original.requires(Commands.hasPermission(Commands.LEVEL_GAMEMASTERS));
            }
        }
        return original;
    }
}
