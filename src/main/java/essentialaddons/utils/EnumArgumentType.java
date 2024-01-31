package essentialaddons.utils;

import com.google.gson.JsonObject;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import essentialaddons.EssentialUtils;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.command.CommandSource;
import net.minecraft.command.argument.serialize.ArgumentSerializer;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.command.ServerCommandSource;

import java.util.HashMap;
import java.util.concurrent.CompletableFuture;

public class EnumArgumentType<T extends Enum<T>> implements ArgumentType<T> {
	static {
		INVALID_ELEMENT_EXCEPTION = new DynamicCommandExceptionType(object -> EssentialUtils.literal("Enumeration element not found: " + object.toString()));
	}

	private static final DynamicCommandExceptionType INVALID_ELEMENT_EXCEPTION;
	private final HashMap<String, T> values;
	private final Class<T> clazz;

	private EnumArgumentType(Class<T> clazz) {
		this.clazz = clazz;
		Enum<?>[] arrayOfEnum = clazz.getEnumConstants();
		this.values = new HashMap<>(arrayOfEnum.length);
		for (Enum<?> enum_ : arrayOfEnum) {
			this.values.put(enum_.name(), clazz.cast(enum_));
		}
	}

	public static <T extends Enum<T>> EnumArgumentType<T> enumeration(Class<T> clazz) {
		return new EnumArgumentType<>(clazz);
	}

	public static <T extends Enum<T>> T getEnumeration(CommandContext<ServerCommandSource> commandContext, String string, Class<T> clazz) {
		return commandContext.getArgument(string, clazz);
	}

	@Override
	public T parse(StringReader reader) throws CommandSyntaxException {
		String name = reader.readString();
		Enum<?> enum_ = this.values.get(name);
		if (enum_ != null) {
			return this.clazz.cast(enum_);
		}
		throw INVALID_ELEMENT_EXCEPTION.create(name);
	}

	public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
		return CommandSource.suggestMatching(this.values.keySet(), builder);
	}

	public static class Serializer implements ArgumentSerializer<EnumArgumentType<?>, Serializer.Properties<?>> {
		@Override
		public void writePacket(Properties properties, PacketByteBuf buf) {
			buf.writeEnumConstant(StringArgumentType.StringType.SINGLE_WORD);
		}

		// Client only
		@Override
		public Properties<?> fromPacket(PacketByteBuf buf) {
			return null;
		}

		@Override
		public void writeJson(Properties properties, JsonObject json) {
			json.addProperty("type", "word");
		}

		@Override
		public Properties<?> getArgumentTypeProperties(EnumArgumentType<?> argumentType) {
			return new Properties<>(argumentType.clazz);
		}

		public class Properties<T extends Enum<T>> implements ArgumentSerializer.ArgumentTypeProperties<EnumArgumentType<?>> {
			private final Class<T> clazz;

			public Properties(Class<T> clazz) {
				this.clazz = clazz;
			}

			@Override
			public EnumArgumentType<T> createType(CommandRegistryAccess commandRegistryAccess) {
				// We need to implement this for client side functionality
				return new EnumArgumentType<>(this.clazz);
			}

			@Override
			public ArgumentSerializer<EnumArgumentType<?>, ?> getSerializer() {
				return Serializer.this;
			}
		}
	}
}