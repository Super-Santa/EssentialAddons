package essentialaddons.mixins;

import carpet.CarpetServer;
import essentialaddons.EssentialAddonsSettings;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.BooleanSupplier;

@Mixin(ServerWorld.class)
public class ServerWorldMixin {
    @Inject(method = "tick", at = @At("HEAD"), cancellable = true)
    public void tick(BooleanSupplier shouldKeepTicking, CallbackInfo ci) {
        if (EssentialAddonsSettings.removeXpEntitiesIfMsptOver > 0) {
            double mspt = MathHelper.average(CarpetServer.minecraft_server.lastTickLengths) * 1.0E-6D;
            if (mspt > EssentialAddonsSettings.removeXpEntitiesIfMsptOver) {
                ServerCommandSource source = CarpetServer.minecraft_server.getCommandSource();
                CarpetServer.minecraft_server.getCommandManager().execute(source, "kill @e[type=experience_orb]");
            }
        }
    }
}