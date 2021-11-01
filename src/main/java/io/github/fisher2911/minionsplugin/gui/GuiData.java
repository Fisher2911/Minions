package io.github.fisher2911.minionsplugin.gui;

import dev.triumphteam.gui.guis.GuiItem;

import java.util.List;
import java.util.Map;

public class GuiData {

    private final String title;
    private final int rows;
    private final List<GuiItem> borderItemStacks;
    private final Map<Integer, GuiItem> itemStackSlots;
    private final Map<Integer, ClickAction> clickActionSlots;

    public GuiData(
            final String title,
            final int rows,
            final List<GuiItem> borderItemStacks,
            final Map<Integer, GuiItem> itemStackSlots,
            final Map<Integer, ClickAction> clickActionSlots) {
        this.title = title;
        this.rows = rows;
        this.borderItemStacks = borderItemStacks;
        this.itemStackSlots = itemStackSlots;
        this.clickActionSlots = clickActionSlots;
    }

    public String getTitle() {
        return this.title;
    }

    public int getRows() {
        return this.rows;
    }

    public List<GuiItem> getBorderItemStacks() {
        return this.borderItemStacks;
    }

    public Map<Integer, GuiItem> getItemStackSlots() {
        return this.itemStackSlots;
    }

    public Map<Integer, ClickAction> getClickActionSlots() {
        return this.clickActionSlots;
    }

    @Override
    public String toString() {
        return "GuiData{" +
                "title='" + this.title + '\'' +
                ", rows=" + this.rows +
                ", borderItemStacks=" + this.borderItemStacks +
                ", itemStackSlots=" + this.itemStackSlots +
                ", clickActionSlots=" + this.clickActionSlots +
                '}';
    }
}
