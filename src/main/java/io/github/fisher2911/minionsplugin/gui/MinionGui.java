package io.github.fisher2911.minionsplugin.gui;

import dev.triumphteam.gui.guis.GuiItem;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

public class MinionGui {

    private final String guiName;
    private final int size;
    public final ItemStack[] borderItemStacks;
    private final Map<Integer, GuiItem> itemStackSlots;

    public MinionGui(final String guiName,
                     final int size,
                     final ItemStack[] borderItemStacks,
                     final Map<Integer, GuiItem> itemStackSlots) {
        this.guiName = guiName;
        this.size = size;
        this.borderItemStacks = borderItemStacks;
        this.itemStackSlots = itemStackSlots;
    }
}
