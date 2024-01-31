package essentialaddons.mixins.commands;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import essentialaddons.EssentialAddons;
import essentialaddons.utils.EnumArgumentType;
import net.minecraft.command.argument.serialize.ArgumentSerializer;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.registry.RegistryKeys;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(targets = "net.minecraft.network.packet.s2c.play.CommandTreeS2CPacket$ArgumentNode")
public class ArgumentNodeMixin {
	@Inject(method = "write(Lnet/minecraft/network/PacketByteBuf;Lnet/minecraft/command/argument/serialize/ArgumentSerializer;Lnet/minecraft/command/argument/serialize/ArgumentSerializer$ArgumentTypeProperties;)V", at = @At("HEAD"), cancellable = true)
	private static <A extends ArgumentType<?>, T extends ArgumentSerializer.ArgumentTypeProperties<A>> void write(PacketByteBuf buf, ArgumentSerializer<A, T> serializer, T properties, CallbackInfo ci) {
		if (serializer instanceof EnumArgumentType.Serializer) {
			buf.writeVarInt(EssentialAddons.server.getRegistryManager().get(RegistryKeys.COMMAND_ARGUMENT_TYPE).getRawId(ArgumentTypesAccessor.getClassMap().get(StringArgumentType.class)));
			serializer.writePacket(properties, buf);
			ci.cancel();
		}
	}
}
