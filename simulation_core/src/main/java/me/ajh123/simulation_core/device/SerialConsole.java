package me.ajh123.simulation_core.device;

import java.io.IOException;

public interface SerialConsole {
    /** –1 on end‐of‐stream */
    int read() throws IOException;

    /** send a byte to the user */
    void write(int value) throws IOException;

    /** whether there’s data waiting to be read */
    boolean hasInput() throws IOException;
}
