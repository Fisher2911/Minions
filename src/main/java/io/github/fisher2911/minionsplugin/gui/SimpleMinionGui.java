package io.github.fisher2911.minionsplugin.gui;

import dev.triumphteam.gui.guis.Gui;
import dev.triumphteam.gui.guis.GuiItem;
import io.github.fisher2911.fishcore.util.builder.ItemBuilder;
import io.github.fisher2911.fishcore.util.builder.SkullBuilder;
import io.github.fisher2911.minionsplugin.gui.action.ClickActions;
import io.github.fisher2911.minionsplugin.gui.action.FillInstructions;
import io.github.fisher2911.minionsplugin.gui.item.FillItem;
import io.github.fisher2911.minionsplugin.gui.item.TypeItem;
import io.github.fisher2911.minionsplugin.lang.Placeholder;
import io.github.fisher2911.minionsplugin.minion.data.MinionData;
import io.github.fisher2911.minionsplugin.minion.types.BaseMinion;
import io.github.fisher2911.minionsplugin.permission.MinionPermission;
import io.github.fisher2911.minionsplugin.permission.MinionPermissionsGroup;
import io.github.fisher2911.minionsplugin.permission.PermissionManager;
import io.github.fisher2911.minionsplugin.upgrade.type.UpgradeType;
import io.github.fisher2911.minionsplugin.user.MinionUser;
import io.github.fisher2911.minionsplugin.util.Displayable;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.HumanEntity;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

public class SimpleMinionGui extends BaseMinionGui<Gui> {

    private PermissionManager permissionManager;
    private final List<Gui> guis = new ArrayList<>();
    private int highestSlot;
    private final Map<Integer, TypeItem> actualItems = new HashMap<>();
    private int currentPage;

    public SimpleMinionGui(
            final BaseMinion<?> baseMinion,
            final MinionUser guiOwner,
            final GuiData guiData,
            final String extraData) {
        super(baseMinion, guiOwner, guiData, extraData);
        this.permissionManager = PermissionManager.getInstance();
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

        gui.open(player);
    }

    @Override
    public void updateItems() {
        this.fillItems();

        final Gui gui = this.get();

        if (gui != null) {
            gui.update();
        }
    }

    private void populateGuis() {
        this.fillItems(-1, -1);
        for (int slot = 0; slot <= this.highestSlot; slot++) {

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

                    final Collection<ClickActions> clickActionsCollection = this.guiData.getClickActionSlots().get(clickedSlot);

                    if (clickActionsCollection == null ||
                            clickActionsCollection.isEmpty()) {
                        return;
                    }

                    for (final var clickActions : clickActionsCollection) {
                        clickActions.runAll(this,
                                clickedSlot,
                                event.getClick());
                        this.fillItems();
                    }
                });

