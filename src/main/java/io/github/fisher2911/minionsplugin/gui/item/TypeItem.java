package io.github.fisher2911.minionsplugin.gui.item;

import dev.triumphteam.gui.guis.GuiItem;
import io.github.fisher2911.fishcore.util.builder.ItemBuilder;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public class TypeItem {

    private @Nullable final String type;
    private @Nullable final String value;
    private final GuiItem guiItem;

    public TypeItem(final @Nullable String type, final @Nullable String value, final GuiItem guiItem) {
        this.type = type;
        this.value = value;
        this.guiItem = guiItem;
    }

    public TypeItem(final @Nullable String type, final @Nullable String value, final ItemStack itemStack) {
        this.type = type;
        this.value = value;
        this.guiItem = new GuiItem(itemStack);
    }

    public @Nullable String getType() {
        return this.type;
    }

    public @Nullable String getValue() {
        return this.value;
    }

    public GuiItem getGuiItem() {
        return this.guiItem;
    }

    public GuiItem getGuiItemWithPlaceholders(final Map<String, String> placeholders) {
        return new GuiItem(
                ItemBuilder.from(this.guiItem.getItemStack().clone()).
                        namePlaceholders(placeholders).
                        lorePlaceholders(placeholders).
                        build()
        );
    }
}
