package io.github.fisher2911.minionsplugin.gui.item;

import dev.triumphteam.gui.guis.GuiItem;
import org.jetbrains.annotations.Nullable;

public class TypeItem {

    private @Nullable final String type;
    private final GuiItem guiItem;

    public TypeItem(final @Nullable String type, final GuiItem guiItem) {
        this.type = type;
        this.guiItem = guiItem;
    }

    public @Nullable String getType() {
        return this.type;
    }

    public GuiItem getGuiItem() {
        return this.guiItem;
    }
}
