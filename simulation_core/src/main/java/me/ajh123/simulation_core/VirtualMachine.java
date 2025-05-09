package me.ajh123.simulation_core;

import li.cil.sedna.api.Sizes;
import li.cil.sedna.api.device.PhysicalMemory;
import li.cil.sedna.device.block.ByteBufferBlockDevice;
import li.cil.sedna.device.memory.Memory;
import li.cil.sedna.device.rtc.GoldfishRTC;
import li.cil.sedna.device.rtc.SystemTimeRealTimeCounter;
import li.cil.sedna.device.serial.UART16550A;
import li.cil.sedna.device.virtio.VirtIOBlockDevice;
import li.cil.sedna.riscv.R5Board;
import me.ajh123.simulation_core.device.SerialConsole;

import java.io.*;

public class VirtualMachine {
    private final R5Board board;
    private final PhysicalMemory memory;
    private final UART16550A uart;
    private final GoldfishRTC rtc;
    private final VirtIOBlockDevice hdd;
    private final BootDisk images;
    private final SerialConsole console;

    public VirtualMachine(BootDisk bootDisk, SerialConsole console) throws IOException {
        this.console = console;
        this.board = new R5Board();
        this.images = bootDisk;
        memory = Memory.create(32 * 1024 * 1024);
        uart = new UART16550A();
        rtc = new GoldfishRTC(SystemTimeRealTimeCounter.get());
        hdd = new VirtIOBlockDevice(board.getMemoryMap(),
                ByteBufferBlockDevice.createFromStream(images.rootfs(), true));
    }

    public void initialise() throws Exception {
        uart.getInterrupt().set(0xA, board.getInterruptController());
        rtc.getInterrupt().set(0xB, board.getInterruptController());
        hdd.getInterrupt().set(0x1, board.getInterruptController());

        board.addDevice(0x80000000L, memory);
        board.addDevice(uart);
        board.addDevice(rtc);
        board.addDevice(hdd);

        board.setBootArguments("root=/dev/vda ro");
        board.setStandardOutputDevice(uart);

        board.reset();

        loadProgramFile(memory, images.firmware());
        loadProgramFile(memory, images.kernel(), 0x200000);
    }

    public void run() throws Exception {
        board.initialize();
        board.setRunning(true);

        final int cyclesPerSecond = board.getCpu().getFrequency();
        final int cyclesPerStep = 1_000;
        int remaining = 0;

        while (board.isRunning()) {
            final long stepStart = System.currentTimeMillis();

            remaining += cyclesPerSecond;
            while (remaining > 0) {
                board.step(cyclesPerStep);
                remaining -= cyclesPerStep;

                int value;
                while ((value = uart.read()) != -1) {
                    console.write(value);
                }

                while (console.hasInput() && uart.canPutByte()) {
                    uart.putByte((byte) console.read());
                }
            }

            if (board.isRestarting()) {
                loadProgramFile(memory, images.firmware());
                loadProgramFile(memory, images.kernel(), 0x200000);
                board.initialize();
            }

            final long stepDuration = System.currentTimeMillis() - stepStart;
            final long sleep = 1000 - stepDuration;
            if (sleep > 0) {
                Thread.sleep(sleep);
            }
        }
    }

    private static void loadProgramFile(final PhysicalMemory memory, final InputStream stream) throws Exception {
        loadProgramFile(memory, stream, 0);
    }

    private static void loadProgramFile(final PhysicalMemory memory, final InputStream stream, final int offset) throws IOException {
        final BufferedInputStream bis = new BufferedInputStream(stream);
        for (int address = offset, value = bis.read(); value != -1; value = bis.read(), address++) {
            memory.store(address, (byte) value, Sizes.SIZE_8_LOG2);
        }
    }
}
