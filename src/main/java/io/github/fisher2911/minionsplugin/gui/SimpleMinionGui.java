package io.github.fisher2911.minionsplugin.gui;

import dev.triumphteam.gui.guis.Gui;
import dev.triumphteam.gui.guis.GuiItem;
import io.github.fisher2911.fishcore.util.builder.ItemBuilder;
import io.github.fisher2911.minionsplugin.gui.item.TypeItem;
import io.github.fisher2911.minionsplugin.lang.Placeholder;
import io.github.fisher2911.minionsplugin.minion.types.BaseMinion;
import io.github.fisher2911.minionsplugin.upgrade.type.UpgradeType;
import io.github.fisher2911.minionsplugin.upgrade.Upgrades;
import io.github.fisher2911.minionsplugin.user.MinionUser;
import net.kyori.adventure.text.Component;
import org.bukkit.inventory.ItemStack;

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

        final Map<String, String> placeholders = Placeholder.getMinionPlaceholders(this.minion);


        for (final Map.Entry<String, String> entry : placeholders.entrySet()) {
            guiOwner.ifOnline(player -> player.sendMessage(entry.getKey() + " : " + entry.getValue()));
        }

        final Gui gui = Gui.gui().
                title(Component.text(this.guiData.getTitle())).
                rows(this.guiData.getRows()).
                create();

        final List<GuiItem>  borderItemStacks = this.guiData.getBorderItemStacks();
        if (!borderItemStacks.isEmpty()) {
            gui.getFiller().fillBorder(borderItemStacks);
        }

        final Upgrades upgrades = this.minion.getUpgrades();

        for (final Map.Entry<Integer, TypeItem> entry : this.guiData.getItemStackSlots().entrySet()) {
            final int slot = entry.getKey();

            final TypeItem typeItem = entry.getValue();

            final String type = typeItem.getType();

            GuiItem setItem = typeItem.getGuiItemWithPlaceholders(placeholders);

            if (type != null) {
                try {
                    final ItemStack itemStack = ItemBuilder.
                            from(upgrades.getUpgradeData(UpgradeType.valueOf(type)).getGuiItemStack()).
                            namePlaceholders(placeholders).
                            lorePlaceholders(placeholders).
                            build();

                    setItem = new GuiItem(itemStack);
                } catch (final IllegalArgumentException ignored) {}
            }

            gui.setItem(slot, setItem);
        }

        gui.setDefaultClickAction(event -> {
            event.setCancelled(true);
            final int clickedSlot = event.getSlot();

            final ClickActions clickActions = this.guiData.getClickActionSlots().get(clickedSlot);

            if (clickActions == null) {
                return;
            }

            clickActions.runAll(this, event.getClick());
        });

        return gui;
    }
}
