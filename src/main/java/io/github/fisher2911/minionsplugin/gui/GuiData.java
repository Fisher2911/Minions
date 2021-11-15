package io.github.fisher2911.minionsplugin.gui;

import com.google.common.collect.Multimap;
import dev.triumphteam.gui.guis.GuiItem;
import io.github.fisher2911.minionsplugin.gui.action.ClickActions;
import io.github.fisher2911.minionsplugin.gui.action.FillInstructions;
import io.github.fisher2911.minionsplugin.gui.item.TypeItem;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;

public class GuiData {

    private final String title;
    private final int rows;
    private final List<GuiItem> borderItemStacks;
    private final Map<Integer, TypeItem> itemStackSlots;
    private final Multimap<Integer, ClickActions> clickActionSlots;
    private final Map<String, FillInstructions> fillInstructions;

    public GuiData(
            final String title,
            final int rows,
            final List<GuiItem> borderItemStacks,
            final Map<Integer, TypeItem> itemStackSlots,
            final Multimap<Integer, ClickActions> clickActionSlots,
            final Map<String, FillInstructions> fillInstructions) {
        this.title = title;
        this.rows = rows;
        this.borderItemStacks = borderItemStacks;
        this.itemStackSlots = itemStackSlots;
        this.clickActionSlots = clickActionSlots;
        this.fillInstructions = fillInstructions;
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

    public Map<Integer, TypeItem> getItemStackSlots() {
        return this.itemStackSlots;
    }

    public Multimap<Integer, ClickActions> getClickActionSlots() {
        return this.clickActionSlots;
    }

    public @Nullable TypeItem getTypeItem(final int slot) {
        return this.itemStackSlots.get(slot);
    }

    public Map<String, FillInstructions> getFillInstructions() {
        return this.fillInstructions;
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
