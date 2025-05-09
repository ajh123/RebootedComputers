package me.ajh123.rebooted_computers.neoforge;

import me.ajh123.rebooted_computers.RebootedComputers;
import net.neoforged.fml.common.Mod;

@Mod(RebootedComputers.MOD_ID)
public final class RebootedComputersNeoForge {
    public RebootedComputersNeoForge() {
        // Run our common setup.
        RebootedComputers.init();
    }
}
