package essentialaddons.mixins.commands;

import essentialaddons.utils.EnumArgumentType;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.SharedConstants;
import net.minecraft.command.argument.ArgumentTypes;
import net.minecraft.command.argument.serialize.ArgumentSerializer;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

//#if MC >= 11903
import net.minecraft.registry.Registry;
//#else
//$$import net.minecraft.util.registry.Registry;
//#endif

//#if MC < 11900
//$$import com.mojang.brigadier.arguments.ArgumentType;
//$$import com.mojang.brigadier.arguments.StringArgumentType;
//$$import net.minecraft.network.PacketByteBuf;
//$$import net.minecraft.util.Identifier;
//$$import org.spongepowered.asm.mixin.Unique;
//$$import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
//#endif

import java.util.Map;

@Mixin(ArgumentTypes.class)
public abstract class ArgumentTypesMixin {
	//#if MC >= 11900
	@Shadow
	@Final
	private static Map<Class<?>, ArgumentSerializer<?, ?>> CLASS_MAP;

	//#if MC >= 11903
	@Redirect(method = "register(Lnet/minecraft/registry/Registry;)Lnet/minecraft/command/argument/serialize/ArgumentSerializer;", at = @At(value = "FIELD", target = "Lnet/minecraft/SharedConstants;isDevelopment:Z"), require = 0)
	//#else
	//$$@Redirect(method = "register(Lnet/minecraft/util/registry/Registry;)Lnet/minecraft/command/argument/serialize/ArgumentSerializer;", at = @At(value = "FIELD", target = "Lnet/minecraft/SharedConstants;isDevelopment:Z"), require = 0)
	//#endif
	private static boolean isDev() {
		return SharedConstants.isDevelopment || FabricLoader.getInstance().isDevelopmentEnvironment();
	}

	//#if MC >= 11903
	@Inject(method = "register(Lnet/minecraft/registry/Registry;)Lnet/minecraft/command/argument/serialize/ArgumentSerializer;", at = @At("HEAD"))
	//#else
	//$$@Inject(method = "register(Lnet/minecraft/util/registry/Registry;)Lnet/minecraft/command/argument/serialize/ArgumentSerializer;", at = @At("HEAD"))
	//#endif
	private static void onRegister(Registry<ArgumentSerializer<?, ?>> registry, CallbackInfoReturnable<ArgumentSerializer<?, ?>> cir) {
		CLASS_MAP.put(EnumArgumentType.class, new EnumArgumentType.Serializer());
	}
	//#else
	//$$@Unique
	//$$private static final Identifier STRING_IDENTIFIER = new Identifier("brigadier:string");
	//$$
	//$$@Inject(method = "toPacket", at = @At("HEAD"), cancellable = true)
	//$$private static <T extends ArgumentType<?>> void onToPacket(PacketByteBuf buf, T type, CallbackInfo ci) {
	//$$	if (type instanceof EnumArgumentType<?>) {
	//$$		buf.writeIdentifier(STRING_IDENTIFIER);
	//$$		buf.writeEnumConstant(StringArgumentType.StringType.SINGLE_WORD);
	//$$		ci.cancel();
	//$$	}
	//$$}
	//#endif
}
