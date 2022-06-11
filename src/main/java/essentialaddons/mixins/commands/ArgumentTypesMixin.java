package essentialaddons.mixins.commands;

import com.mojang.brigadier.arguments.ArgumentType;
import essentialaddons.utils.EnumArgumentType;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.SharedConstants;
import net.minecraft.command.argument.ArgumentTypes;
import net.minecraft.command.argument.serialize.ArgumentSerializer;
import net.minecraft.util.registry.Registry;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Map;

@Mixin(ArgumentTypes.class)
public abstract class ArgumentTypesMixin {
	@Shadow
	@Final
	private static Map<Class<?>, ArgumentSerializer<?, ?>> CLASS_MAP;

	@Redirect(method = "register(Lnet/minecraft/util/registry/Registry;)Lnet/minecraft/command/argument/serialize/ArgumentSerializer;", at = @At(value = "FIELD", target = "Lnet/minecraft/SharedConstants;isDevelopment:Z"), require = 0)
	private static boolean isDev() {
		return SharedConstants.isDevelopment || FabricLoader.getInstance().isDevelopmentEnvironment();
	}

	@Inject(method = "register(Lnet/minecraft/util/registry/Registry;)Lnet/minecraft/command/argument/serialize/ArgumentSerializer;", at = @At("HEAD"))
	private static void onRegister(Registry<ArgumentSerializer<?, ?>> registry, CallbackInfoReturnable<ArgumentSerializer<?, ?>> cir) {
		CLASS_MAP.put(EnumArgumentType.class, new EnumArgumentType.Serializer());
	}
}
