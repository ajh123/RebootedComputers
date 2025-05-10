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

import com.mojang.blaze3d.platform.NativeImage;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.resources.ResourceLocation;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

@Environment(EnvType.CLIENT)
public class FontAtlas {
    private final static int PADDING = 2; // Padding between glyphs

    private final ResourceLocation resources;
    private int atlasWidth;
    private int atlasHeight;
    public NativeImage atlasImage;  // The current texture
    private final DynamicTexture dynamicTexture;
    private final List<Glyph> glyphs;

    private int currentX = 0;  // X coordinate to place next glyph
    private int currentY = 0;  // Y coordinate to place next glyph

    public FontAtlas(int initialWidth, int initialHeight, String fontAtlasName) {
        this.atlasWidth = initialWidth;
        this.atlasHeight = initialHeight;
        this.atlasImage = new NativeImage(atlasWidth, atlasHeight, false);
        this.dynamicTexture = new DynamicTexture(atlasImage);
        this.resources = ResourceLocation.fromNamespaceAndPath("oc2r", fontAtlasName);
        Minecraft.getInstance().getTextureManager().register(resources, dynamicTexture);
        this.glyphs = new ArrayList<>();

        for (int x = 0; x < atlasWidth; x++) {
            for (int y = 0; y < atlasHeight; y++) {
                this.atlasImage.setPixelRGBA(x, y, new Color(0, 0, 0, 0).getRGB());
            }
        }

        BufferedImage f = new BufferedImage(16, 32, BufferedImage.TYPE_INT_ARGB);

        Graphics2D g = f.createGraphics();
        g.setColor(Color.WHITE);
        g.draw(new Rectangle(0, 0, 16, 32));

        g.dispose();

        Glyph square = new Glyph(f, 16, 32, 0);

        addGlyph(square);

        glyphs.add(square);
    }

    // Method to add a glyph to the atlas, resizing it if needed
    public void addGlyph(Glyph glyph) {
        if (currentX + glyph.image.getWidth() > atlasWidth) {
            currentX = 0;
            currentY += glyph.image.getHeight() + PADDING;
        }

        // Check if there's enough space in the current atlas
        if (currentY + glyph.image.getHeight() > atlasHeight) {
            resizeAtlas();  // Resize the atlas if there isn't enough space
        }

        // Copy the glyph into the atlas at the correct position
        for (int y = 0; y < glyph.image.getHeight(); y++) {
            for (int x = 0; x < glyph.image.getWidth(); x++) {
                int color = glyph.image.getRGB(x, y);
                atlasImage.setPixelRGBA(currentX + x, currentY + y, color);
            }
        }

        // Calculate the UV coordinates for this glyph
        float uStart = (float) currentX * (1f / atlasWidth);
        float vStart = (float) currentY * (1f / atlasHeight);
        float uEnd = (float) (currentX + glyph.image.getWidth() + 1) * (1f / atlasWidth);
        float vEnd = (float) (currentY + glyph.image.getHeight() + 1) * (1f / atlasHeight);

        glyph.setUV(uStart, vStart, uEnd, vEnd);

        glyphs.add(glyph);

        // Update the position for the next glyph
        currentX += glyph.image.getWidth() + PADDING;
        updateTexture();
    }

    private void resizeAtlas() {
        int newWidth = atlasWidth * 2;
        int newHeight = atlasHeight * 2;

        NativeImage newAtlasImage = new NativeImage(newWidth, newHeight, false);

        for (int y = 0; y < atlasHeight; y++) {
            for (int x = 0; x < atlasWidth; x++) {
                int color = atlasImage.getPixelRGBA(x, y);
                newAtlasImage.setPixelRGBA(x, y, color);
            }
        }

        for (Glyph glyph : glyphs) {
            glyph.setUV(glyph.uStart/2f, glyph.vStart/2f, glyph.uEnd/2f, glyph.vEnd/2f);
        }

        this.atlasWidth = newWidth;
        this.atlasHeight = newHeight;
        this.atlasImage = newAtlasImage;

        this.dynamicTexture.setPixels(atlasImage);
        updateTexture();
    }

    public ResourceLocation getTextureId() {
        return this.resources;
    }

    public void updateTexture() {
        dynamicTexture.upload();
    }
}
