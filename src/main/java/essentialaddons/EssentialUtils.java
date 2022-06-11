package essentialaddons;

import carpet.CarpetServer;
import carpet.helpers.InventoryHelper;
import net.fabricmc.api.EnvType;
import essentialaddons.utils.Subscription;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShulkerBoxBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.text.LiteralText;
import net.minecraft.util.WorldSavePath;
import net.minecraft.text.MutableText;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.nio.file.Path;

import static essentialaddons.EssentialAddons.server;
import static net.minecraft.block.Block.dropStack;
import static net.minecraft.block.Block.getDroppedStacks;

public class EssentialUtils {
    public static void sendToActionBar(ServerPlayerEntity playerEntity, String message) {
        playerEntity.sendMessage(EssentialUtils.literal(message), true);
    }

    // For easier porting to 1.19
    public static MutableText literal(String text) {
        return new LiteralText(text);
    }

    public static boolean isItemShulkerBox(Item item) {
        return item instanceof BlockItem blockItem && blockItem.getBlock() instanceof ShulkerBoxBlock;
    }

    public static void placeItemInInventory(BlockState state, World world, BlockPos pos, BlockEntity blockEntity, ServerPlayerEntity player, ItemStack stack){
        if (world instanceof ServerWorld serverWorld) {
            getDroppedStacks(state, serverWorld, pos, blockEntity, player, stack).forEach((itemStack) -> {
                if (!placeItemInInventory(player, itemStack)) {
                    dropStack(serverWorld, pos, itemStack);
                }
            });
            state.onStacksDropped(serverWorld, pos, stack);
        }
    }

    public static boolean placeItemInInventory(ServerPlayerEntity player, ItemStack itemStack) {
        Item item = itemStack.getItem();
        int itemAmount = itemStack.getCount();
        if (EssentialSettings.stackableShulkersInPlayerInventories && !InventoryHelper.shulkerBoxHasItems(itemStack) && isItemShulkerBox(itemStack.getItem())) {
            itemStack.removeSubNbt("BlockEntityTag");
            item = itemStack.getItem();
        }
        if (player.getInventory().insertStack(itemStack)) {
            player.getWorld().playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.PLAYERS, 0.2f, (CarpetServer.rand.nextFloat() - CarpetServer.rand.nextFloat()) * 1.4F + 2.0F);
            player.increaseStat(Stats.PICKED_UP.getOrCreateStat(item), itemAmount);
            return true;
        }
        return false;
    }

    public static boolean hasCareful(Entity entity, Subscription subscription) {
        return entity instanceof ServerPlayerEntity player && hasCareful(player, subscription);
    }

    public static boolean hasCareful(ServerPlayerEntity player, Subscription subscription) {
        return subscription.getRequirement().get() && (player.isSneaking() || Subscription.ALWAYS_CAREFUL.hasPlayer(player)) && subscription.hasPlayer(player);
    }

    public static boolean tryCareful(Entity entity, Subscription subscription, ItemStack stack) {
        if (hasCareful(entity, subscription)) {
            ServerPlayerEntity player = (ServerPlayerEntity) entity;
            return placeItemInInventory(player, stack);
        }
        return false;
    }

    public static Path getSavePath() {
        return server.getSavePath(WorldSavePath.ROOT);
    }

    public static Path getConfigPath() {
        FabricLoader fabricLoader = FabricLoader.getInstance();
        return fabricLoader.getEnvironmentType() == EnvType.SERVER ? fabricLoader.getConfigDir() : getSavePath();
    }

    public static void throwAsRuntime(ThrowableRunnable runnable) {
        try {
            runnable.run();
        }
        catch (Throwable throwable) {
            throw new RuntimeException(throwable);
        }
    }

    @FunctionalInterface
    public interface ThrowableRunnable {
        void run() throws Throwable;
    }
}