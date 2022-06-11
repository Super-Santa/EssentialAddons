package essentialaddons.mixins.commands;

import com.mojang.brigadier.arguments.StringArgumentType;
import essentialaddons.utils.EnumArgumentType;
import net.minecraft.util.registry.Registry;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(targets = "net.minecraft.network.packet.s2c.play.CommandTreeS2CPacket$ArgumentNode")
public class ArgumentNodeMixin {
	@Redirect(method = "write(Lnet/minecraft/network/PacketByteBuf;Lnet/minecraft/command/argument/serialize/ArgumentSerializer;Lnet/minecraft/command/argument/serialize/ArgumentSerializer$ArgumentTypeProperties;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/registry/Registry;getRawId(Ljava/lang/Object;)I"))
	private static <T> int onGetRawId(Registry<T> instance, @Nullable T t) {
		if (t instanceof EnumArgumentType.Serializer) {
			return Registry.COMMAND_ARGUMENT_TYPE.getRawId(ArgumentTypesAccessor.getClassMap().get(StringArgumentType.class));
		}
		return instance.getRawId(t);
	}
}
