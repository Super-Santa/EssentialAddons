package essentialaddons;

import carpet.helpers.InventoryHelper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import essentialaddons.utils.Subscription;
import me.lucko.fabric.api.permissions.v0.Permissions;
import net.fabricmc.api.EnvType;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShulkerBoxBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.WorldSavePath;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.Map;
import java.util.Random;
import java.util.function.Predicate;
import java.util.function.Supplier;

import static essentialaddons.EssentialAddons.server;
import static net.minecraft.block.Block.dropStack;
import static net.minecraft.block.Block.getDroppedStacks;

//#if MC >= 11900
import static carpet.utils.CommandHelper.canUseCommand;
//#else
//$$import net.minecraft.text.*;
//$$import static carpet.settings.SettingsManager.canUseCommand;
//#endif

public class EssentialUtils {
    public static final Random RANDOM = new Random();

    public static void sendToActionBar(ServerPlayerEntity playerEntity, String message) {
        playerEntity.sendMessage(EssentialUtils.literal(message), true);
    }

    // For easier porting to 1.19
    public static MutableText literal(String text) {
        //#if MC >= 11900
        return Text.literal(text);
        //#else
        //$$return new LiteralText(text);
        //#endif
    }

    public static Text translatable(String key, Object... arguments) {
        //#if MC >= 11900
        return Text.translatable(key, arguments);
        //#else
        //$$return new TranslatableText(key, arguments);
        //#endif
    }

    public static void sendFeedback(ServerCommandSource source, boolean log, Supplier<Text> generator) {
        //#if MC >= 12000
        source.sendFeedback(generator, log);
        //#else
        //$$source.sendFeedback(generator.get(), log);
        //#endif
    }

    public static void sendRawFeedback(ServerCommandSource source, boolean log, String string) {
        sendFeedback(source, log, () -> literal(string));
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
            //#if MC >= 11900
            state.onStacksDropped(serverWorld, pos, stack, true);
            //#else
            //$$state.onStacksDropped(serverWorld, pos, stack);
            //#endif
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
            player.getWorld().playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.PLAYERS, 0.2f, (RANDOM.nextFloat() - RANDOM.nextFloat()) * 1.4F + 2.0F);
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

    public static boolean hasPermission(ServerCommandSource source, Supplier<Object> field, String permission) {
        return canUseCommand(source, field.get()) || Permissions.check(source, permission);
    }

    public static Predicate<ServerCommandSource> enabled(Supplier<Object> field, String permission) {
        return source -> hasPermission(source, field, permission);
    }

    public static World getWorld(Entity entity) {
        return entity.getEntityWorld();
    }

    public static ServerWorld getWorld(ServerPlayerEntity player) {
        //#if MC >= 12000
        return player.getServerWorld();
        //#else
        //$$return player.getWorld();
        //#endif
    }

    public static Predicate<ServerCommandSource> op(String permission) {
        return source -> Permissions.check(source, permission, 4);
    }

    public static Path getSavePath() {
        if (server == null) {
            return FabricLoader.getInstance().getConfigDir();
        }
        return server.getSavePath(WorldSavePath.ROOT);
    }

    public static Path getConfigPath() {
        FabricLoader fabricLoader = FabricLoader.getInstance();
        return fabricLoader.getEnvironmentType() == EnvType.SERVER ? fabricLoader.getConfigDir() : getSavePath();
    }

    public static Map<String, String> getTranslations(String lang) {
        InputStream langFile = EssentialUtils.class.getClassLoader().getResourceAsStream("assets/essentialaddons/lang/%s.json".formatted(lang));
        if (langFile == null) {
            return Map.of();
        }
        String jsonData;
        try {
            jsonData = IOUtils.toString(langFile, StandardCharsets.UTF_8);
        } catch (IOException e) {
            return Map.of();
        }
        return new Gson().fromJson(jsonData, new TypeToken<Map<String, String>>() {}.getType());
    }

    public static void throwAsRuntime(ThrowableRunnable runnable) {
        try {
            runnable.run();
        } catch (Throwable throwable) {
            throw new RuntimeException(throwable);
        }
    }

    @FunctionalInterface
    public interface ThrowableRunnable {
        void run() throws Throwable;
    }
}
