package me.ajh123.rebooted_computers.network;

import dev.architectury.networking.NetworkManager;
import me.ajh123.rebooted_computers.network.payloads.OpenTerminalScreenS2CPayload;

public class ModPackets {
    public static void init() {
        NetworkManager.registerReceiver(
                NetworkManager.Side.S2C,
                OpenTerminalScreenS2CPayload.TYPE,
                OpenTerminalScreenS2CPayload.STREAM_CODEC,
                OpenTerminalScreenS2CPayload::receive
        );
    }
}
