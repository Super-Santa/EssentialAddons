package essentialaddons.mixins.commands;

import essentialaddons.utils.EnumArgumentType;
import net.minecraft.command.argument.ArgumentTypes;
import net.minecraft.command.argument.serialize.ArgumentSerializer;
import net.minecraft.registry.Registry;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Map;

@Mixin(ArgumentTypes.class)
public abstract class ArgumentTypesMixin {
	@Shadow
	@Final
	private static Map<Class<?>, ArgumentSerializer<?, ?>> CLASS_MAP;

	@Inject(
		method = "register(Lnet/minecraft/registry/Registry;)Lnet/minecraft/command/argument/serialize/ArgumentSerializer;",
		at = @At("HEAD")
	)
	private static void onRegister(
		Registry<ArgumentSerializer<?, ?>> registry,
		CallbackInfoReturnable<ArgumentSerializer<?, ?>> cir
	) {
		CLASS_MAP.put(EnumArgumentType.class, new EnumArgumentType.Serializer());
	}
}
