package me.supersanta.essential_addons.feature.careful_break

import me.supersanta.essential_addons.utils.hasCarefulDrop
import net.casual.arcade.utils.MathUtils.component1
import net.casual.arcade.utils.MathUtils.component2
import net.casual.arcade.utils.MathUtils.component3
import net.casual.arcade.utils.player.dropItemStackIntoInventory
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.Container
import net.minecraft.world.Containers
import net.minecraft.world.damagesource.DamageSource
import net.minecraft.world.entity.Entity
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level
import java.util.function.Consumer

object CarefulDrop {
    private val DESTROYING_ENTITY: ThreadLocal<Entity> = ThreadLocal()

    @JvmStatic
    fun carefullyDropContents(
        source: DamageSource,
        level: Level,
        entity: Entity,
        container: Container
    ): Boolean {
        val attacker = source.entity
        if (attacker is ServerPlayer && attacker.hasCarefulDrop()) {
            this.carefullyDropContents(attacker, level, entity, container)
            return true
        }
        return false
    }

    @JvmStatic
    fun carefullyDrop(entity: Entity?, stack: ItemStack, remaining: Consumer<ItemStack>) {
        if (entity is ServerPlayer && entity.hasCarefulDrop()) {
            this.carefullyDrop(entity, stack, remaining)
        } else {
            remaining.accept(stack)
        }
    }

    @JvmStatic
    fun carefullyDrop(player: ServerPlayer, stack: ItemStack, remaining: Consumer<ItemStack>) {
        player.dropItemStackIntoInventory(stack, remaining::accept)
    }

    @JvmStatic
    fun setVehicleDestroyingEntity(entity: Entity?) {
        DESTROYING_ENTITY.set(entity)
    }

    @JvmStatic
    fun getVehicleDestroyingEntity(): Entity? {
        return DESTROYING_ENTITY.get()
    }

    private fun carefullyDropContents(player: ServerPlayer, level: Level, entity: Entity, container: Container) {
        val (x, y, z) = entity.position()
        for (i in 0..< container.containerSize) {
            val stack = container.getItem(i)
            player.dropItemStackIntoInventory(stack) { remaining ->
                Containers.dropItemStack(level, x, y, z, remaining)
            }
        }
    }
}