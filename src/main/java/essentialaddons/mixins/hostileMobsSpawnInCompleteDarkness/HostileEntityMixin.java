package essentialaddons.mixins.hostileMobsSpawnInCompleteDarkness;

import essentialaddons.EssentialAddonsSettings;
import net.minecraft.entity.mob.HostileEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.Random;

@Mixin(HostileEntity.class)
public class HostileEntityMixin {
    @Redirect(method = "isSpawnDark", at = @At(value = "INVOKE", target = "Ljava/util/Random;nextInt(I)I"))
    private static int onNextInt(Random random, int bound) {
        if (EssentialAddonsSettings.hostileMobsSpawnInCompleteDarkness)
            return 0;
        return random.nextInt(8);
    }
}
