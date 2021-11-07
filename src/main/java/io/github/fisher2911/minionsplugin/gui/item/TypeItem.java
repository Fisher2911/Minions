package io.github.fisher2911.minionsplugin.gui.item;

import dev.triumphteam.gui.guis.GuiItem;
import io.github.fisher2911.fishcore.util.builder.ItemBuilder;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

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

    public GuiItem getGuiItemWithPlaceholders(final Map<String, String> placeholders) {
        return new GuiItem(
                ItemBuilder.from(this.guiItem.getItemStack()).
                        namePlaceholders(placeholders).
                        lorePlaceholders(placeholders).
                        build()
        );
    }
}
