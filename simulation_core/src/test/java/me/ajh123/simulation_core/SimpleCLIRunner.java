package me.ajh123.simulation_core;

import li.cil.sedna.Sedna;
import me.ajh123.simulation_core.device.SystemStreamSerialConsole;

public class SimpleCLIRunner {
    public static void main(String[] args) throws Exception {
        Sedna.initialize();
        SystemStreamSerialConsole console = new SystemStreamSerialConsole();
        BootDisk bootDisk = OperatingSystems.SEDNA_BUILDROOT;

        final VirtualMachine vm = new VirtualMachine(
            bootDisk,
            console
        );
        vm.initialise();
        vm.run();
    }
}
