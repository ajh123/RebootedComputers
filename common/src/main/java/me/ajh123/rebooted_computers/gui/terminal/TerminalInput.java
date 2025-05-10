/*
 * This file contains code from Open Computers 2 which is licensed under the MIT.
 * This means the Rebooted Computers license does not apply to this file.
 *
 * See below for the full license:
 *
 * MIT License
 *
 * Copyright (c) 2020-2021 Florian "Sangar" Nücke
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 *
 * -------------------------------------------------------------------------------
 *
 * All images / textures and localization strings (resources) are put in the
 * public domain, unless explicitly excluded below. More specifically, see CC0 1.0
 * Universal:
 *
 *       http://creativecommons.org/publicdomain/zero/1.0/
 *
 */

package me.ajh123.rebooted_computers.gui.terminal;

import it.unimi.dsi.fastutil.ints.Int2ObjectArrayMap;
import org.lwjgl.glfw.GLFW;

import jakarta.annotation.Nullable;

public class TerminalInput {
    private static final Int2ObjectArrayMap<Int2ObjectArrayMap<byte[]>> KEYCODE_SEQUENCES = new Int2ObjectArrayMap<>();
    private static final Int2ObjectArrayMap<Int2ObjectArrayMap<byte[]>> DECCKM_KEYCODE_SEQUENCES = new Int2ObjectArrayMap<>();

    static {
        addSequence(GLFW.GLFW_KEY_ENTER, '\r');
        addSequence(GLFW.GLFW_KEY_TAB, '\t');
        addSequence(GLFW.GLFW_KEY_BACKSPACE, '\b');

        addSequence(GLFW.GLFW_KEY_ESCAPE, "\33");
        addSequence(GLFW.GLFW_KEY_HOME, "\033[1~");
        addSequence(GLFW.GLFW_KEY_INSERT, "\033[2~");
        addSequence(GLFW.GLFW_KEY_DELETE, "\033[3~");
        addSequence(GLFW.GLFW_KEY_END, "\033[4~");
        addSequence(GLFW.GLFW_KEY_PAGE_UP, "\033[5~");
        addSequence(GLFW.GLFW_KEY_PAGE_DOWN, "\033[6~");

        addSequence(GLFW.GLFW_KEY_F1, "\033[11~");
        addSequence(GLFW.GLFW_KEY_F2, "\033[12~");
        addSequence(GLFW.GLFW_KEY_F3, "\033[13~");
        addSequence(GLFW.GLFW_KEY_F4, "\033[14~");
        addSequence(GLFW.GLFW_KEY_F5, "\033[15~");
        addSequence(GLFW.GLFW_KEY_F6, "\033[17~");
        addSequence(GLFW.GLFW_KEY_F7, "\033[18~");
        addSequence(GLFW.GLFW_KEY_F8, "\033[19~");
        addSequence(GLFW.GLFW_KEY_F9, "\033[20~");
        addSequence(GLFW.GLFW_KEY_F10, "\033[21~");
        addSequence(GLFW.GLFW_KEY_F11, "\033[23~");
        addSequence(GLFW.GLFW_KEY_F12, "\033[24~");

        addSequence(GLFW.GLFW_KEY_UP, "\033[A");
        addSequence(GLFW.GLFW_KEY_DOWN, "\033[B");
        addSequence(GLFW.GLFW_KEY_RIGHT, "\033[C");
        addSequence(GLFW.GLFW_KEY_LEFT, "\033[D");

        for (int i = 'A'; i <= 'Z'; i++) {
            addSequence(
                    GLFW.GLFW_MOD_CONTROL,
                    GLFW.GLFW_KEY_A + (i - 'A'),
                    (byte) (1 + i - 'A')
            );
            addSequence(
                    GLFW.GLFW_MOD_CONTROL | GLFW.GLFW_MOD_SHIFT,
                    GLFW.GLFW_KEY_A + (i - 'A'),
                    (byte) (1 + i - 'A')
            );

            addSequence(
                    GLFW.GLFW_MOD_ALT,
                    GLFW.GLFW_KEY_A + (i - 'A'),
                    (byte) '\033', (byte) ('a' + i - 'A')
            );
            addSequence(
                    GLFW.GLFW_MOD_ALT | GLFW.GLFW_MOD_SHIFT,
                    GLFW.GLFW_KEY_A + (i - 'A'),
                    (byte) '\033', (byte) (i + 128)
            );
        }

        addSequence(GLFW.GLFW_MOD_CONTROL, GLFW.GLFW_KEY_LEFT_BRACKET, (byte) '\033');
        addSequence(GLFW.GLFW_MOD_CONTROL | GLFW.GLFW_MOD_SHIFT, GLFW.GLFW_KEY_LEFT_BRACKET, (byte) '\033');
        addSequence(GLFW.GLFW_MOD_CONTROL, GLFW.GLFW_KEY_BACKSLASH, (byte) '\034');
        addSequence(GLFW.GLFW_MOD_CONTROL | GLFW.GLFW_MOD_SHIFT, GLFW.GLFW_KEY_BACKSLASH, (byte) '\034');
        addSequence(GLFW.GLFW_MOD_CONTROL, GLFW.GLFW_KEY_RIGHT_BRACKET, (byte) '\035');
        addSequence(GLFW.GLFW_MOD_CONTROL | GLFW.GLFW_MOD_SHIFT, GLFW.GLFW_KEY_RIGHT_BRACKET, (byte) '\035');

        addDECCKMSequence(GLFW.GLFW_KEY_UP, "\033OA");
        addDECCKMSequence(GLFW.GLFW_KEY_DOWN, "\033OB");
        addDECCKMSequence(GLFW.GLFW_KEY_RIGHT, "\033OC");
        addDECCKMSequence(GLFW.GLFW_KEY_LEFT, "\033OD");
    }

