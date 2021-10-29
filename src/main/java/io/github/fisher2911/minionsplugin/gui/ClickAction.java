package io.github.fisher2911.minionsplugin.gui;

import org.bukkit.event.inventory.ClickType;

public class ClickAction {

    private final ClickType clickType;
    private final Action action;

    public ClickAction(final ClickType clickType, final Action action) {
        this.clickType = clickType;
        this.action = action;
    }

    @FunctionalInterface
    public interface Action {

        void act();

    }
}