                gui = newGui;
            }

            final TypeItem typeItem = this.getGuiTypeItem(slot);

            if (typeItem == null) {
                continue;
            }

            gui.setItem(slot - (pageNumber * (this.guiData.getRows() * 9)),
                    typeItem.getGuiItem());
        }
    }

    private @Nullable TypeItem getGuiTypeItem(final int slot) {
        final TypeItem typeItem = this.actualItems.get(slot);

        if (typeItem == null) {
            return null;
        }

        final String type = typeItem.getType();
        final String value = typeItem.getValue();

        final Map<String, String> placeholders = Placeholder.getMinionPlaceholders(
                this.minion,
                this.guiOwner.getId()
        );

        if (type == null || value == null) {
            return new TypeItem(type,
                    value,
                    typeItem.getGuiItemWithPlaceholders(placeholders)
            );
        }

        if (type.equals(TypeItem.Types.PLAYER_UUID)) {

            final UUID uuid = UUID.fromString(value);

            placeholders.putAll(Placeholder.getPermissionsPlaceholders(
                    this.minion,
                    uuid
            ));
        }

        if (!type.equals(TypeItem.Types.UPGRADE)) {
            return new TypeItem(type,
                    value,
                    typeItem.getGuiItemWithPlaceholders(placeholders)
            );
        }

        try {
            final ItemStack itemStack = ItemBuilder.
                    from(this.minion.
                            getMinionData().
                            getUpgrades().
                            getUpgradeData(UpgradeType.valueOf(value)).
                            getGuiItemStack()).
                    namePlaceholders(placeholders).
                    lorePlaceholders(placeholders).
                    build();

            return new TypeItem(typeItem.getType(),
                    typeItem.getValue(),
                    new GuiItem(itemStack));
        } catch (final IllegalArgumentException ignored) {
            return new TypeItem(
                    type,
                    value,
                    typeItem.getGuiItemWithPlaceholders(placeholders));
        }
    }

    private void fillItemSlots() {
        int highestSlot = 0;
        for (final var entry : this.guiData.getFillInstructions().entrySet()) {
            final String type = entry.getKey();
            final FillInstructions fillInstructions = entry.getValue();

            final FillItem fillItem = fillInstructions.getFillItem();

            switch (type.toLowerCase(Locale.ROOT)) {
                case "permissions" -> highestSlot = Math.max(this.fillPermissionsItems(fillItem), highestSlot);
                case "permission-groups" -> highestSlot = Math.max(this.fillPermissionGroupItems(fillItem), highestSlot);
                case "online-players" -> highestSlot = Math.max(this.fillPlayerItems(fillItem,
                        Bukkit.getOnlinePlayers()), highestSlot);
                case "permission-group-players" -> highestSlot = Math.max(this.
                        fillPermissionGroupPlayerItems(fillItem), highestSlot);
            }
        }

        this.highestSlot = Math.max(highestSlot, this.highestSlot);
    }

    private void fillItems() {

        final int size = this.guiData.getRows() * 9;

        final int minSlot = this.currentPage * size;

        this.fillItems(minSlot, minSlot + size);
    }

    private void fillItems(int min, int max) {
        this.actualItems.clear();

        for (final Gui gui : this.guis) {
            gui.getGuiItems().clear();
            if (this.guiData.getBorderItemStacks().isEmpty()) {
                continue;
            }
            gui.getFiller().fillBorder(this.guiData.getBorderItemStacks());
        }

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
                this.actualItems.put(slot, typeItem);
            }

            this.highestSlot = Math.max(slot, this.highestSlot);
        }

        this.fillItemSlots();

        min = Math.max(0, min);
        max = Math.min(this.highestSlot, max);


        for (int slot = min; slot <= max; slot++) {
            final TypeItem item = this.getGuiTypeItem(slot);

            if (item == null) {
                continue;
            }

            final Gui gui = this.getGuiPage(this.getPageNumberFromSlot(slot));

            if (gui == null) {
                continue;
            }

            final int guiSlot = this.pageSlotToRawSLot(slot);

            gui.setItem(guiSlot,
                    item.getGuiItem());
        }
    }

    /**
     * @return the highest slot used
     */
    private int fillPermissionsItems(
            final FillItem fillItem,
            final int minSlot,
            final int maxSlot) {
        final Collection<MinionPermission> permissions = new HashSet<>();

        for (final var entry : this.permissionManager.getAll().entrySet()) {
            permissions.add(entry.getValue());
        }

        return this.fillDisplayableItems(
                fillItem,
                permissions,
                minSlot,
                maxSlot
        );
    }

    private int fillDisplayableItems(final FillItem fillItem,
                                     final Collection<? extends Displayable> displayItems) {
        return this.fillDisplayableItems(
                fillItem,
                displayItems,
                -1,
                -1);
    }

    private int fillDisplayableItems(final FillItem fillItem,
                                     final Collection<? extends Displayable> displayItems,
                                     final int minSlot,
                                     final int maxSlot) {
        int slot = 0;

        for (final Displayable displayable : displayItems) {

            slot = this.getNextAvailableSlot(slot);

            if (minSlot != -1 && minSlot > slot) {
                continue;
            }

            if (maxSlot != -1 && maxSlot < slot) {
                continue;
            }

            final TypeItem typeItem = this.getFillItem(
                    fillItem,
                    displayable.getDisplayItem());

            this.actualItems.put(slot, typeItem);
            this.guiData.getClickActionSlots().putAll(slot, fillItem.getClickActions());
        }

        return this.getNextAvailableSlot(slot) - 1;
    }

    private int fillPermissionGroupItems(final FillItem fillItem) {
        return this.fillPermissionGroupItems(fillItem, -1, -1);
    }

    private int fillPermissionGroupItems(
            final FillItem fillItem,
            final int minSlot,
            final int maxSlot) {
        final MinionData minionData = this.minion.getMinionData();

        final List<MinionPermissionsGroup> permissionsGroups = minionData.getMinionPermissionsGroups();

        return this.fillDisplayableItems(fillItem,
                permissionsGroups,
                minSlot,
                maxSlot);
    }

    private int fillPlayerItems(
            final FillItem fillItem,
            final Collection<? extends OfflinePlayer> players) {
        return this.fillPlayerItems(fillItem, players, -1, -1);
    }

    private int fillPermissionsItems(final FillItem fillItem) {
        return this.fillPermissionsItems(fillItem, -1, -1);
    }

    /**
     * @return the highest slot used
     */
    private int fillPlayerItems(
            final FillItem fillItem,
            final Collection<? extends OfflinePlayer> players,
            final int minSlot,
            final int maxSlot) {
        int slot = 0;

        for (final OfflinePlayer player : players) {
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
                    build();

            final TypeItem typeItem = this.getFillItem(
                    fillItem,
                    new TypeItem(
                            TypeItem.Types.PLAYER_UUID,
                            player.getUniqueId().toString(),
                            itemStack
                    )
            );

            this.actualItems.put(slot, typeItem);
            this.guiData.getClickActionSlots().putAll(slot, fillItem.getClickActions());
        }

        return this.getNextAvailableSlot(slot) - 1;
    }

    private int fillPermissionGroupPlayerItems(final FillItem fillItem) {
        MinionPermissionsGroup group = this.minion.getMinionData().getMinionPermissionsGroup(this.extraData);

        if (group == null) {
            return 0;
        }

        final Collection<OfflinePlayer> players = new HashSet<>();

        for (final UUID member : group.getMembers()) {

            players.add(Bukkit.getOfflinePlayer(member));
        }

        return this.fillPlayerItems(fillItem, players);
    }

    private int getNextAvailableSlot(int slot) {

        final boolean hasBorderItems = !this.guiData.getBorderItemStacks().isEmpty();

        while ((isBorderSlot(slot) && hasBorderItems) ||
                this.actualItems.containsKey(slot)) {
            slot++;
        }

        return slot;
    }

    @Nullable
    public Gui getGuiPage(final int page) {
        if (this.guis.size() <= page || page < 0) {
            return null;
        }

        return this.guis.get(page);
    }

    private boolean isBorderSlot(int slot) {
        slot = this.pageSlotToRawSLot(slot);
        return this.guiData.getRows() > 2
                && (slot < 9 ||
                slot % 9 == 0 ||
                (slot + 1) % 9 == 0 ||
                slot >= this.guiData.getRows() * 9 - 9
                        && slot < this.guiData.getRows() * 9);
    }

    private int getPageNumberFromSlot(final int itemSlot) {
        return (int) (float) (itemSlot - 1) / (this.guiData.getRows() * 9);
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

        this.updateItems();

        nextPage.open(player);
    }

    @Override
    public void openPreviousPage(final HumanEntity player) {
        final Gui previousPage = this.previousPage();

        if (previousPage == null) {
            return;
        }

        this.updateItems();

        previousPage.open(player);
    }

    private int rawSlotToPageSlot(final int slot) {
        return slot + (this.getPageNumberFromSlot(slot) * this.guiData.getRows() * 9);
    }

    private int pageSlotToRawSLot(final int slot) {
        return slot - (this.getPageNumberFromSlot(slot) * this.guiData.getRows() * 9);
    }

    @Nullable
    @Override
    public TypeItem getItemAtSlot(int slot,
                                  final boolean accountForPage) {
        if (accountForPage) {
            slot = this.rawSlotToPageSlot(slot);
        }
        return this.actualItems.get(slot);
    }

    private TypeItem getFillItem(
            final FillItem fillItem,
            final TypeItem displayItem) {
        final String type = displayItem.getType();
        final String value = displayItem.getValue();

        final ItemBuilder itemBuilder = ItemBuilder.from(
                displayItem.getItemStack()
        );

        final String name = fillItem.getName();
        final List<String> lore = fillItem.getLore();

        if (name != null) {
            itemBuilder.name(name);
        }

        if (lore != null && !lore.isEmpty()) {
            itemBuilder.lore(lore);
        }

        final Map<String, String> placeholders = new HashMap<>();

        if (type != null) {
            placeholders.put(Placeholder.TYPE, type);
        }

        if (value != null) {
            placeholders.put(Placeholder.VALUE, value);
        }

        if (this.extraData != null) {
            placeholders.put(Placeholder.EXTRA_DATA, this.extraData);
        }

        itemBuilder.namePlaceholders(placeholders);
        itemBuilder.lorePlaceholders(placeholders);

        return new TypeItem(
                displayItem.getType(),
                displayItem.getValue(),
                itemBuilder.build()
        );
    }
}
