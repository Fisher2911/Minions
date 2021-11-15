package io.github.fisher2911.minionsplugin.gui.action;

import io.github.fisher2911.minionsplugin.gui.BaseMinionGui;
import org.bukkit.event.inventory.ClickType;

import java.util.ArrayList;
import java.util.List;

public class ClickActions {

    private final List<ClickAction> clickActions;

    public ClickActions() {
        this.clickActions = new ArrayList<>();
    }

    public ClickActions(final List<ClickAction> clickActions) {
        this.clickActions = clickActions;
    }

    public List<ClickAction> getClickActions() {
        return this.clickActions;
    }

    public void addClickAction(final ClickAction clickAction) {
        this.clickActions.add(clickAction);
    }

    public void addAll(final ClickActions clickActions) {
        this.clickActions.addAll(clickActions.getClickActions());
    }

    public void runAll(
            final BaseMinionGui<?> gui,
            final int clickedSlot,
            final ClickType clickType) {
        this.clickActions.forEach(clickAction ->
                clickAction.act(gui, clickedSlot, clickType));
    }

    public static ClickActions none() {
        return new ClickActions();
    }
}
