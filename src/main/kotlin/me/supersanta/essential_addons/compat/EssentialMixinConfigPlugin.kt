package me.supersanta.essential_addons.compat

import com.google.common.collect.HashMultimap
import net.fabricmc.loader.api.FabricLoader
import org.objectweb.asm.tree.ClassNode
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin
import org.spongepowered.asm.mixin.extensibility.IMixinInfo

class EssentialMixinConfigPlugin: IMixinConfigPlugin {
    companion object {
        private const val MIXIN_COMPAT = "me.supersanta.essential_addons.mixin.compat."

        private val incompatible = HashMultimap.create<String, String>()

        init {
            this.incompatible.put("essentialaddons.mixins.combinePotionDuration", "RPGStats")
        }
    }

    override fun shouldApplyMixin(targetClassName: String, mixinClassName: String): Boolean {
        if (mixinClassName.startsWith(MIXIN_COMPAT)) {
            val modId = mixinClassName.removePrefix(MIXIN_COMPAT).substringBefore('.')
            return FabricLoader.getInstance().isModLoaded(modId)
        }
        for (modId in incompatible.get(mixinClassName)) {
            if (FabricLoader.getInstance().isModLoaded(modId)) {
                return false
            }
        }
        return true
    }

    override fun getRefMapperConfig(): String? {
        return null
    }

    override fun getMixins(): List<String>? {
        return null
    }

    override fun onLoad(mixinPackage: String) {

    }

    override fun acceptTargets(myTargets: Set<String>, otherTargets: Set<String>) {

    }

    override fun preApply(
        targetClassName: String,
        targetClass: ClassNode,
        mixinClassName: String,
        mixinInfo: IMixinInfo
    ) {

    }

    override fun postApply(
        targetClassName: String,
        targetClass: ClassNode,
        mixinClassName: String,
        mixinInfo: IMixinInfo
    ) {

    }
}