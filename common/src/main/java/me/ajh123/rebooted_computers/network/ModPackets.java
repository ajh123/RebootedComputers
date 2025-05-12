package me.ajh123.rebooted_computers.network;

import dev.architectury.networking.NetworkManager;
import jakarta.annotation.Nullable;
import me.ajh123.rebooted_computers.network.payloads.OpenTerminalScreenS2CPayload;
import me.ajh123.rebooted_computers.network.payloads.TerminalScreenInputC2SPayload;
import me.ajh123.rebooted_computers.network.payloads.UpdateScreenStateS2CPayload;
import net.minecraft.network.FriendlyByteBuf;

import java.nio.ByteBuffer;

public class ModPackets {
    public static void init() {
        NetworkManager.registerReceiver(
                NetworkManager.Side.S2C,
                OpenTerminalScreenS2CPayload.TYPE,
                OpenTerminalScreenS2CPayload.STREAM_CODEC,
                OpenTerminalScreenS2CPayload::receive
        );

        NetworkManager.registerReceiver(
                NetworkManager.Side.S2C,
                UpdateScreenStateS2CPayload.TYPE,
                UpdateScreenStateS2CPayload.STREAM_CODEC,
                UpdateScreenStateS2CPayload::receive
        );

        NetworkManager.registerReceiver(
                NetworkManager.Side.C2S,
                TerminalScreenInputC2SPayload.TYPE,
                TerminalScreenInputC2SPayload.STREAM_CODEC,
                TerminalScreenInputC2SPayload::receive
        );
    }

    public static void writeByteBuffer(FriendlyByteBuf buf, @Nullable ByteBuffer payload) {
        if (payload != null) {
            buf.writeInt(payload.remaining());
            buf.writeBytes(payload);
        } else {
            buf.writeInt(0);
        }
    }

    public static ByteBuffer readByteBuffer(FriendlyByteBuf buf) {
        int length = buf.readInt();
        if (length > 0) {
            ByteBuffer byteBuffer = ByteBuffer.allocate(length);
            buf.readBytes(byteBuffer);
            return byteBuffer;
        }
        return ByteBuffer.allocate(0);
    }
}
