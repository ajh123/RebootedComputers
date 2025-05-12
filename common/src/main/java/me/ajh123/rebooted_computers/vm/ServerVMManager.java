package me.ajh123.rebooted_computers.vm;

import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Manages multiple VirtualMachine instances, each running in its own Thread.
 */
public class ServerVMManager {
    private final Map<String, VirtualMachine> vms = new ConcurrentHashMap<>();
    private final Map<ServerPlayer, VirtualMachine> terminalUsers = new ConcurrentHashMap<>();
    private final Map<String, Thread> threads = new ConcurrentHashMap<>();

    /**
     * Get the VM for a given player.
     */
    public @Nullable VirtualMachine getVM(final ServerPlayer player) {
        return terminalUsers.get(player);
    }

    /**
     * Set the VM for a given player.
     */
    public void setVM(final ServerPlayer player, final VirtualMachine vm) {
        if (vm == null) {
            terminalUsers.remove(player);
        } else {
            terminalUsers.put(player, vm);
            vm.addTerminalUser(player);
        }
    }

    /**
     * Remove player from all VMs.
     */
    public void removePlayer(final ServerPlayer player) {
        VirtualMachine vm = terminalUsers.remove(player);
        if (vm != null) {
            vm.removeTerminalUser(player);
        }
    }

    /**
     * Register a VM under a unique id.
     */
    public void registerVM(final String id, final VirtualMachine vm) {
        if (vms.containsKey(id)) {
            throw new IllegalArgumentException("VM id already in use: " + id);
        }
        vm.setId(id);
        vms.put(id, vm);
    }

    /**
     * Unregister a VM.
     */
    public void unregisterVM(final String id) {
        if (!vms.containsKey(id)) {
            throw new IllegalArgumentException("No VM registered with id: " + id);
        }
        vms.remove(id);
        Thread t = threads.remove(id);
        if (t != null && t.isAlive()) {
            t.interrupt();
        }
    }

    /**
     * Start a single VM in its own thread.
     */
    public void startVM(final String id) {
        VirtualMachine vm = vms.get(id);
        if (vm == null) {
            throw new IllegalArgumentException("No VM registered with id: " + id);
        }
        Thread t = new Thread(() -> {
            try {
                vm.initialise();
                vm.run();
            } catch (InterruptedException ie) {
                // honour interruption for clean shutdown
                Thread.currentThread().interrupt();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, "VM-" + id);
        threads.put(id, t);
        t.start();
    }

    /**
     * Stop a running VM by interrupting its thread.
     */
    public void stopVM(final String id) {
        Thread t = threads.get(id);
        if (t != null && t.isAlive()) {
            t.interrupt();
        }
    }

    /**
     * Start all registered VMs.
     */
    public void startAll() {
        for (String id : vms.keySet()) {
            startVM(id);
        }
    }

    /**
     * Stop all running VMs.
     */
    public void stopAll() {
        for (String id : threads.keySet()) {
            stopVM(id);
        }
    }

    /**
     * Unregister all VMs.
     */
    public void unregisterAll() {
        for (String id : vms.keySet()) {
            unregisterVM(id);
        }
    }

    /**
     * Get a VM by its id.
     */
    public @Nullable VirtualMachine getVM(final String id) {
        return vms.get(id);
    }

    /**
     * Check if a VM is alive.
     */
    public boolean isRunning(final String id) {
        Thread t = threads.get(id);
        return t != null && t.isAlive();
    }
}
