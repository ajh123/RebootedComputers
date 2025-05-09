package me.ajh123.simulation_core;

import li.cil.sedna.buildroot.Buildroot;

public class OperatingSystems {
    public static final BootDisk SEDNA_BUILDROOT = new BootDisk(
            Buildroot.getFirmware(),
            Buildroot.getLinuxImage(),
            Buildroot.getRootFilesystem()
    );
}
