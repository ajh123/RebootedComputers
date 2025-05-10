package me.ajh123.rebooted_computers.vm;

import li.cil.sedna.buildroot.Buildroot;

public class OperatingSystems {
    public static final BootDisk SEDNA_BUILDROOT = new BootDisk(
            Buildroot.getFirmware(),
            Buildroot.getLinuxImage(),
            Buildroot.getRootFilesystem()
    );
}
