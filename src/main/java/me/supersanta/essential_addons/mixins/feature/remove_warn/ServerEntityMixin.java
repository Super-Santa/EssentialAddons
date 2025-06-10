package me.supersanta.essential_addons.mixins.feature.remove_warn;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import me.supersanta.essential_addons.EssentialSettings;
import net.minecraft.server.level.ServerEntity;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ServerEntity.class)
public class ServerEntityMixin {
    @WrapWithCondition(
        method = "sendPairingData",
        at = @At(
            value = "INVOKE",
            target = "Lorg/slf4j/Logger;warn(Ljava/lang/String;Ljava/lang/Object;)V",
            remap = false
        )
    )
    private boolean shouldWarnRemovedEntity(Logger instance, String s, Object o) {
        return !EssentialSettings.removeWarnRemovedEntity;
    }
}
