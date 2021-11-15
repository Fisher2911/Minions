package io.github.fisher2911.minionsplugin.gui.item;

import io.github.fisher2911.minionsplugin.gui.action.ClickActions;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.List;

public class FillItem {

    @Nullable
    private final String name;

    @Nullable
    private final List<String> lore;

    private final Collection<ClickActions> clickActions;

    public FillItem(@Nullable final String name,
                    @Nullable final List<String> lore,
                    final Collection<ClickActions> clickActions) {
        this.name = name;
        this.lore = lore;
        this.clickActions = clickActions;
    }

    @Nullable
    public String getName() {
        return this.name;
    }

    @Nullable
    public List<String> getLore() {
        return this.lore;
    }

    public Collection<ClickActions> getClickActions() {
        return this.clickActions;
    }
}
