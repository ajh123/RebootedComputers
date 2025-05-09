package me.ajh123.rebooted_computers.fabric;

import me.ajh123.rebooted_computers.RebootedComputers;
import net.fabricmc.api.ModInitializer;

public final class RebootedComputersFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        // This code runs as soon as Minecraft is in a mod-load-ready state.
        // However, some things (like resources) may still be uninitialized.
        // Proceed with mild caution.

        // Run our common setup.
        RebootedComputers.init();
    }
}