    ///////////////////////////////////////////////////////////////////

    @Nullable
    public static byte[] getSequence(final int keyCode) {
        return getSequence(keyCode, 0);
    }

    @Nullable
    public static byte[] getSequence(final int keyCode, final int modifiers) {
        final Int2ObjectArrayMap<byte[]> map = KEYCODE_SEQUENCES.get(modifiers);
        if (map == null) {
            return null;
        }
        return map.get(keyCode);
    }

    ///////////////////////////////////////////////////////////////////

    @Nullable
    public static byte[] getDECCKMSequence(final int keyCode) {
        return getDECCKMSequence(keyCode, 0);
    }

    @Nullable
    public static byte[] getDECCKMSequence(final int keyCode, final int modifiers) {
        final Int2ObjectArrayMap<byte[]> map = DECCKM_KEYCODE_SEQUENCES.get(modifiers);
        if (map == null) {
            return null;
        }
        return map.get(keyCode);
    }

    ///////////////////////////////////////////////////////////////////

    private static void addSequence(final int keyCode, final char ch) {
        addSequence(keyCode, (byte) ch);
    }

    private static void addSequence(final int keyCode, final byte... sequence) {
        addSequence(0, keyCode, sequence);
    }

    private static void addSequence(final int keyCode, final String sequence) {
        addSequence(0, keyCode, sequence);
    }

    private static void addSequence(final int modifiers, final int keyCode, final String sequence) {
        final byte[] bytes = new byte[sequence.length()];
        final char[] chars = sequence.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            bytes[i] = (byte) chars[i];
        }
        addSequence(modifiers, keyCode, bytes);
    }

    private static void addSequence(final int modifiers, final int keyCode, final byte... sequence) {
        KEYCODE_SEQUENCES
                .computeIfAbsent(modifiers, i -> new Int2ObjectArrayMap<>())
                .put(keyCode, sequence);
    }

    ///////////////////////////////////////////////////////////////////

    private static void addDECCKMSequence(final int keyCode, final char ch) {
        addDECCKMSequence(keyCode, (byte) ch);
    }

    private static void addDECCKMSequence(final int keyCode, final byte... sequence) {
        addDECCKMSequence(0, keyCode, sequence);
    }

    private static void addDECCKMSequence(final int keyCode, final String sequence) {
        addDECCKMSequence(0, keyCode, sequence);
    }

    private static void addDECCKMSequence(final int modifiers, final int keyCode, final String sequence) {
        final byte[] bytes = new byte[sequence.length()];
        final char[] chars = sequence.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            bytes[i] = (byte) chars[i];
        }
        addDECCKMSequence(modifiers, keyCode, bytes);
    }

    private static void addDECCKMSequence(final int modifiers, final int keyCode, final byte... sequence) {
        DECCKM_KEYCODE_SEQUENCES
                .computeIfAbsent(modifiers, i -> new Int2ObjectArrayMap<>())
                .put(keyCode, sequence);
    }
}
