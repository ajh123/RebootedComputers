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

public class ECH extends CSISequenceHandler {
    public ECH(final Terminal terminal) {
        super(terminal);
    }

    public void execute(int[] args, int argCount, CSIState state) {
        int chars = args[0];
        Terminal.ColorData c;
        switch (terminal.currentBackgroundColorMode) {
            case SIXTEEN_COLOR -> c = terminal.sixteenColor;
            case TWO_FIFTY_SIX_COLOR -> c = terminal.twoFiftySixColor;
            case TRUE_COLOR -> c = terminal.backgroundColor;
            case SIXTEEN_COLOR_BRIGHT -> c = terminal.sixteenColorBright;
            default -> c = Terminal.DEFAULT_BACKGROUND_COLOR;
        }
        if (terminal.currentPrivateModeState.isAltBufferEnabled()) {
            int fromIndex = terminal.x + terminal.y * Terminal.WIDTH;
            int toIndex = fromIndex + Math.max(Math.min(Math.max(chars, 1), Terminal.WIDTH - terminal.x), 1);
            Arrays.fill(terminal.altBuffer, fromIndex, toIndex, ' ');
            Arrays.fill(terminal.altColors, fromIndex, toIndex, Terminal.DEFAULT_COLORS.Copy());
            Arrays.fill(terminal.altColorsBackground, fromIndex, toIndex, c.Copy());
            Arrays.fill(terminal.altStyles, fromIndex, toIndex, Terminal.DEFAULT_STYLE);
        } else {
            int fromIndex = terminal.x + (terminal.y + (terminal.lastRowToDisplayMax - Terminal.HEIGHT)) * Terminal.WIDTH;
            int toIndex = fromIndex + Math.max(Math.min(Math.max(chars, 1), Terminal.WIDTH - terminal.x), 1);
            Arrays.fill(terminal.buffer, fromIndex, toIndex, ' ');
            Arrays.fill(terminal.colors, fromIndex, toIndex, Terminal.DEFAULT_COLORS.Copy());
            Arrays.fill(terminal.colorsBackground, fromIndex, toIndex, c.Copy());
            Arrays.fill(terminal.styles, fromIndex, toIndex, Terminal.DEFAULT_STYLE);
        }

        terminal.renderers.forEach(model -> model.getDirtyMask().accumulateAndGet(1 << terminal.y, (left, right) -> left | right));
    }
}
