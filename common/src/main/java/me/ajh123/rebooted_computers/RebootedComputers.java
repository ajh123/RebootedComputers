package me.ajh123.rebooted_computers;

import dev.architectury.event.events.common.LifecycleEvent;
import dev.architectury.event.events.common.PlayerEvent;
import dev.architectury.networking.NetworkManager;
import li.cil.sedna.Sedna;
import me.ajh123.rebooted_computers.network.ModPackets;
import me.ajh123.rebooted_computers.network.payloads.OpenTerminalScreenS2CPayload;
import me.ajh123.rebooted_computers.vm.BootDisk;
import me.ajh123.rebooted_computers.vm.OperatingSystems;
import me.ajh123.rebooted_computers.vm.ServerVMManager;
import me.ajh123.rebooted_computers.vm.VirtualMachine;

import java.io.IOException;

public final class RebootedComputers {
    public static final String MOD_ID = "rebooted_computers";
    public static final ServerVMManager VM_MANAGER = new ServerVMManager();

    public static void init() {
        ModPackets.init();

        Sedna.initialize();

        BootDisk bootDisk = OperatingSystems.SEDNA_BUILDROOT;

        LifecycleEvent.SERVER_BEFORE_START.register(server -> {
            VM_MANAGER.unregisterAll();

            try {
                final VirtualMachine vm = new VirtualMachine(
                        bootDisk
                );
                VM_MANAGER.registerVM("test-vm", vm);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            VM_MANAGER.startVM("test-vm");
        });

        PlayerEvent.PLAYER_JOIN.register(player -> {
            NetworkManager.sendToPlayer(player, new OpenTerminalScreenS2CPayload("1"));
            VirtualMachine vm = VM_MANAGER.getVM("test-vm");
            if (vm != null) {
                VM_MANAGER.setVM(player, vm);
            }
        });
    }
}
