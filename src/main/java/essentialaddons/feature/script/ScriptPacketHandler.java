package essentialaddons.feature.script;

import carpet.script.CarpetContext;
import carpet.script.Context;
import carpet.script.Expression;
import carpet.script.exception.InternalExpressionException;
import carpet.script.value.*;
import essentialaddons.EssentialAddons;
import essentialaddons.utils.NetworkHandler;
import io.netty.buffer.Unpooled;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.packet.s2c.play.CustomPayloadS2CPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import static essentialaddons.utils.NetworkUtils.*;

//#if MC >= 11903
import net.minecraft.registry.RegistryKeys;
//#else
//$$import net.minecraft.util.registry.Registry;
//#endif

public class ScriptPacketHandler extends NetworkHandler {
	public static final ScriptPacketHandler INSTANCE = new ScriptPacketHandler();
	public static final Identifier SCRIPT_HANDLER;

	static {
		SCRIPT_HANDLER = new Identifier("essentialclient", "clientscript");
	}

	private ScriptPacketHandler() { }

	public void addScarpetExpression(Expression expression) {
		expression.addContextFunction("send_script_packet", -1, (c, t, v) -> {
			if (v.size() == 0) {
				throw new InternalExpressionException("'send_script_packet' must specify player to send to");
			}

			Value playerValue = v.get(0);
			MinecraftServer server = this.source(c).getServer();
			ServerPlayerEntity player = EntityValue.getPlayerByValue(server, playerValue);
			if (player == null) {
				throw new InternalExpressionException("Cannot target player '%s'".formatted(playerValue.getString()));
			}

			List<Value> values = new ArrayList<>();
			for (int i = 1; i < v.size(); i++) {
				values.add(v.get(i));
			}

			ArgumentParser parser = new ArgumentParser(values);
			player.networkHandler.sendPacket(new CustomPayloadS2CPacket(SCRIPT_HANDLER, parser.parse()));
			return Value.NULL;
		});

		expression.addContextFunction("get_scripters", -1, (c, t, v) -> {
			return new ListValue(this.getValidPlayers().stream().map(EntityValue::of).toList());
		});

		expression.addContextFunction("is_scripter", 1, (c, t, v) -> {
			Value playerValue = v.get(0);
			MinecraftServer server = this.source(c).getServer();
			ServerPlayerEntity player = EntityValue.getPlayerByValue(server, playerValue);
			if (player == null) {
				throw new InternalExpressionException("Cannot target player '%s'".formatted(playerValue.getString()));
			}
			return BooleanValue.of(this.getValidPlayers().contains(player));
		});
	}

	@Override
	public Identifier getNetworkChannel() {
		return SCRIPT_HANDLER;
	}

	@Override
	public int getVersion() {
		return 1_0_0;
	}

	@Override
	protected void processData(PacketByteBuf packetByteBuf, ServerPlayerEntity player) {
		PacketParser parser = new PacketParser(packetByteBuf);
		PacketEvent.EVENT.onScriptPacket(player, List.of(EntityValue.of(player), parser.parseToValues()));
	}

	private ServerCommandSource source(Context context) {
		//#if MC >= 11904
		return ((CarpetContext) context).source();
		//#else
		//$$return ((CarpetContext) context).s;
		//#endif
	}

	private record PacketParser(PacketByteBuf buf) {
		private ListValue parseToValues() {
			List<Value> values = new ArrayList<>();

			while (this.buf.readableBytes() > 0) {
				values.add(ValueConversions.guess(null, this.readNext()));
			}

			return new ListValue(values);
		}

		private Object readNext() {
			return switch (this.buf.readByte()) {
				case BOOLEAN -> this.buf.readBoolean();
				case BYTE -> this.buf.readByte();
				case SHORT -> this.buf.readShort();
				case INT -> this.buf.readInt();
				case LONG -> this.buf.readLong();
				case FLOAT -> this.buf.readFloat();
				case DOUBLE -> this.buf.readDouble();
				case BYTE_ARRAY -> this.buf.readByteArray();
				case INT_ARRAY -> this.buf.readIntArray();
				case LONG_ARRAY -> this.buf.readLongArray();
				case STRING -> this.buf.readString();
				case TEXT -> this.buf.readText();
				case UUID -> this.buf.readUuid();
				case IDENTIFIER -> this.buf.readIdentifier();
				case ITEM_STACK -> this.buf.readItemStack();
				case NBT -> this.buf.readNbt();
				case POS -> new Vec3d(this.buf.readDouble(), this.buf.readDouble(), this.buf.readDouble());
				default -> null;
			};
		}
	}

