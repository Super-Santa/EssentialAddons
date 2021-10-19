package essentialaddons.mixins.minecartBoosting;

import essentialaddons.EssentialAddonsSettings;
import net.minecraft.entity.vehicle.AbstractMinecartEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(AbstractMinecartEntity.class)
public class AbstractMinecartEntityMixin {
    @ModifyConstant(method = "pushAwayFrom", constant = @Constant(doubleValue = 0.800000011920929D))
    private double minecartBoosting(double v) {
        if (EssentialAddonsSettings.minecartBoosting) { return -Double.MAX_VALUE; }
        return v;
    }
}
