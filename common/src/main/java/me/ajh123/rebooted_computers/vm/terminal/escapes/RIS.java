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

package me.ajh123.rebooted_computers.vm.terminal.escapes;

import me.ajh123.rebooted_computers.vm.terminal.Terminal;
import me.ajh123.rebooted_computers.vm.terminal.modes.ModeState;
import me.ajh123.rebooted_computers.vm.terminal.modes.PrivateModeState;

import java.util.Arrays;

public class RIS {
    public static void execute(Terminal terminal) {
        terminal.currentForegroundColorMode = Terminal.ColorMode.SIXTEEN_COLOR;
        terminal.currentBackgroundColorMode = Terminal.ColorMode.DEFAULT_BACKGROUND;
        terminal.Use1006 = false;
        terminal.sixteenColor = Terminal.DEFAULT_COLORS.Copy();
        terminal.sixteenColorBright = Terminal.DEFAULT_BRIGHT_COLORS.Copy();
        terminal.backgroundColor = Terminal.DEFAULT_TRUE_COLOR_BACKGROUND.Copy();
        terminal.foregroundColor = Terminal.DEFAULT_TRUE_COLOR_FOREGROUND.Copy();
        terminal.twoFiftySixColor = Terminal.DEFAULT_256_COLORS.Copy();
        terminal.style = Terminal.DEFAULT_STYLE;
        terminal.currentModeState = new ModeState();
        terminal.currentPrivateModeState = new PrivateModeState();
        terminal.lastRowToDisplay = 24;
        terminal.lastRowToDisplayMax = 24;
        terminal.drawingModeG0 = Terminal.DrawingMode.ASCII;
        terminal.drawingModeG1 = Terminal.DrawingMode.ASCII;
        terminal.useG0 = true;
        terminal.clear();
        terminal.clearAlt();
        Arrays.fill(terminal.buffer, ' ');
        Arrays.fill(terminal.colors, Terminal.DEFAULT_COLORS.Copy());
        Arrays.fill(terminal.colorsBackground, Terminal.DEFAULT_BACKGROUND_COLOR.Copy());
        Arrays.fill(terminal.styles, Terminal.DEFAULT_STYLE);
        Arrays.fill(terminal.altBuffer, ' ');
        Arrays.fill(terminal.altColors, Terminal.DEFAULT_COLORS.Copy());
        Arrays.fill(terminal.altColorsBackground, Terminal.DEFAULT_BACKGROUND_COLOR.Copy());
        Arrays.fill(terminal.altStyles, Terminal.DEFAULT_STYLE);
        Arrays.fill(terminal.tabs, false);
        Arrays.fill(terminal.altTabs, false);
        for (int i = 1; i < Terminal.WIDTH; i++) {
            if (i % Terminal.TAB_WIDTH == 0) {
                terminal.tabs[i] = true;
                terminal.altTabs[i] = true;
            }
        }
    }
}
