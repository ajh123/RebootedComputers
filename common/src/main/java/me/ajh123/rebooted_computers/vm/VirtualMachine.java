package me.ajh123.rebooted_computers.vm;

import dev.architectury.networking.NetworkManager;
import io.netty.buffer.Unpooled;
import li.cil.sedna.api.Sizes;
import li.cil.sedna.api.device.PhysicalMemory;
import li.cil.sedna.device.block.ByteBufferBlockDevice;
import li.cil.sedna.device.memory.Memory;
import li.cil.sedna.device.rtc.GoldfishRTC;
import li.cil.sedna.device.rtc.SystemTimeRealTimeCounter;
import li.cil.sedna.device.serial.UART16550A;
import li.cil.sedna.device.virtio.VirtIOBlockDevice;
import li.cil.sedna.riscv.R5Board;
import me.ajh123.rebooted_computers.RebootedComputers;
import me.ajh123.rebooted_computers.gui.screens.ComputerTerminalScreen;
import me.ajh123.rebooted_computers.network.payloads.UpdateScreenStateS2CPayload;
import me.ajh123.rebooted_computers.vm.terminal.Terminal;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class VirtualMachine {
    private final R5Board board;
    private final PhysicalMemory memory;
    private final UART16550A uart;
    private final GoldfishRTC rtc;
    private final VirtIOBlockDevice hdd;
    private final BootDisk bootDisk;
    private final Terminal terminal;
    private final List<ServerPlayer> terminalUsers = new ArrayList<>();
    private String id;
    private long lastPacketSentTime = 0;
    private static final long PACKET_SEND_INTERVAL_MS = 50; // send max 20 times/sec

    public VirtualMachine(BootDisk bootDisk) throws IOException {
        this.terminal = new Terminal();
        this.board = new R5Board();
        this.bootDisk = bootDisk;
        memory = Memory.create(32 * 1024 * 1024);
        uart = new UART16550A();
        rtc = new GoldfishRTC(SystemTimeRealTimeCounter.get());
        if (bootDisk != null && bootDisk.rootfs() != null) {
            hdd = new VirtIOBlockDevice(board.getMemoryMap(),
                    ByteBufferBlockDevice.createFromStream(this.bootDisk.rootfs(), true));
        } else {
            hdd = null;
        }
    }

    public void addTerminalUser(ServerPlayer player) {
        terminalUsers.add(player);
    }

    public void removeTerminalUser(ServerPlayer player) {
        terminalUsers.remove(player);
    }

    private void sendUpdatePacket() {
        for (ServerPlayer player : terminalUsers) {
            ComputerTerminalScreen.ComputerTerminalScreenState state = new ComputerTerminalScreen.ComputerTerminalScreenState(
                    RebootedComputers.VM_MANAGER.isRunning(this.getId()),
                    this.terminal.getOutput()
            );

            FriendlyByteBuf payload = new FriendlyByteBuf(Unpooled.buffer());
            ComputerTerminalScreen.STREAM_CODEC.encode(payload, state);
            byte[] bytes = new byte[payload.readableBytes()];
            payload.readBytes(bytes);
            NetworkManager.sendToPlayer(player, new UpdateScreenStateS2CPayload(bytes));
        }
    }

    private void maybeSendUpdatePacket() {
        long now = System.currentTimeMillis();
        if (now - lastPacketSentTime >= PACKET_SEND_INTERVAL_MS) {
            sendUpdatePacket();
            lastPacketSentTime = now;
        }
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

        loadProgramFile(memory, bootDisk.firmware());
        loadProgramFile(memory, bootDisk.kernel(), 0x200000);
    }

    public void run() throws Exception {
        board.initialize();
        board.setRunning(true);

        final int cyclesPerSecond = board.getCpu().getFrequency();
        final int cyclesPerStep = 1_000;
        int remaining = 0;

        // Honour interruption in main loop condition
        while (board.isRunning() && !Thread.currentThread().isInterrupted()) {
            final long stepStart = System.currentTimeMillis();

            remaining += cyclesPerSecond;
            while (remaining > 0 && !Thread.currentThread().isInterrupted()) {
                board.step(cyclesPerStep);
                remaining -= cyclesPerStep;

                int value;
                while ((value = uart.read()) != -1) {
                    terminal.putOutput((byte) value);
                }

                while (!terminal.input.isEmpty() && uart.canPutByte()) {
                    uart.putByte(terminal.input.dequeueByte());
                }

                maybeSendUpdatePacket();
            }

            if (board.isRestarting() && !Thread.currentThread().isInterrupted()) {
                loadProgramFile(memory, bootDisk.firmware());
                loadProgramFile(memory, bootDisk.kernel(), 0x200000);
                board.initialize();
            }

            final long stepDuration = System.currentTimeMillis() - stepStart;
            final long sleep = 1_000 - stepDuration;
            if (sleep > 0) {
                try {
                    Thread.sleep(sleep);
                } catch (InterruptedException ie) {
                    // Re-assert interrupt flag and exit loop cleanly
                    Thread.currentThread().interrupt();
                    break;
                }
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Terminal getTerminal() {
        return terminal;
    }
}
