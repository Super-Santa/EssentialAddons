package essentialaddons.mixins.commands;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import essentialaddons.utils.EnumArgumentType;
import net.minecraft.command.argument.ArgumentTypes;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ArgumentTypes.class)
public class ArgumentTypesMixin {
	@Unique
	private static final Identifier STRING_IDENTIFIER = new Identifier("brigadier:string");

	@Inject(method = "toPacket", at = @At("HEAD"), cancellable = true)
	private static <T extends ArgumentType<?>> void onToPacket(PacketByteBuf buf, T type, CallbackInfo ci) {
		if (type instanceof EnumArgumentType<?>) {
			buf.writeIdentifier(STRING_IDENTIFIER);
			buf.writeEnumConstant(StringArgumentType.StringType.SINGLE_WORD);
			ci.cancel();
		}
	}
}
