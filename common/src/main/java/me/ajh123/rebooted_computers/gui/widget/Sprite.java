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

package me.ajh123.rebooted_computers.gui.widget;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.util.Mth;

public final class Sprite {
    public final Texture texture;
    public final int width, height;
    public final int u0, v0;

    ///////////////////////////////////////////////////////////////////

    public Sprite(final Texture texture) {
        this.texture = texture;
        this.width = texture.width;
        this.height = texture.height;
        this.u0 = 0;
        this.v0 = 0;
    }

    public Sprite(final Texture texture, final int width, final int height, final int u0, final int v0) {
        this.texture = texture;
        this.width = width;
        this.height = height;
        this.u0 = u0;
        this.v0 = v0;
    }

    ///////////////////////////////////////////////////////////////////

    public void draw(final GuiGraphics graphics, final int x, final int y) {
        draw(graphics, x, y, 0, 0);
    }

    public void draw(final GuiGraphics graphics, final int x, final int y, final int uOffset, final int vOffset) {
        graphics.blit(texture.location, x, y, u0 + uOffset, v0 + vOffset, width, height, texture.width, texture.height);
    }

    public void drawFillY(final GuiGraphics graphics, final int x, final int y, final float value) {
        final int h = (int) (this.height * Mth.clamp(value, 0, 1));
        graphics.blit(texture.location, x, y + (height - h), u0, v0 + (height - h), width, h, texture.width, texture.height);
    }
}