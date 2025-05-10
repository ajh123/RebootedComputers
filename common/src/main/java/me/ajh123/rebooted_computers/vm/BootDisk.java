package me.ajh123.rebooted_computers.vm;

import java.io.InputStream;

public record BootDisk(InputStream firmware, InputStream kernel, InputStream rootfs) {
}
