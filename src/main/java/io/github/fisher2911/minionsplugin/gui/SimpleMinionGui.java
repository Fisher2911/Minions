package io.github.fisher2911.minionsplugin.gui;

import dev.triumphteam.gui.guis.Gui;
import dev.triumphteam.gui.guis.GuiItem;
import io.github.fisher2911.fishcore.util.builder.ItemBuilder;
import io.github.fisher2911.fishcore.util.builder.SkullBuilder;
import io.github.fisher2911.minionsplugin.gui.item.TypeItem;
import io.github.fisher2911.minionsplugin.lang.Placeholder;
import io.github.fisher2911.minionsplugin.minion.types.BaseMinion;
import io.github.fisher2911.minionsplugin.permission.MinionPermission;
import io.github.fisher2911.minionsplugin.permission.RegisteredPermissions;
import io.github.fisher2911.minionsplugin.upgrade.Upgrades;
import io.github.fisher2911.minionsplugin.upgrade.type.UpgradeType;
import io.github.fisher2911.minionsplugin.user.MinionUser;
import org.bukkit.Bukkit;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;

public class SimpleMinionGui extends BaseMinionGui<Gui> {

    private final List<Gui> guis = new ArrayList<>();
    private final NavigableMap<Integer, GuiItem> actualItems = new TreeMap<>();
    private int currentPage;

    public SimpleMinionGui(
            final BaseMinion<?> baseMinion,
            final MinionUser guiOwner,
            final GuiData guiData) {
        super(baseMinion, guiOwner, guiData);
        this.populateGuis();
    }

    @Override
    public Gui get() {
        return this.getGuiPage(this.currentPage);
    }

    public void open(final HumanEntity player) {
        final Gui gui = this.get();

        if (gui == null) {
            return;
        }

        this.fillItems();

        gui.open(player);
    }

    @Override
    public void updateItems() {
        this.fillItems();
    }

    private void populateGuis() {
        this.fillItems(-1, -1);
        for (int slot = 0; slot <= this.actualItems.lastEntry().getKey(); slot++) {

            final int pageNumber = this.getPageNumberFromSlot(slot);

            Gui gui = this.getGuiPage(pageNumber);

            if (gui == null) {
                final Gui newGui = new Gui(this.guiData.getRows(), this.guiData.getTitle());

                final List<GuiItem> borderItems = this.guiData.getBorderItemStacks();

                if (!borderItems.isEmpty()) {
                    newGui.getFiller().fillBorder(borderItems);
                }
                if (this.guis.size() <= pageNumber) {
                    this.guis.add(newGui);
                } else {
                    this.guis.set(pageNumber, newGui);
                }

                newGui.setDefaultClickAction(event -> {
                    event.setCancelled(true);
                    final int clickedSlot = this.rawSlotToPageSlot(event.getSlot());

                    final ClickActions clickActions = this.guiData.getClickActionSlots().get(clickedSlot);

                    if (clickActions == null) {
                        return;
                    }

                    clickActions.runAll(this,
                            clickedSlot,
                            event.getClick());
                    this.fillItems();
                });

                gui = newGui;
            }

            final GuiItem guiItem = this.actualItems.get(slot);

            if (guiItem == null) {
                continue;
            }

            gui.setItem(slot - (pageNumber * (this.guiData.getRows() * 9)), guiItem);
        }
    }

    private void fillItemSlots() {
        int highestSlot = 0;
        for (final var entry : this.guiData.getFillItems().entrySet()) {
            final String type = entry.getKey();
            final ClickActions clickActions = entry.getValue();

            switch (type.toLowerCase(Locale.ROOT)) {
                case "permissions" ->
                        highestSlot = Math.max(this.fillPermissionsItems(clickActions), highestSlot);
                case "players" ->
                        highestSlot = Math.max(this.fillPlayerItems(clickActions), highestSlot);
            }
        }
    }

    private void fillItems() {

        final int size = this.guiData.getRows() * 9;

        final int minSlot = this.currentPage * size;

        this.fillItems(minSlot, minSlot + size);
    }

    private void fillItems(int min, int max) {
        for (final var entry : this.guiData.getItemStackSlots().entrySet()) {

            final int slot = entry.getKey();

            if (min != -1 && min > slot) {
                continue;
            }

            if (max != -1 && max < slot) {
                continue;
            }

            final TypeItem typeItem = this.guiData.getItemStackSlots().get(slot);

            if (typeItem != null) {
                this.actualItems.put(slot, this.typeItemToGuiItem(typeItem));
            }
        }

        this.fillItemSlots();

        min = Math.max(0, min);
        max = Math.min(this.actualItems.lastEntry().getKey() - 1, max);


        for (int slot = min; slot <= max; slot++) {
            final GuiItem item = this.actualItems.get(slot);

            if (item == null) {
                continue;
            }

            final Gui gui = this.getGuiPage(this.getPageNumberFromSlot(slot));

            if (gui == null) {
                continue;
            }

            final int pageNumber = this.getPageNumberFromSlot(slot);

            gui.setItem(slot - (pageNumber * (this.guiData.getRows() * 9)), item);
        }
    }

