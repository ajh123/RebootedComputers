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

import net.minecraft.resources.ResourceLocation;

import java.awt.*;
import java.io.IOException;
import java.io.InputStream;

public class FontHandling {
    public static final FontAtlas FontAtlas = new FontAtlas(1024, 1024, "font_atlas");
    // Regular
    public static final Font RegularFont = loadFont("/assets/oc2r/fonts/monocraft-r.ttf", 32f);
    public static final UnicodeFontRenderer regularFontRenderer = new UnicodeFontRenderer(RegularFont, false);
    // Bold
    public static final Font BoldFont = loadFont("/assets/oc2r/fonts/monocraft-b.ttf", 32f);
    public static final UnicodeFontRenderer boldFontRenderer = new UnicodeFontRenderer(BoldFont, false);
    // Italic
    public static final Font ItalicFont = loadFont("/assets/oc2r/fonts/monocraft-i.ttf", 32f);
    public static final UnicodeFontRenderer italicFontRenderer = new UnicodeFontRenderer(ItalicFont, true);
    // Bold
    public static final Font BoldItalicFont = loadFont("/assets/oc2r/fonts/monocraft-bi.ttf", 32f);
    public static final UnicodeFontRenderer boldItalicFontRenderer = new UnicodeFontRenderer(BoldItalicFont, true);

    public static Glyph getGlyph(int character, FontStyle style) {
        return switch (style) {
            case REGULAR -> regularFontRenderer.getGlyph(character);
            case ITALIC -> italicFontRenderer.getGlyph(character);
            case BOLD -> boldFontRenderer.getGlyph(character);
            case BOLD_ITALIC -> boldItalicFontRenderer.getGlyph(character);
        };
    }

    public static ResourceLocation getAtlas() {
        return FontAtlas.getTextureId();
    }

    public static Font loadFont(String path, float size) {
        try (InputStream is = FontHandling.class.getResourceAsStream(path)) {
            if (is == null) {
                return new Font("Arial", Font.PLAIN, (int) size);
            }
            return Font.createFont(Font.TRUETYPE_FONT, is).deriveFont(size);
        } catch (FontFormatException | IOException e) {
            return new Font("Arial", Font.PLAIN, (int) size); // fallback
        }
    }

    public enum FontStyle {
        REGULAR,
        ITALIC,
        BOLD,
        BOLD_ITALIC
    }
}
