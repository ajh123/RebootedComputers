package me.ajh123.rebooted_computers.vm;

import me.ajh123.rebooted_computers.vm.device.SerialConsole;

import java.io.IOException;

public class DummyVirtualMachine extends VirtualMachine {
    public DummyVirtualMachine(BootDisk bootDisk, SerialConsole console) throws IOException {
        super(bootDisk, console);
    }

    @Override
    public void initialise() throws Exception {
        // do nothing
    }

    @Override
    public void run() throws Exception {
        // do nothing
    }

    @Override
    public boolean isRunning() {
        return true; // always running
    }
}
