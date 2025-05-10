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

package me.ajh123.rebooted_computers.vm.terminal.modes;

import java.util.HashMap;

public class ImplementedPrivateModes {
    public static HashMap<Integer, Boolean> modeStatus = new HashMap<>();

    public static ImplementedPrivateModes instance = new ImplementedPrivateModes();

    public ImplementedPrivateModes() {
        modeStatus.put(1, true); // DECCKM;
        modeStatus.put(2, false); // DECANM;
        modeStatus.put(3, false); // DECCOLM;
        modeStatus.put(4, true); // DECSCLM;
        modeStatus.put(5, true); // DECSCNM; Has no function, meant for older terminals
        modeStatus.put(6, true); // DECOM;
        modeStatus.put(7, true); // DECAWM;
        modeStatus.put(8, true); // DECARM;
        modeStatus.put(9, false); // X10MM;
        modeStatus.put(10, false); // TOOLBAR;
        modeStatus.put(12, true); // START_BLINKING_CURSOR;
        modeStatus.put(13, true); // START_BLINKING_CURSOR2;
        modeStatus.put(14, false); // XORBLINK;
        modeStatus.put(18, false); // DECPFF;
        modeStatus.put(19, false); // DECPEX;
        modeStatus.put(25, true); // DECTCEM;
        modeStatus.put(30, false); // SHOW_SCROLL;
        modeStatus.put(35, false); // FONT_SHIFT;
        modeStatus.put(38, false); // TEKTRONIX;
        modeStatus.put(40, false); // ENABLE_80_132;
        modeStatus.put(41, false); // MORE_FIX;
        modeStatus.put(42, false); // DECNRCM;
        modeStatus.put(43, false); // DECGEPM;
        modeStatus.put(44, false); // MARG_BELL;
        modeStatus.put(45, false); // XTREVWRAP;
        modeStatus.put(46, false); // XTLOGGING;
        modeStatus.put(47, true); // ALT_BUFFER;
        modeStatus.put(66, false); // DECNKM;
        modeStatus.put(67, false); // DECBKM;
        modeStatus.put(69, false); // DECLRMM;
        modeStatus.put(80, false); // DECSDM;
        modeStatus.put(96, false); // DECNCSM;
        modeStatus.put(1000, true); // X11MM;
        modeStatus.put(1001, false); // HILITE_MOUSE;
        modeStatus.put(1002, true); // CELL_MOTION_MOUSE; -- PARTIAL SUPPORT: Treated as X11MM currently, no motion events are sent. This actually seems to match Window Terminal's level of implementation.
        modeStatus.put(1003, false); // ALL_MOTION_MOUSE_TRACKING;
        modeStatus.put(1004, true); // FOCUS_IN_FOCUS_OUT;
        modeStatus.put(1005, true); // UTF8_MOUSE;
        modeStatus.put(1006, true); // SGR_MOUSE;
        modeStatus.put(1007, false); // ALTERNATE_SCROLL_MODE;
        modeStatus.put(1010, false); // SCROLL_BOTTOM_ON_OUTPUT;
        modeStatus.put(1011, false); // SCROLL_BOTTOM_ON_KEY_PRESS;
        modeStatus.put(1014, false); // FAST_SCROLL;
        modeStatus.put(1015, true); // URXVT_MOUSE;
        modeStatus.put(1016, false); // SGR_MOUSE_PIXEL;
        modeStatus.put(1034, false); // META_KEY;
        modeStatus.put(1035, false); // SPECIAL_MODIFIERS;
        modeStatus.put(1036, false); // META_SENDS_ESCAPE;
        modeStatus.put(1037, false); // DEL_EDIT_KEYPAD_DEL;
        modeStatus.put(1039, false); // ALT_SENDS_ESC;
        modeStatus.put(1040, false); // KEEP_SELECTION;
        modeStatus.put(1041, false); // USE_CLIP;
        modeStatus.put(1042, false); // ENABLE_URGENCY;
        modeStatus.put(1043, false); // RAISE_ON_CTRL_G;
        modeStatus.put(1044, false); // KEEP_CLIP;
        modeStatus.put(1045, false); // EXT_REV_WRAP;
        modeStatus.put(1046, false); // ALLOW_ALT_BUFFER;
        modeStatus.put(1047, true); // SWITCH_ALT_BUFFER;
        modeStatus.put(1048, true); // SAVE_CURSOR;
        modeStatus.put(1049, true); // SAVE_CLEAR_AND_SWITCH;
        modeStatus.put(1050, false); // SET_TERMINFO_FUNC_KEY_MODE;
        modeStatus.put(1051, false); // SET_SUN_KEY_MODE;
        modeStatus.put(1052, false); // SET_HP_K0EY_MODE;
        modeStatus.put(1053, false); // SET_SCO_KEY_MODE;
        modeStatus.put(1060, false); // SET_LEGACY_KEYBOARD;
        modeStatus.put(1061, false); // SET_VT220_KEYBOARD;
        modeStatus.put(2001, false); // ENABLE_READLINE_MOUSE_1;
        modeStatus.put(2002, false); // ENABLE_READLINE_MOUSE_2;
        modeStatus.put(2003, false); // ENABLE_READLINE_MOUSE_3;
        modeStatus.put(2004, true); // SET_BRACKETED_PASTE;
        modeStatus.put(2005, false); // ENABLE_READLINE_CHAR_QUOTE;
        modeStatus.put(2006, false); // ENABLE_READLINE_NEWLINE_PASTE;
        modeStatus.put(2026, true); // APPLICATION_SYNC;
        modeStatus.put(7727, true); // APPLICATION_ESC_MODE;
    }

    public void modeUsed(int mode, boolean state) {
        if (!modeStatus.get(mode)) {
            System.out.println("Unimplemented Mode: " + mode + " was " + (state ? "set." : "reset."));
        }
    }
}
