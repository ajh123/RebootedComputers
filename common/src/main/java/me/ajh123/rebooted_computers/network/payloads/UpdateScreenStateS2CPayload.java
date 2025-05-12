package me.ajh123.rebooted_computers.network.payloads;

import dev.architectury.networking.NetworkManager;
import dev.architectury.utils.Env;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import me.ajh123.rebooted_computers.RebootedComputers;
import me.ajh123.rebooted_computers.gui.screens.SynchronisedScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public class UpdateScreenStateS2CPayload implements CustomPacketPayload {
    private final byte[] rawData;

    public static final ResourceLocation ID =
            ResourceLocation.fromNamespaceAndPath(RebootedComputers.MOD_ID, "update_screen_state");

    /**
     * A codec that writes/reads exactly the raw byte[].
     */
    public static final StreamCodec<FriendlyByteBuf, UpdateScreenStateS2CPayload> STREAM_CODEC =
            StreamCodec.of(
                    (buf, msg) -> buf.writeByteArray(msg.rawData),
                    buf -> new UpdateScreenStateS2CPayload(buf.readByteArray())
            );

    public static final Type<UpdateScreenStateS2CPayload> TYPE = new Type<>(ID);

    public UpdateScreenStateS2CPayload(byte[] rawData) {
        this.rawData = rawData;
    }

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    /**
     * On the client: wrap the rawData in a FriendlyByteBuf, decode it via
     * the screen’s own codec, and hand off the typed state object.
     */
    @SuppressWarnings("unchecked") // codecs ensure type safety
    public static void receive(UpdateScreenStateS2CPayload msg, NetworkManager.PacketContext ctx) {
        if (ctx.getEnvironment() != Env.CLIENT) return;

        Minecraft.getInstance().execute(() -> {
            if (Minecraft.getInstance().screen instanceof SynchronisedScreen<?> syncScreen) {
                // 1) wrap the bytes into a Netty ByteBuf, then FriendlyByteBuf
                ByteBuf nettyBuf = Unpooled.wrappedBuffer(msg.rawData);
                FriendlyByteBuf friendly = new FriendlyByteBuf(nettyBuf);

                // 2) decode using the screen’s declared codec
                StreamCodec<FriendlyByteBuf, Object> codec =
                        (StreamCodec<FriendlyByteBuf, Object>) syncScreen.getStateCodec();
                Object state = codec.decode(friendly);

                // 3) dispatch the typed state back into the screen
                ((SynchronisedScreen<Object>) syncScreen).read(state);
            }
        });
    }
}
