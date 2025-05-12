package me.ajh123.rebooted_computers.gui.screens;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;

/**
 * A screen whose state can be kept in sync with the server via a codec.
 *
 * @param <S> the type of the screen-state object
 */
public interface SynchronisedScreen<S> {
    /**
     * @return a codec that can read/write the screen state from/to a FriendlyByteBuf
     */
    StreamCodec<FriendlyByteBuf, S> getStateCodec();

    /**
     * Called when a fresh state S has been decoded from the server.
     */
    void read(S state);
}
