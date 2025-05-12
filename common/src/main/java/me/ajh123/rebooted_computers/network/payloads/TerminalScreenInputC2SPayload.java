package me.ajh123.rebooted_computers.network.payloads;

import dev.architectury.networking.NetworkManager;
import dev.architectury.utils.Env;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import me.ajh123.rebooted_computers.RebootedComputers;
import me.ajh123.rebooted_computers.gui.screens.ComputerTerminalScreen;
import me.ajh123.rebooted_computers.network.ModPackets;
import me.ajh123.rebooted_computers.vm.VirtualMachine;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.NotNull;

import java.nio.ByteBuffer;

public record TerminalScreenInputC2SPayload(String vmID, ByteBuffer input) implements CustomPacketPayload {
    public static final ResourceLocation ID = ResourceLocation.fromNamespaceAndPath(RebootedComputers.MOD_ID, "terminal_screen_action");
    public static final StreamCodec<FriendlyByteBuf, TerminalScreenInputC2SPayload> STREAM_CODEC =  CustomPacketPayload.codec(TerminalScreenInputC2SPayload::write, TerminalScreenInputC2SPayload::new);
    public static final Type<TerminalScreenInputC2SPayload> TYPE = new Type<>(ID);

    private TerminalScreenInputC2SPayload(FriendlyByteBuf friendlyByteBuf) {
        this(friendlyByteBuf.readUtf(), ModPackets.readByteBuffer(friendlyByteBuf));
    }

    private void write(FriendlyByteBuf friendlyByteBuf) {
        friendlyByteBuf.writeUtf(this.vmID);
        ModPackets.writeByteBuffer(friendlyByteBuf, this.input);
    }

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void receive(TerminalScreenInputC2SPayload t, NetworkManager.PacketContext packetContext) {
        VirtualMachine machine = RebootedComputers.VM_MANAGER.getVM((ServerPlayer) packetContext.getPlayer());

        if (machine != null) {
            machine.getTerminal().putInput(t.input.flip());
        }
    }
}