    /**
     *
     * @return the highest slot used
     */
    private int fillPermissionsItems(
            final ClickActions clickActions,
            final int minSlot,
            final int maxSlot) {
        int slot = 0;
        
        final Map<String, String> placeholders = Placeholder.getMinionPlaceholders(
                this.minion,
                this.guiOwner.getId()
        );

        for (final var entry : RegisteredPermissions.getAll().entrySet()) {
            slot = this.getNextAvailableSlot(slot);

            if (minSlot != -1 && minSlot > slot) {
                continue;
            }

            if (maxSlot != -1 && maxSlot < slot) {
                continue;
            }

            final MinionPermission permission = entry.getValue();
            final ItemStack displayItem = permission.getDisplayItem();

            final GuiItem guiItem = new GuiItem(
                    ItemBuilder.from(displayItem).
                            namePlaceholders(placeholders).
                            lorePlaceholders(placeholders).
                            build()
            );

            this.actualItems.put(slot, guiItem);
            this.guiData.getClickActionSlots().put(slot, clickActions);
        }

        return this.getNextAvailableSlot(slot) - 1;
    }

    private int fillPlayerItems(final ClickActions clickActions) {
        return this.fillPlayerItems(clickActions, -1, -1);
    }

    private int fillPermissionsItems(final ClickActions clickActions) {
        return this.fillPermissionsItems(clickActions, -1, -1);
    }

    /**
     *
     * @return the highest slot used
     */
    private int fillPlayerItems(
            final ClickActions clickActions,
            final int minSlot,
            final int maxSlot) {
        int slot = 0;

        final Map<String, String> placeholders = Placeholder.getMinionPlaceholders(
                this.minion,
                this.guiOwner.getId()
        );

        for (final Player player : Bukkit.getOnlinePlayers()) {
            slot = this.getNextAvailableSlot(slot);

            if (minSlot != -1 && minSlot > slot) {
                continue;
            }

            if (maxSlot != -1 && maxSlot < slot) {
                continue;
            }

            final ItemStack itemStack = SkullBuilder.
                    create().
                    owner(player).
                    namePlaceholders(placeholders).
                    lorePlaceholders(placeholders).
                    build();
            
            this.actualItems.put(slot, new GuiItem(itemStack));
            this.guiData.getClickActionSlots().put(slot, clickActions);
        }

        return this.getNextAvailableSlot(slot) - 1;
    }

    private int getNextAvailableSlot(int slot) {
        while ((isBorderSlot(slot) &&
                !this.guiData.getBorderItemStacks().isEmpty()) ||
                this.guiData.getItemStackSlots().containsKey(slot)) {
            slot++;
        }

        return slot;
    }

    private GuiItem typeItemToGuiItem(
            final TypeItem typeItem) {

        final String type = typeItem.getType();

        final Upgrades upgrades = this.minion.getUpgrades();

        final Map<String, String> placeholders = Placeholder.getMinionPlaceholders(
                this.minion,
                this.guiOwner.getId());

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

        return setItem;
    }

    @Nullable
    public Gui getGuiPage(final int page) {
        if (this.guis.size() <= page || page < 0) {
            return null;
        }

        return this.guis.get(page);
    }

    private boolean isBorderSlot(final int slot) {
        return slot < 9 ||
                slot % 9 == 0 ||
                (slot - 1) % 9 == 0 ||
                slot >= this.guiData.getRows() - 9;
    }

    private int getPageNumberFromSlot(final int itemSlot) {
        return (int) (float) (itemSlot -1) / (this.guiData.getRows() * 9);
    }

    @Override
    @Nullable
    public Gui nextPage() {
        final Gui gui = this.getGuiPage(this.currentPage + 1);
        if (gui != null) {
            this.currentPage++;
        }
        
        return gui;
    }

    @Override
    @Nullable
    public Gui previousPage() {
        final Gui gui = this.getGuiPage(this.currentPage - 1);
        if (gui != null) {
            this.currentPage--;
        }

        return gui;
    }

    @Override
    public void openNextPage(final HumanEntity player) {
        final Gui nextPage = this.nextPage();

        if (nextPage == null) {
            return;
        }


        nextPage.open(player);
    }

    @Override
    public void openPreviousPage(final HumanEntity player) {
        final Gui previousPage = this.previousPage();

        if (previousPage == null) {
            return;
        }

        previousPage.open(player);
    }

    private int rawSlotToPageSlot(final int slot) {
        return slot + (this.getPageNumberFromSlot(slot) * this.guiData.getRows());
    }
}
