package io.github.fisher2911.minionsplugin.gui;

import org.bukkit.event.inventory.ClickType;

import java.util.EnumSet;
import java.util.Set;

public class ClickAction {

    private static final ClickType[] CLICK_TYPES = ClickType.values();

    private final Set<ClickType> clickTypes;
    private final Action action;

    public ClickAction(final Set<ClickType> clickTypes, final Action action) {
        this.clickTypes = clickTypes;
        this.action = action;
    }

    public static ClickAction none() {
        return new ClickAction(EnumSet.noneOf(ClickType.class), (gui, slot) -> {});
    }

    public void act(
            final BaseMinionGui<?> baseMinionGui,
            final int slotClicked,
            final ClickType clickType) {
        if (this.clickTypes.contains(clickType)) {
            this.action.act(baseMinionGui, slotClicked);
        }
    }

    public Set<ClickType> getClickTypes() {
        return this.clickTypes;
    }

    public Action getAction() {
        return this.action;
    }

    @FunctionalInterface
    public interface Action {

        void act(final BaseMinionGui<?> baseMinionGui, final int slotClicked);

    }

    @Override
    public String toString() {
        return "ClickAction{" +
                "clickTypes=" + this.clickTypes +
                ", action=" + this.action +
                '}';
    }
}
