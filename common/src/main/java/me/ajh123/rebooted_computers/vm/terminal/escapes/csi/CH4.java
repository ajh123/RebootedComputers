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

public class CH4 extends CSISequenceHandler { // Combined Handler 4 (XTWINOPS, XTSMTITLE, DECSWBV, and DECRARA)
    public CH4(final Terminal terminal) {
        super(terminal);
    }

    @Override
    public void execute(final int[] args, final int argsCount, final CSIState state) {
        if (state.greaterThan) { // XTSMTITLE
            System.out.println("XTSMTITLE is not implemented");
        } else if (state.space) { // DECSWBV
            System.out.println("DECSWBV is not implemented yet");
        } else if (state.dollarSign) { //DECRARA
            System.out.println("DECRARA is not implemented");
        } else { // XTWINOPS
            switch (args[0]) {
                case 14 -> terminal.putResponse("\033[4;" + Terminal.HEIGHT + ";" + Terminal.WIDTH); //terminal.putResponse("\033[4;" + (Terminal.HEIGHT * Terminal.CHAR_HEIGHT) + ";" + (Terminal.WIDTH * Terminal.CHAR_WIDTH));
                case 15 -> terminal.putResponse("\033[5;" + (Terminal.HEIGHT * Terminal.CHAR_HEIGHT) + ";" + (Terminal.WIDTH * Terminal.CHAR_WIDTH));
                case 16 -> terminal.putResponse("\033[6;" + Terminal.CHAR_HEIGHT + ";" + Terminal.CHAR_WIDTH);
                case 18 -> terminal.putResponse("\033[8;" + Terminal.HEIGHT + ";" + Terminal.WIDTH);
                case 19 -> terminal.putResponse("\033[9;" + Terminal.HEIGHT + ";" + Terminal.WIDTH);
            }
        }
    }
}
