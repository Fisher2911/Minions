package io.github.fisher2911.minionsplugin.gui;

import dev.triumphteam.gui.guis.Gui;
import dev.triumphteam.gui.guis.GuiItem;
import io.github.fisher2911.minionsplugin.minion.types.BaseMinion;
import io.github.fisher2911.minionsplugin.user.MinionUser;
import net.kyori.adventure.text.Component;

import java.util.List;
import java.util.Map;

public class SimpleMinionGui extends BaseMinionGui<Gui> {

    public SimpleMinionGui(
            final BaseMinion<?> baseMinion,
            final MinionUser guiOwner,
            final GuiData guiData) {
        super(baseMinion, guiOwner, guiData);
    }

    @Override
    public Gui create() {
        final Gui gui = Gui.gui().
                title(Component.text(this.guiData.getTitle())).
                rows(this.guiData.getRows()).
                create();

        final List<GuiItem>  borderItemStacks = this.guiData.getBorderItemStacks();
        if (!borderItemStacks.isEmpty()) {
            gui.getFiller().fillBorder(borderItemStacks);
        }

        for (final Map.Entry<Integer, GuiItem> entry : this.guiData.getItemStackSlots().entrySet()) {
            gui.setItem(entry.getKey(), entry.getValue());
        }

        gui.setDefaultClickAction(event -> {
            event.setCancelled(true);
            final int clickedSlot = event.getSlot();

            final ClickAction clickAction = this.guiData.getClickActionSlots().get(clickedSlot);

            if (clickAction == null) {
                return;
            }

            clickAction.act(this, event.getClick());
        });

        return gui;
    }
}
