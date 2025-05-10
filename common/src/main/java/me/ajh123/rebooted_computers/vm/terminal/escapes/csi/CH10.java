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

import java.util.Arrays;

public class CH10 extends CSISequenceHandler { // Combined Handler 10 (DCH and XTPUSHCOLORS)
    public CH10(final Terminal terminal) {
        super(terminal);
    }

    @Override
    public void execute(final int[] args, final int argsCount, final CSIState state) {
        if (state.hash) { // XTPUSHCOLORS
            System.out.println("XTPUSHCOLORS not implemented");
        } else { // DCH
            int chars = Math.min(Math.max(args[0], 1), Terminal.WIDTH - terminal.x);
            int startIndex = ((terminal.currentPrivateModeState.isAltBufferEnabled()) ? terminal.y * Terminal.WIDTH : (terminal.y + (terminal.lastRowToDisplayMax - Terminal.HEIGHT)) * Terminal.WIDTH) + terminal.x;
            int count = (Terminal.WIDTH - terminal.x) - chars;
            int endIndex = startIndex + count;
            Terminal.ColorData c;
            switch (terminal.currentBackgroundColorMode) {
                case SIXTEEN_COLOR -> c = terminal.sixteenColor;
                case TWO_FIFTY_SIX_COLOR -> c = terminal.twoFiftySixColor;
                case TRUE_COLOR -> c = terminal.backgroundColor;
                case SIXTEEN_COLOR_BRIGHT -> c = terminal.sixteenColorBright;
                default -> c = Terminal.DEFAULT_BACKGROUND_COLOR;
            }
            if (terminal.currentPrivateModeState.isAltBufferEnabled()) {
                System.arraycopy(terminal.altBuffer, startIndex + chars, terminal.altBuffer, startIndex, count);
                System.arraycopy(terminal.altColors, startIndex + chars, terminal.altColors, startIndex, count);
                System.arraycopy(terminal.altColorsBackground, startIndex + chars, terminal.altColorsBackground, startIndex, count);
                System.arraycopy(terminal.altStyles, startIndex + chars, terminal.altStyles, startIndex, count);
                Arrays.fill(terminal.altBuffer, endIndex, endIndex + chars + 1, ' ');
                Arrays.fill(terminal.altColors, endIndex, endIndex + chars + 1, Terminal.DEFAULT_COLORS.Copy());
                Arrays.fill(terminal.altColorsBackground, endIndex, endIndex + chars + 1, c.Copy());
                Arrays.fill(terminal.altStyles, endIndex, endIndex + chars + 1, Terminal.DEFAULT_STYLE);
            } else {
                System.arraycopy(terminal.buffer, startIndex + chars, terminal.buffer, startIndex, count);
                System.arraycopy(terminal.colors, startIndex + chars, terminal.colors, startIndex, count);
                System.arraycopy(terminal.colorsBackground, startIndex + chars, terminal.colorsBackground, startIndex, count);
                System.arraycopy(terminal.styles, startIndex + chars, terminal.styles, startIndex, count);
                Arrays.fill(terminal.buffer, endIndex, endIndex + chars + 1, ' ');
                Arrays.fill(terminal.colors, endIndex, endIndex + chars + 1, Terminal.DEFAULT_COLORS.Copy());
                Arrays.fill(terminal.colorsBackground, endIndex, endIndex + chars + 1, c.Copy());
                Arrays.fill(terminal.styles, endIndex, endIndex + chars + 1, Terminal.DEFAULT_STYLE);
            }

            terminal.renderers.forEach(model -> model.getDirtyMask().accumulateAndGet(1 << terminal.y, (left, right) -> left | right));
        }
    }
}
