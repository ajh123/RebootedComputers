package me.ajh123.simulation_core;

import li.cil.sedna.Sedna;

public class SimpleCLIRunner {
    public static void main(String[] args) throws Exception {
        Sedna.initialize();
        BootDisk bootDisk = OperatingSystems.SEDNA_BUILDROOT;

        final VirtualMachine vm = new VirtualMachine(
            bootDisk
        );
        vm.initialise();
        vm.run();
    }
}
