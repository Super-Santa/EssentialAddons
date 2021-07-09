package essentialaddons;

import carpet.CarpetServer;
import carpet.helpers.InventoryHelper;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.text.LiteralText;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import static net.minecraft.block.Block.dropStack;
import static net.minecraft.block.Block.getDroppedStacks;

public class EssentialAddonsUtils {

    public static void sendToActionBar(ServerPlayerEntity playerEntity, String msg) {
        playerEntity.sendMessage(new LiteralText(msg), true);
    }
    //For stackable shulker carefulBreak support
    public static void placeItemInInventory(BlockState state, World world, BlockPos pos, BlockEntity blockEntity, Entity entity, ItemStack stack){
        if (world instanceof ServerWorld) {
            getDroppedStacks(state, (ServerWorld) world, pos, blockEntity, entity, stack).forEach((itemStack) -> {
                Item item = itemStack.getItem();
                int itemAmount = itemStack.getCount();
                if (!InventoryHelper.shulkerBoxHasItems(itemStack) && itemStack.getItem().toString().contains("shulker_box") && EssentialAddonsSettings.stackableShulkersInPlayerInventories) {
                    itemStack.removeSubTag("BlockEntityTag");
                    item = itemStack.getItem();
                    EssentialAddonsSettings.inventoryStacking = true;
                }
                if (((PlayerEntity) entity).inventory.insertStack(itemStack)) {
                    world.playSound(null, entity.getX(), entity.getY(), entity.getZ(), SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.PLAYERS, 0.2f, (CarpetServer.rand.nextFloat() - CarpetServer.rand.nextFloat()) * 1.4F + 2.0F);
                    ((PlayerEntity) entity).increaseStat(Stats.PICKED_UP.getOrCreateStat(item), itemAmount);
                }
                else
                    dropStack(world, pos, itemStack);
                EssentialAddonsSettings.inventoryStacking = false;
            });
            state.onStacksDropped((ServerWorld) world, pos, stack);
        }
    }
}
