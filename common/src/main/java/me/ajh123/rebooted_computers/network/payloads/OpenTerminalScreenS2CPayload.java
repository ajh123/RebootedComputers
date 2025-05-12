package me.ajh123.rebooted_computers.network.payloads;

import dev.architectury.networking.NetworkManager;
import dev.architectury.utils.Env;
import me.ajh123.rebooted_computers.RebootedComputers;
import me.ajh123.rebooted_computers.gui.screens.ComputerTerminalScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public record OpenTerminalScreenS2CPayload(String vmID) implements CustomPacketPayload {
    public static final ResourceLocation ID = ResourceLocation.fromNamespaceAndPath(RebootedComputers.MOD_ID, "open_terminal_screen");
    public static final StreamCodec<FriendlyByteBuf, OpenTerminalScreenS2CPayload> STREAM_CODEC =  CustomPacketPayload.codec(OpenTerminalScreenS2CPayload::write, OpenTerminalScreenS2CPayload::new);
    public static final CustomPacketPayload.Type<OpenTerminalScreenS2CPayload> TYPE = new CustomPacketPayload.Type<>(ID);

    private OpenTerminalScreenS2CPayload(FriendlyByteBuf friendlyByteBuf) {
        this(friendlyByteBuf.readUtf());
    }

    private void write(FriendlyByteBuf friendlyByteBuf) {
        friendlyByteBuf.writeUtf(this.vmID);
    }

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void receive(OpenTerminalScreenS2CPayload t, NetworkManager.PacketContext packetContext) {
        if (packetContext.getEnvironment() == Env.CLIENT) {
            Minecraft.getInstance().setScreen(new ComputerTerminalScreen(t.vmID()));
        }
    }
}
