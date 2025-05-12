package me.ajh123.rebooted_computers.gui.screens;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;

public abstract class AbstractScreen<S> extends Screen implements SynchronisedScreen<S> {
    protected AbstractScreen(Component title) {
        super(title);
    }

    public boolean isMouseOver(final int mouseX, final int mouseY, final int x, final int y, final int width, final int height) {
        final int localMouseX = mouseX - getRectangle().left();
        final int localMouseY = mouseY - getRectangle().top();
        return localMouseX >= x &&
                localMouseX < x + width &&
                localMouseY >= y &&
                localMouseY < y + height;
    }

    @Override
    public void render(final @NotNull GuiGraphics graphics, final int mouseX, final int mouseY, final float partialTicks) {
        renderBackground(graphics, mouseX, mouseY, partialTicks);

        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1, 1, 1, 1);

        super.render(graphics, mouseX, mouseY, partialTicks);

        renderFg(graphics, partialTicks, mouseX, mouseY);
    }

    protected abstract void renderFg(final GuiGraphics graphics, final float partialTicks, final int mouseX, final int mouseY);
}
