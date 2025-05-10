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

package me.ajh123.rebooted_computers.vm.terminal.escapes.csi;

import me.ajh123.rebooted_computers.vm.terminal.Terminal;

public class CH7 extends CSISequenceHandler { // Combined Handler 7 (XTVERSION, DECLL, DECSCUSR, DECSCA, and XTPOPSGR)
    public CH7(final Terminal terminal) {
        super(terminal);
    }

    public void execute(final int[] args, final int argsCount, final CSIState state) {
        if (state.greaterThan) { // XTVERSION
            if (args[0] == 0) {
                terminal.putResponse("\033P>|oc2rvt(1.0.0)\033\\");
            }
        } else if (state.space) { // DECSCUSR
            int cursorStyle = args[0];
            if (cursorStyle < 0 || cursorStyle > 6) {
                terminal.cursorMode = Terminal.CursorMode.DEFAULT;
                return;
            }
            terminal.cursorMode = cursorStyle;
        } else if (state.quote) { // DECSCA
            System.out.println("DECSCA not implemented");
        } else if (state.hash) { // XTPOPSGR
            System.out.println("XTPOPSGR not implemented");
        } else { // DECLL
            System.out.println("DECLL not implemented");
        }
    }
}
