package com.octoarms.client.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import com.octoarms.menu.AssemblerMenu;
import com.octoarms.menu.ChargerMenu;
import com.octoarms.menu.PressMenu;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;

/**
 * Procedurally drawn machine GUI (no bitmap needed): dark steel panel,
 * burn bar, progress bar and PU energy column.
 */
public class MachineScreen<M extends AbstractContainerMenu> extends AbstractContainerScreen<M> {

    public enum Kind {PRESS, CHARGER, ASSEMBLER}

    private final Kind kind;

    public static MachineScreen<PressMenu> press(PressMenu menu, Inventory inv, Component title) {
        return new MachineScreen<>(menu, inv, title, Kind.PRESS, PressMenu.IMAGE_WIDTH, PressMenu.IMAGE_HEIGHT);
    }

    public static MachineScreen<ChargerMenu> charger(ChargerMenu menu, Inventory inv, Component title) {
        return new MachineScreen<>(menu, inv, title, Kind.CHARGER, ChargerMenu.IMAGE_WIDTH, ChargerMenu.IMAGE_HEIGHT);
    }

    public static MachineScreen<AssemblerMenu> assembler(AssemblerMenu menu, Inventory inv, Component title) {
        return new MachineScreen<>(menu, inv, title, Kind.ASSEMBLER, AssemblerMenu.IMAGE_WIDTH, AssemblerMenu.IMAGE_HEIGHT);
    }

    protected MachineScreen(M menu, Inventory inv, Component title, Kind kind, int width, int height) {
        super(menu, inv, title);
        this.kind = kind;
        this.imageWidth = width;
        this.imageHeight = height;
    }

    @Override
    public void renderBg(GuiGraphics graphics, float partialTick, int mouseX, int mouseY) {
        RenderSystem.disableDepthTest();
        int x = leftPos;
        int y = topPos;

        // panel
        graphics.fill(x, y, x + imageWidth, y + imageHeight, 0xFF2A3038);
        graphics.fill(x + 1, y + 1, x + imageWidth - 1, y + imageHeight - 1, 0xFF3B434D);
        graphics.fill(x + 3, y + 3, x + imageWidth - 3, y + 14, 0xFF262C33);
        graphics.fill(x + 3, y + imageHeight - 96, x + imageWidth - 3, y + imageHeight - 3, 0xFF313841);

        int lit = menu.getData(0);
        int litTotal = Math.max(1, menu.getData(1));
        int progress = menu.getData(2);
        int progressTotal = Math.max(1, menu.getData(3));
        int energy = menu.getData(4);
        int energyCap = Math.max(1, menu.getData(5));

        switch (kind) {
            case PRESS -> {
                drawBurn(graphics, x + 26, y + 71, lit, litTotal);
                drawProgress(graphics, x + 76, y + 44, progress, progressTotal);
            }
            case CHARGER -> {
                drawBurn(graphics, x + 26, y + 53, lit, litTotal);
                drawEnergy(graphics, x + 60, y + 20, 60, energy, energyCap);
                drawProgress(graphics, x + 104, y + 34, progress, 100);
            }
            case ASSEMBLER -> {
                drawBurn(graphics, x + 98, y + 80, lit, litTotal);
                drawEnergy(graphics, x + 140, y + 17, 90, energy, energyCap);
                drawProgress(graphics, x + 104, y + 44, progress, progressTotal);
            }
        }
        RenderSystem.enableDepthTest();
    }

    private void drawBurn(GuiGraphics graphics, int x, int y, int lit, int litTotal) {
        graphics.fill(x, y, x + 14, y + 14, 0xFF20262C);
        int h = (int) (14.0F * lit / litTotal);
        graphics.fill(x + 2, y + 14 - h, x + 12, y + 14, 0xFFE8842C);
        if (h > 3) {
            graphics.fill(x + 4, y + 14 - h + 1, x + 10, y + 14 - 1, 0xFFFFC24A);
        }
    }

    private void drawProgress(GuiGraphics graphics, int x, int y, int progress, int total) {
        graphics.fill(x, y + 4, x + 26, y + 10, 0xFF20262C);
        int w = (int) (26.0F * Math.min(1.0F, progress / (float) total));
        graphics.fill(x, y + 5, x + w, y + 9, 0xFF7FD4FF);
        graphics.fill(x, y + 5, x + w, y + 6, 0xFFD9F4FF);
    }

    private void drawEnergy(GuiGraphics graphics, int x, int y, int height, int energy, int cap) {
        graphics.fill(x, y, x + 10, y + height, 0xFF20262C);
        int h = (int) ((height - 2) * Math.min(1.0F, energy / (float) cap));
        graphics.fill(x + 1, y + height - 1 - h, x + 9, y + height - 1, 0xFF46E0B4);
        graphics.fill(x + 1, y + height - 1 - h, x + 9, y + height - 2 - h, 0xFFDFFFFF);
        graphics.drawString(font, "PU", x - 2, y - 9, 0xFF9FE8D2, false);
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        renderBackground(graphics);
        super.render(graphics, mouseX, mouseY, partialTick);
        renderTooltip(graphics, mouseX, mouseY);
    }

    @Override
    protected void renderLabels(GuiGraphics graphics, int mouseX, int mouseY) {
        graphics.drawString(font, title, 8, 6, 0xFFD8E6F2, false);
        graphics.drawString(font, playerInventoryTitle, 8, imageHeight - 94, 0xFF9AA7B4, false);
    }
}
