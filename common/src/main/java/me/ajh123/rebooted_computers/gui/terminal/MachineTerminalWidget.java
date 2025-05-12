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

package me.ajh123.rebooted_computers.gui.terminal;

import com.mojang.blaze3d.vertex.*;
import me.ajh123.rebooted_computers.gui.Sprites;
import me.ajh123.rebooted_computers.gui.screens.ComputerTerminalScreen;
import me.ajh123.rebooted_computers.vm.terminal.Terminal;
import me.ajh123.rebooted_computers.vm.terminal.modes.MouseMode;
import me.ajh123.rebooted_computers.vm.terminal.modes.PrivateMode;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.MultiBufferSource;
import org.joml.Matrix4f;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.network.chat.Component;
import org.joml.Vector2i;
import org.lwjgl.glfw.GLFW;

import jakarta.annotation.Nullable;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

@Environment(EnvType.CLIENT)
public final class MachineTerminalWidget {
    private static final int TERMINAL_WIDTH = Terminal.WIDTH * Terminal.CHAR_WIDTH / 2;
    private static final int TERMINAL_HEIGHT = Terminal.HEIGHT * Terminal.CHAR_HEIGHT / 2;

    private static final int MARGIN_SIZE = 8;
    private static final int TERMINAL_X = MARGIN_SIZE;
    private static final int TERMINAL_Y = MARGIN_SIZE;

    public static final int WIDTH = Sprites.TERMINAL_SCREEN.width;
    public static final int HEIGHT = Sprites.TERMINAL_SCREEN.height;

    ///////////////////////////////////////////////////////////////////

    private final ComputerTerminalScreen parent;
    private final Terminal terminal;
    private int leftPos, topPos;
    private boolean isMouseOverTerminal;
    private Terminal.RendererView rendererView;
    private boolean isOver;

    ///////////////////////////////////////////////////////////////////

    public MachineTerminalWidget(final ComputerTerminalScreen parent, final Terminal terminal) {
        this.parent = parent;
        this.terminal = terminal;
    }

    public void renderBackground(final GuiGraphics graphics, final int mouseX, final int mouseY) {
        isMouseOverTerminal = isMouseOverTerminal(mouseX, mouseY);

        Sprites.TERMINAL_SCREEN.draw(graphics, leftPos, topPos);

        if (shouldCaptureInput()) {
            Sprites.TERMINAL_FOCUSED.draw(graphics, leftPos, topPos);
        }
    }

    public void render(final GuiGraphics graphics, @Nullable final Component error) {
        if (parent.getState().isVmRunning()) {
            final PoseStack terminalStack = new PoseStack();
            terminalStack.translate(leftPos + TERMINAL_X, topPos + TERMINAL_Y, 0);
            terminalStack.scale(TERMINAL_WIDTH / (float) terminal.getWidth(), TERMINAL_HEIGHT / (float) terminal.getHeight(), 1f);

            if (rendererView == null) {
                rendererView = terminal.getRenderer();
            }

            //final Matrix4f projectionMatrix = orthographic(0, parent.width, 0, parent.height, -10, 10f);
            final Matrix4f projectionMatrix = (new Matrix4f()).setOrtho(0, parent.width, parent.height, 0, -10f, 10f);
            rendererView.render(terminalStack, projectionMatrix);
        } else {
            final Font font = getClient().font;
            if (error != null) {
                final int textWidth = font.width(error);
                final int textOffsetX = (TERMINAL_WIDTH - textWidth) / 2;
                final int textOffsetY = (TERMINAL_HEIGHT - font.lineHeight) / 2;
                drawShadow(
                        font,
                        graphics,
                        error,
                        leftPos + TERMINAL_X + textOffsetX,
                        topPos + TERMINAL_Y + textOffsetY
                );
            }
        }
    }

    private void drawShadow(Font font, GuiGraphics graphics, Component text, float x, float y) {
        var batch = MultiBufferSource.immediate(new ByteBufferBuilder(786432));
        font.drawInBatch(text, x, y, 15610658, true, graphics.pose().last().pose(), batch, Font.DisplayMode.NORMAL, 0, 15728880);
        batch.endBatch();
    }

    public void tick() {
        final ByteBuffer input = terminal.getInput();
        if (input != null) {
            parent.sendTerminalInputToServer(input);
        }
    }

    public boolean mouseScrolled(double dir) {
        if (terminal.currentPrivateModeState.isAltBufferEnabled()) return false;
        if (dir < 0) {
            terminal.incrementLastLineToDisplay(true);
        } else {
            terminal.decrementLastLineToDisplay();
        }
        return true;
    }

