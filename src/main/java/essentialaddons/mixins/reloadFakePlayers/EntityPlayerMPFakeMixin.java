package essentialaddons.mixins.reloadFakePlayers;

import carpet.patches.EntityPlayerMPFake;
import carpet.patches.FakeClientConnection;
import com.mojang.authlib.GameProfile;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.Dynamic;
import essentialaddons.EssentialAddons;
import essentialaddons.EssentialSettings;
import essentialaddons.utils.ConfigFakePlayerData;
import essentialaddons.utils.ducks.IFakePlayer;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.NetworkSide;
//#if MC >= 11900 && MC < 11903
//$$import net.minecraft.network.encryption.PlayerPublicKey;
//#endif
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableTextContent;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

//#if MC >= 11903
import net.minecraft.registry.RegistryKey;
//#else
//$$import net.minecraft.util.registry.RegistryKey;
//#endif

@Mixin(value = EntityPlayerMPFake.class)
public abstract class EntityPlayerMPFakeMixin extends ServerPlayerEntity implements IFakePlayer {
	//#if MC >= 11900 && MC < 11903
	//$$public EntityPlayerMPFakeMixin(MinecraftServer server, ServerWorld world, GameProfile profile, PlayerPublicKey publicKey) {
	//$$	super(server, world, profile, publicKey);
	//$$}
	//#else
	public EntityPlayerMPFakeMixin(MinecraftServer server, ServerWorld world, GameProfile profile) {
		super(server, world, profile);
	}
	//#endif

	@Inject(method = "<init>", at = @At("TAIL"), remap = false)
	//#if MC >= 11900 && MC < 11903
	//$$private void onCreateFakePlayer(MinecraftServer server, ServerWorld worldIn, GameProfile profile, boolean shadow, PlayerPublicKey profilePublicKey, CallbackInfo ci) {
	//#else
	private void onCreateFakePlayer(MinecraftServer server, ServerWorld worldIn, GameProfile profile, boolean shadow, CallbackInfo ci) {
		//#endif
		ConfigFakePlayerData.INSTANCE.addFakePlayer((EntityPlayerMPFake) (Object) this);
	}

	@Inject(method = "kill(Lnet/minecraft/text/Text;)V", at = @At("HEAD"))
	private void onPlayerKill(Text reason, CallbackInfo ci) {
		ConfigFakePlayerData.INSTANCE.removeFakePlayer((EntityPlayerMPFake) (Object) this);
		//#if MC >= 11900
		if (EssentialSettings.fakePlayerDropInventoryOnKill && !(reason instanceof MutableText text && text.getContent() instanceof TranslatableTextContent content && content.getKey().equals("multiplayer.disconnect.duplicate_login"))) {
			//#else
			//$$if (EssentialSettings.fakePlayerDropInventoryOnKill && !(reason instanceof TranslatableText text && text.getKey().equals("multiplayer.disconnect.duplicate_login"))) {
			//#endif
			this.dropInventory();
		}
	}

	@Override
	public void join(MinecraftServer server) {
		NbtCompound nbtCompound = server.getPlayerManager().loadPlayerData(this);
		RegistryKey<World> registryKey = World.OVERWORLD;
		if (nbtCompound != null) {
			@SuppressWarnings("deprecation")
			DataResult<RegistryKey<World>> data = DimensionType.worldFromDimensionNbt(new Dynamic<>(NbtOps.INSTANCE, nbtCompound.get("Dimension")));
			registryKey = data.resultOrPartial(EssentialAddons.LOGGER::error).orElse(World.OVERWORLD);
		}
		ServerWorld serverWorld = server.getWorld(registryKey);
		this.setWorld(serverWorld);
		server.getPlayerManager().onPlayerConnect(new FakeClientConnection(NetworkSide.SERVERBOUND), this);
		//#if MC >= 11904
		this.setStepHeight(0.6F);
		//#else
		//this.stepHeight = 0.6F;
		//#endif
		this.unsetRemoved();
	}
}
