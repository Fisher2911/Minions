package io.github.fisher2911.minionsplugin.gui;

import dev.triumphteam.gui.guis.Gui;
import dev.triumphteam.gui.guis.GuiItem;
import net.kyori.adventure.text.Component;

import java.util.List;
import java.util.Map;

public class SimpleMinionGui extends BaseMinionGui<Gui> {

    public SimpleMinionGui(final String title,
                           final int rows,
                           final List<GuiItem> borderItemStacks,
                           final Map<Integer, GuiItem> itemStackSlots) {
        super(title, rows, borderItemStacks, itemStackSlots);
    }

    @Override
    public Gui create() {
        final Gui gui = Gui.gui().
                title(Component.text(this.title)).
                rows(this.rows).
                create();

        if (!this.borderItemStacks.isEmpty()) {
            gui.getFiller().fillBorder(this.borderItemStacks);
        }

        for (final Map.Entry<Integer, GuiItem> entry : this.itemStackSlots.entrySet()) {
            gui.setItem(entry.getKey(), entry.getValue());
        }

        gui.setDefaultClickAction(event -> event.setCancelled(true));

        return gui;
    }
}
