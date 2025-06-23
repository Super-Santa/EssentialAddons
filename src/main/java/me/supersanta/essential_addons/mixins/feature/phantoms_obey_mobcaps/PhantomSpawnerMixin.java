package me.supersanta.essential_addons.mixins.feature.phantoms_obey_mobcaps;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import me.supersanta.essential_addons.EssentialSettings;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.NaturalSpawner;
import net.minecraft.world.level.levelgen.PhantomSpawner;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(PhantomSpawner.class)
public class PhantomSpawnerMixin {
    @ModifyExpressionValue(
        method = "tick",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/server/level/ServerPlayer;isSpectator()Z"
        )
    )
    private boolean shouldNotSpawnPhantoms(
        boolean original,
        @Local ServerPlayer player
    ) {
        if (original) {
            return true;
        }
        if (EssentialSettings.phantomsObeyMobcaps) {
            NaturalSpawner.SpawnState state = player.level().getChunkSource().getLastSpawnState();
            if (state != null) {
                return !((SpawnStateInvoker) state).isBelowLocalMobcap(MobCategory.MONSTER, player.chunkPosition());
            }
        }
        return false;
    }
}
