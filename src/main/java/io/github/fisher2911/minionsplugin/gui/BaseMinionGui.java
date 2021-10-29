package io.github.fisher2911.minionsplugin.gui;

import dev.triumphteam.gui.guis.BaseGui;
import dev.triumphteam.gui.guis.GuiItem;

import java.util.List;
import java.util.Map;

public abstract class BaseMinionGui<G extends BaseGui> {

    public static final String MAIN = "main";
    public static final String UPGRADES = "upgrades";
    public static final String COSMETICS = "cosmetics";

    protected final String title;
    protected final int rows;
    protected final List<GuiItem> borderItemStacks;
    protected final Map<Integer, GuiItem> itemStackSlots;

    public BaseMinionGui(final String title,
                         final int rows,
                         final List<GuiItem> borderItemStacks,
                         final Map<Integer, GuiItem> itemStackSlots) {
        this.title = title;
        this.rows = rows;
        this.borderItemStacks = borderItemStacks;
        this.itemStackSlots = itemStackSlots;
    }

    public abstract G create();
}
