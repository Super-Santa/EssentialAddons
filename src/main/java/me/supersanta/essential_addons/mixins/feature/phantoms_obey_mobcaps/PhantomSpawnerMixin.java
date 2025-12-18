package me.supersanta.essential_addons.mixins.feature.phantoms_obey_mobcaps;

import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import me.supersanta.essential_addons.EssentialSettings;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.NaturalSpawner;
import net.minecraft.world.level.levelgen.PhantomSpawner;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(PhantomSpawner.class)
public class PhantomSpawnerMixin {
    @Definition(id = "get", method = "Lnet/minecraft/world/level/gamerules/GameRules;get(Lnet/minecraft/world/level/gamerules/GameRule;)Ljava/lang/Object;")
    @Definition(id = "SPAWN_PHANTOMS", field = "Lnet/minecraft/world/level/gamerules/GameRules;SPAWN_PHANTOMS:Lnet/minecraft/world/level/gamerules/GameRule;")
    @Definition(id = "Boolean", type = Boolean.class)
    @Expression("(Boolean) ?.get(SPAWN_PHANTOMS)")
    @ModifyExpressionValue(
        method = "tick",
        at = @At("MIXINEXTRAS:EXPRESSION")
    )
    private Boolean shouldSpawnPhantomsGlobal(Boolean original, ServerLevel level) {
        if (!original) {
            return false;
        }

        if (EssentialSettings.phantomsObeyMobcaps) {
            NaturalSpawner.SpawnState state = level.getChunkSource().getLastSpawnState();
            if (state != null) {
                return ((SpawnStateInvoker) state).isBelowGlobalMobcap(MobCategory.MONSTER);
            }
        }
        return true;
    }

    @ModifyExpressionValue(
        method = "tick",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/server/level/ServerPlayer;isSpectator()Z"
        )
    )
    private boolean shouldNotSpawnPhantomsLocal(
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
