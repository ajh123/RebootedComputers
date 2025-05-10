package me.ajh123.rebooted_computers.vm.device;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;

public class SystemStreamSerialConsole implements SerialConsole {
    private final BufferedReader reader;
    private final PrintStream out;

    public SystemStreamSerialConsole() {
        this.reader = new BufferedReader(new InputStreamReader(System.in));
        this.out    = System.out;
    }

    @Override
    public int read() throws IOException {
        return reader.read();
    }

    @Override
    public boolean hasInput() throws IOException {
        return reader.ready();
    }

    @Override
    public void write(final int value) {
        out.print((char) value);
        out.flush();
    }
}
