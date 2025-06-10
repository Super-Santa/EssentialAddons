package me.supersanta.essential_addons.mixins.feature.public_commands;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import me.supersanta.essential_addons.EssentialSettings;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.server.commands.ScoreboardCommand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ScoreboardCommand.class)
public class ScoreboardCommandMixin {
    @ModifyReturnValue(
        method = "method_13585",
        at = @At("RETURN")
    )
    private static boolean getRequirements(boolean original) {
        return original || EssentialSettings.commandPublicScoreboard;
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
                original.requires(source -> source.hasPermission(2));
            }
        }
        return original;
    }
}
