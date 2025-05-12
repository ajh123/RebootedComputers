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

package me.ajh123.rebooted_computers.gui.screens;

import com.mojang.blaze3d.platform.InputConstants;
import me.ajh123.rebooted_computers.gui.Sprites;
import me.ajh123.rebooted_computers.gui.terminal.MachineTerminalWidget;
import me.ajh123.rebooted_computers.network.ModPackets;
import me.ajh123.rebooted_computers.vm.terminal.Terminal;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.StreamCodec;

import java.nio.ByteBuffer;

public class ComputerTerminalScreen extends AbstractScreen<ComputerTerminalScreen.ComputerTerminalScreenState> {
    private static final int CONTROLS_TOP = 8;
    private static final int ENERGY_TOP = CONTROLS_TOP + Sprites.SIDEBAR_3.height + 4;
    public static final StreamCodec<FriendlyByteBuf, ComputerTerminalScreenState> STREAM_CODEC = StreamCodec.of(
            (buf, state) -> {
                buf.writeInt(state.isVmRunning ? 1 : 0);
                ModPackets.writeByteBuffer(buf, state.terminalOutput);
            },
            buf -> {
                int isVmRunning = buf.readInt();
                boolean vmRunning = isVmRunning == 1;
                ByteBuffer terminalOutput = ModPackets.readByteBuffer(buf);
                terminalOutput.flip();
                return new ComputerTerminalScreenState(vmRunning, terminalOutput);
            }
    );

    private ComputerTerminalScreenState state;
    private boolean mouseClicked;

    private final String vmID;
    private final Terminal terminal;
    private final MachineTerminalWidget terminalWidget;
    private boolean shouldCaptureInput = false;

    public ComputerTerminalScreen(String vmID) {
        super(Component.literal(""));
        this.vmID = vmID;
        this.terminal = new Terminal();
        this.terminalWidget = new MachineTerminalWidget(this, terminal);
        this.state = new ComputerTerminalScreenState(false, null);
    }

    @Override
    public boolean charTyped(final char ch, final int modifiers) {
        return terminalWidget.charTyped(ch, modifiers) ||
                super.charTyped(ch, modifiers);
    }

    @Override
    public boolean mouseClicked(final double x, final double y, final int button) {
        mouseClicked = true;
        if (!terminalWidget.mouseClicked(x,y,button)) {
            return super.mouseClicked(x, y, button);
        }
        return true;
    }

    @Override
    public void mouseMoved(double x, double y) {
        terminalWidget.mouseMoved(x, y);
    }

    @Override
    public boolean mouseScrolled(double d, double e, double f, double g) {
        return terminalWidget.mouseScrolled(g);
    }

    @Override
    public boolean mouseReleased(final double x, final double y, final int button) {
        if (!mouseClicked) return super.mouseReleased(x, y, button);
        if (!terminalWidget.mouseReleased(x,y,button)) {
            return super.mouseReleased(x, y, button);
        }
        return true;
    }

    @Override
    public boolean keyPressed(final int keyCode, final int scanCode, final int modifiers) {
        if (terminalWidget.keyPressed(keyCode, scanCode, modifiers)) {
            return true;
        }

        // Don't close with inventory binding since we usually want to use that as terminal input
        // even without input capture enabled.
        final InputConstants.Key input = InputConstants.getKey(keyCode, scanCode);
        if (minecraft.options.keyInventory.matches(input.getValue(), input.getValue())) {
            return true;
        }

        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public void init() {
        super.init();
        terminalWidget.init();
    }

    @Override
    public void onClose() {
        super.onClose();
        terminalWidget.onClose();
    }

    @Override
    protected void renderFg(GuiGraphics graphics, float partialTicks, int mouseX, int mouseY) {
        terminal.clientTick();
        terminalWidget.tick();

        int leftPos = getRectangle().left();
        int topPos = getRectangle().top();

        Sprites.SIDEBAR_3.draw(graphics, leftPos - Sprites.SIDEBAR_3.width, topPos + CONTROLS_TOP);

        if (shouldRenderEnergyBar()) {
            final int x = leftPos - Sprites.SIDEBAR_2.width;
            final int y = topPos + ENERGY_TOP;
            Sprites.SIDEBAR_2.draw(graphics, x, y);
            Sprites.ENERGY_BASE.draw(graphics, x + 4, y + 4);
        }

        terminalWidget.renderBackground(graphics, mouseX, mouseY);

        if (shouldRenderEnergyBar()) {
            final int x = leftPos - Sprites.SIDEBAR_2.width + 4;
            final int y = topPos + ENERGY_TOP + 4;
            Sprites.ENERGY_BAR.drawFillY(graphics, x, y, 100 / (float) 100);
        }

        terminalWidget.render(graphics, null);
    }

    public Minecraft getMinecraft() {
        return minecraft;
    }

    public Terminal getTerminal() {
        return terminal;
    }

    public boolean shouldCaptureInput() {
        return shouldCaptureInput;
    }

    public void setShouldCaptureInput(boolean shouldCaptureInput) {
        this.shouldCaptureInput = shouldCaptureInput;
    }

    public void sendTerminalInputToServer(ByteBuffer input) {
        terminal.putOutput(input);
    }

    private boolean shouldRenderEnergyBar() {
        return true;
    }

    public ComputerTerminalScreenState getState() {
        return state;
    }

    @Override
    public StreamCodec<FriendlyByteBuf, ComputerTerminalScreenState> getStateCodec() {
        return STREAM_CODEC;
    }

    @Override
    public void read(ComputerTerminalScreenState state) {
        this.state = state;
        this.terminal.putOutput(state.terminalOutput);
    }

    public record ComputerTerminalScreenState(
            boolean isVmRunning,
            ByteBuffer terminalOutput
    ) {}
}