    public void mouseMoved(double x, double y) {
        if (isMouseOverTerminal((int)x, (int)y)) {
            if (!isOver && terminal.currentPrivateModeState.FOCUS_IN_FOCUS_OUT) {
                isOver = true;
                terminal.putInput("\033[I");
            }
        } else {
            if(isOver && terminal.currentPrivateModeState.FOCUS_IN_FOCUS_OUT) {
                terminal.putInput("\033[O");
            }
        }
    }

    public boolean mouseClicked(double x, double y, int button) {
        MouseMode currentMouseMode = terminal.currentPrivateModeState.getMouseMode();
        if (currentMouseMode.isMouseDisabled()) return false;
        Vector2i position = getMousePosition(x, y);
        boolean overTerminal = isMouseOverTerminal((int)x, (int)y);
        if (overTerminal && shouldCaptureInput()) {
            switch(currentMouseMode.PrimaryMode) {
                case PrivateMode.X11MM, PrivateMode.CELL_MOTION_MOUSE -> {
                    if (currentMouseMode.isSecondaryModeEnabled(PrivateMode.SGR_MOUSE)) {
                        terminal.putInput("\033[<" + button + ";" + position.x + ";" + position.y + "M");
                        return true;
                    }
                    else if (currentMouseMode.isSecondaryModeEnabled(PrivateMode.UTF8_MOUSE))
                    {
                        byte[] csiMBytes = "\033[M".getBytes(StandardCharsets.UTF_8);
                        byte[] buttonBytes = utf8(button + 32);
                        byte[] colBytes = utf8(position.x + 32);
                        byte[] rowBytes = utf8(position.y + 32);
                        byte[] finalBytes = new byte[csiMBytes.length + buttonBytes.length + colBytes.length + rowBytes.length];

                        System.arraycopy(csiMBytes, 0, finalBytes, 0, csiMBytes.length);
                        System.arraycopy(buttonBytes, 0, finalBytes, csiMBytes.length, buttonBytes.length);
                        System.arraycopy(colBytes, 0, finalBytes, csiMBytes.length + buttonBytes.length, colBytes.length);
                        System.arraycopy(rowBytes, 0, finalBytes, csiMBytes.length + buttonBytes.length + colBytes.length, rowBytes.length);

                        terminal.putInput(ByteBuffer.wrap(finalBytes));
                        return true;
                    }
                    else if (currentMouseMode.isSecondaryModeEnabled(PrivateMode.URXVT_MOUSE))
                    {
                        terminal.putInput("\033[" + (button + 32) + ";" + position.x + ";" + position.y + "M");
                    }
                    else
                    {
                        terminal.putInput('\033');
                        terminal.putInput('[');
                        terminal.putInput('M');
                        terminal.putInput((byte) (button + 32));
                        terminal.putInput((byte) (position.x + 32));
                        terminal.putInput((byte) (position.y + 32));
                        return true;
                    }
                }
                default -> System.out.println("ERR: Unsupported primary mode");
            }
        }
        return false;
    }

    public boolean mouseReleased(double x, double y, int button) {
        MouseMode currentMouseMode = terminal.currentPrivateModeState.getMouseMode();
        if (currentMouseMode.isMouseDisabled()) return false;
        Vector2i position = getMousePosition(x, y);
        boolean overTerminal = isMouseOverTerminal((int)x, (int)y);
        if (overTerminal && shouldCaptureInput()) {
            switch(currentMouseMode.PrimaryMode) {
                case PrivateMode.X11MM, PrivateMode.CELL_MOTION_MOUSE -> {
                    if (currentMouseMode.isSecondaryModeEnabled(PrivateMode.SGR_MOUSE)) {
                        terminal.putInput("\033[<" + button + ";" + position.x + ";" + position.y + "m");
                        return true;
                    }
                    else if (currentMouseMode.isSecondaryModeEnabled(PrivateMode.UTF8_MOUSE))
                    {
                        byte[] csiMBytes = "\033[M".getBytes(StandardCharsets.UTF_8);
                        byte[] buttonBytes = utf8(35);
                        byte[] colBytes = utf8(position.x + 32);
                        byte[] rowBytes = utf8(position.y + 32);
                        byte[] finalBytes = new byte[csiMBytes.length + buttonBytes.length + colBytes.length + rowBytes.length];

                        System.arraycopy(csiMBytes, 0, finalBytes, 0, csiMBytes.length);
                        System.arraycopy(buttonBytes, 0, finalBytes, csiMBytes.length, buttonBytes.length);
                        System.arraycopy(colBytes, 0, finalBytes, csiMBytes.length + buttonBytes.length, colBytes.length);
                        System.arraycopy(rowBytes, 0, finalBytes, csiMBytes.length + buttonBytes.length + colBytes.length, rowBytes.length);

                        terminal.putInput(ByteBuffer.wrap(finalBytes));
                        return true;
                    }
                    else if (currentMouseMode.isSecondaryModeEnabled(PrivateMode.URXVT_MOUSE))
                    {
                        terminal.putInput("\033[" + 35 + ";" + position.x + ";" + position.y + "M");
                    }
                    else
                    {
                        terminal.putInput('\033');
                        terminal.putInput('[');
                        terminal.putInput('M');
                        terminal.putInput((byte) 35);
                        terminal.putInput((byte) (position.x + 32));
                        terminal.putInput((byte) (position.y + 32));
                        return true;
                    }
                }
                default -> System.out.println("ERR: Unsupported primary mode");
            }
        }
        return false;
    }

