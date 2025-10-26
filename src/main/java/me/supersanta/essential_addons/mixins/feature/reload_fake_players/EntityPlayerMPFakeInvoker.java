package me.supersanta.essential_addons.mixins.feature.reload_fake_players;

import carpet.patches.EntityPlayerMPFake;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(value = EntityPlayerMPFake.class, remap = false)
public interface EntityPlayerMPFakeInvoker {
    @Invoker("loadPlayerData")
    static void invokeLoadPlayerData(EntityPlayerMPFake player) {
        throw new AssertionError();
    }
}
