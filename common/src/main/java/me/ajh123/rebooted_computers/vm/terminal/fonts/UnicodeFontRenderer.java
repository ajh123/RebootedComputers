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

package me.ajh123.rebooted_computers.vm.terminal.fonts;

import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

public class UnicodeFontRenderer {
    public final Font font;
    private final Map<Integer, Glyph> glyphCache = new HashMap<>();
    private final FontRenderContext frc = new FontRenderContext(null, true, false);
    private final boolean isItalic;

    public UnicodeFontRenderer(Font font, boolean isItalic) {
        this.font = font;
        this.isItalic = isItalic;

        String initialSet = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz!@#$%^&*()_+-=_.,:;<>?;':\"\\|`~[]{}1234567890△▽ ";
        int[] characters = initialSet.codePoints().toArray();
        for (final int character : characters) {
            getGlyph(character);
        }
    }

    public Glyph getGlyph(int character) {
        return glyphCache.computeIfAbsent(character, this::rasterizeGlyph);
    }

    private Glyph rasterizeGlyph(int character) {
        GlyphVector gv = font.createGlyphVector(frc, Character.toChars(character));
        BufferedImage img = new BufferedImage((isItalic) ? 44 : 20, 32, BufferedImage.TYPE_INT_ARGB); // size can be dynamic
        Graphics2D g = img.createGraphics();

        g.setFont(font);
        g.setColor(Color.WHITE);

        FontMetrics metrics = g.getFontMetrics();
        int ascent = metrics.getAscent();

        g.drawGlyphVector(gv, 0, ascent - 1);
        g.dispose();

        Glyph glyph = new Glyph(img, (isItalic) ? 44 : 20, 32, (int) gv.getGlyphMetrics(0).getAdvance());

        FontHandling.FontAtlas.addGlyph(glyph);
        return glyph;
    }
}
