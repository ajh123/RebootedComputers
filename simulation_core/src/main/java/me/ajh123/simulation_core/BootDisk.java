package me.ajh123.simulation_core;

import java.io.InputStream;

public record BootDisk(InputStream firmware, InputStream kernel, InputStream rootfs) {
}