    private byte[] utf8(int value) {
        return new String(new int[]{value}, 0, 1).getBytes(StandardCharsets.UTF_8);
    }

    private Vector2i getMousePosition(double x, double y) {
        int tx = TERMINAL_WIDTH / Terminal.WIDTH;
        int ty = TERMINAL_HEIGHT / Terminal.HEIGHT;
        int sx = (int)(((x - leftPos) - MachineTerminalWidget.TERMINAL_X) / tx) + 1;
        int sy = (int)(((y - topPos) - MachineTerminalWidget.TERMINAL_Y) / ty) + 1;

        return new Vector2i(sx, sy);
    }

    public boolean charTyped(final char ch, final int modifier) {
        if (modifier == 0 || modifier == GLFW.GLFW_MOD_SHIFT) {
            terminal.putInput((byte) ch);
        }
        return true;
    }

    @SuppressWarnings("unused")
    public boolean keyPressed(final int keyCode, final int scanCode, final int modifiers) {
        if (!shouldCaptureInput() && keyCode == GLFW.GLFW_KEY_ESCAPE) {
            return false;
        }

        if (keyCode == GLFW.GLFW_KEY_ESCAPE && terminal.currentPrivateModeState.APPLICATION_ESC_MODE) {
            terminal.putInput("\033[0[");
        }

        if ((modifiers & GLFW.GLFW_MOD_CONTROL) != 0 && keyCode == GLFW.GLFW_KEY_V) {
            final String value = getClient().keyboardHandler.getClipboard();
            boolean bracketed = terminal.currentPrivateModeState.SET_BRACKETED_PASTE;
            if (bracketed) terminal.putInput("\033[200~");
            for (final char ch : value.toCharArray()) {
                terminal.putInput((byte) ch);
            }
            if (bracketed) terminal.putInput("\033[201~");
        } else {
            byte[] sequence;
            if (terminal.currentPrivateModeState.DECCKM && (keyCode == GLFW.GLFW_KEY_UP || keyCode == GLFW.GLFW_KEY_DOWN || keyCode == GLFW.GLFW_KEY_LEFT || keyCode == GLFW.GLFW_KEY_RIGHT))
                sequence = TerminalInput.getDECCKMSequence(keyCode, modifiers);
            else sequence = TerminalInput.getSequence(keyCode, modifiers);
            if (sequence != null) {
                for (final byte b : sequence) {
                    terminal.putInput(b);
                }
            }
        }

        return true;
    }

    public void init() {
        this.leftPos = (parent.width - WIDTH) / 2;
        this.topPos = (parent.height - HEIGHT) / 2;
    }

    public void onClose() {
        if (rendererView != null) {
            terminal.releaseRenderer(rendererView);
            rendererView = null;
        }
    }

    ///////////////////////////////////////////////////////////////////

    private Minecraft getClient() {
        return parent.getMinecraft();
    }

    private boolean shouldCaptureInput() {
        return isMouseOverTerminal && parent.shouldCaptureInput() &&
                parent.getState().isVmRunning();
    }

    private boolean isMouseOverTerminal(final int mouseX, final int mouseY) {
        return parent.isMouseOver(mouseX, mouseY,
                MachineTerminalWidget.TERMINAL_X, MachineTerminalWidget.TERMINAL_Y,
                MachineTerminalWidget.TERMINAL_WIDTH, MachineTerminalWidget.TERMINAL_HEIGHT);
    }
}