	private static class ArgumentParser {
		private final List<Value> arguments;
		private final PacketByteBuf buf;

		private ArgumentParser(List<Value> arguments) {
			this.arguments = arguments;
			this.buf = new PacketByteBuf(Unpooled.buffer());
			this.buf.writeVarInt(16);
		}

		private PacketByteBuf parse() {
			for (Value value : this.arguments) {
				if (value instanceof BooleanValue booleanValue) {
					this.buf.writeByte(BOOLEAN);
					this.buf.writeBoolean(booleanValue.getBoolean());
					continue;
				}
				if (value instanceof NumericValue number) {
					if (number.isInteger()) {
						this.buf.writeByte(LONG);
						this.buf.writeLong(number.getLong());
						continue;
					}
					this.buf.writeByte(DOUBLE);
					this.buf.writeDouble(number.getDouble());
					continue;
				}
				if (value instanceof FormattedTextValue text) {
					this.buf.writeByte(TEXT);
					this.buf.writeText(text.getText());
					continue;
				}
				if (value instanceof StringValue string) {
					this.buf.writeByte(STRING);
					this.buf.writeString(string.getString());
					continue;
				}
				if (value instanceof NBTSerializableValue nbt) {
					this.buf.writeByte(NBT);
					this.buf.writeNbt(nbt.getCompoundTag());
					continue;
				}
				if (value instanceof ListValue list) {
					this.parseList(list);
				}
			}
			return this.buf;
		}

		private void parseList(ListValue listValue) {
			List<Value> list = listValue.getItems();
			int size = list.size();
			if (size == 0) {
				this.buf.writeLongArray(new long[0]);
				return;
			}

			if (size == 3) {
				if (list.get(0) instanceof StringValue str && list.get(1) instanceof NumericValue num) {
					//#if MC >= 11903
					Optional<Item> optional = EssentialAddons.server.getRegistryManager().get(RegistryKeys.ITEM).getOrEmpty(new Identifier(str.getString()));
					//#else
					//$$Optional<Item> optional = Registry.ITEM.getOrEmpty(new Identifier(str.getString()));
					//#endif
					if (optional.isPresent()) {
						this.buf.writeByte(ITEM_STACK);
						ItemStack stack = optional.get().getDefaultStack();
						if (list.get(2) instanceof NBTSerializableValue nbtValue) {
							stack.setNbt(nbtValue.getCompoundTag());
						}
						stack.setCount(num.getInt());
						this.buf.writeItemStack(stack);
						return;
					}
					throw new InternalExpressionException("Could not get ItemStack '%s'".formatted(str.getString()));
				}
				if (list.get(0) instanceof NumericValue x && list.get(1) instanceof NumericValue y && list.get(2) instanceof NumericValue z) {
					this.buf.writeByte(POS);
					this.buf.writeDouble(x.getDouble());
					this.buf.writeDouble(y.getDouble());
					this.buf.writeDouble(z.getDouble());
					return;
				}
			}

			int mod = 0;
			if (list.get(0) instanceof StringValue string) {
				mod++;
				switch (string.getString().toLowerCase(Locale.ROOT)) {
					case "b" -> {
						byte[] bytes = new byte[list.size() - 1];
						for ( ; mod < list.size(); mod++) {
							Value value = list.get(mod);
							if (!(value instanceof NumericValue number)) {
								throw new InternalExpressionException("Expected numbers in packet list, got: %s".formatted(value.getString()));
							}
							bytes[mod - 1] = (byte) number.getInt();
						}
						this.buf.writeByte(BYTE_ARRAY);
						this.buf.writeByteArray(bytes);
						return;
					}
					case "i" -> {
						int[] ints = new int[list.size() - 1];
						for ( ; mod < list.size(); mod++) {
							Value value = list.get(mod);
							if (!(value instanceof NumericValue number)) {
								throw new InternalExpressionException("Expected numbers in packet list, got: %s".formatted(value.getString()));
							}
							ints[mod - 1] = number.getInt();
						}
						this.buf.writeByte(INT_ARRAY);
						this.buf.writeIntArray(ints);
						return;
					}
				}
			}

			long[] longs = new long[list.size() - mod];
			for (int i = 0; i < list.size() - mod; i++) {
				Value value = list.get(i);
				if (!(value instanceof NumericValue number)) {
					throw new InternalExpressionException("Expected numbers in packet list, got: %s".formatted(value.getString()));
				}
				longs[i - mod] = number.getLong();
			}
			this.buf.writeByte(LONG_ARRAY);
			this.buf.writeLongArray(longs);
		}
	}
}
