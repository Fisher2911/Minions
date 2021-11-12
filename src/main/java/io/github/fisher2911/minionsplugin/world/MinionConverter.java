package io.github.fisher2911.minionsplugin.world;

import dev.triumphteam.gui.builder.item.ItemBuilder;
import io.github.fisher2911.fishcore.world.Position;
import io.github.fisher2911.minionsplugin.MinionsPlugin;
import io.github.fisher2911.minionsplugin.keys.Keys;
import io.github.fisher2911.minionsplugin.minion.builder.MinionBuilder;
import io.github.fisher2911.minionsplugin.minion.data.MinionData;
import io.github.fisher2911.minionsplugin.minion.types.BaseMinion;
import io.github.fisher2911.minionsplugin.upgrade.UpgradeData;
import io.github.fisher2911.minionsplugin.upgrade.UpgradeGroupManager;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class MinionConverter {

    private final Map<Long, MinionData> savedMinionData;

    private final MinionsPlugin plugin;
    private final UpgradeGroupManager upgradeGroupManager;

    public MinionConverter(
            final Map<Long, MinionData> savedMinionData,
            final MinionsPlugin plugin) {
        this.savedMinionData = savedMinionData;
        this.plugin = plugin;
        this.upgradeGroupManager = this.plugin.getUpgradeGroupManager();
    }

    public ItemStack minionToItemStack(final BaseMinion<?> minion) {
        final MinionData minionData = minion.getMinionData();

        final double foodLevel = minionData.getFoodData().getFoodLevel();

        final List<Component> lore = new ArrayList<>();
        lore.add(Component.empty());
        lore.add(Component.text(minionData.getName()));
        lore.add(Component.text("Food: " + foodLevel));
        lore.add(Component.text("Upgrades:"));

        final List<UpgradeData<?, ?>> upgradeDataList = minionData.getUpgrades().getAllUpgrades();

        for (final var upgradeData : upgradeDataList) {
            lore.add(Component.text(
                    upgradeData.getUpgrade().getDisplayName() +
                            " - Level " + upgradeData.getLevel()));
        }

        return ItemBuilder.from(
                        Material.ARMOR_STAND
                ).name(Component.text(minionData.getName())).
                // todo - get settings from config
                        lore(lore).
                pdc(container ->
                        container.set(Keys.MINION_ID_KEY, PersistentDataType.LONG, minionData.getId())
                ).build();
    }

    public Optional<MinionData> itemStackToMinionData(final ItemStack itemStack) {

        final Optional<MinionData> EMPTY = Optional.empty();


        final ItemMeta itemMeta = itemStack.getItemMeta();

        if (itemMeta == null) {
            return EMPTY;
        }

        final PersistentDataContainer container = itemMeta.getPersistentDataContainer();

        final Long id = container.get(Keys.MINION_ID_KEY, PersistentDataType.LONG);


        if (id == null || id == -1) {
            return EMPTY;
        }

        return Optional.ofNullable(this.savedMinionData.get(id));
    }

    public Optional<BaseMinion<?>> itemStackToMinion(
            final ItemStack itemStack,
            final Position position) {

        final Optional<BaseMinion<?>> EMPTY = Optional.empty();

        final Optional<MinionData> optionalMinionData = this.itemStackToMinionData(itemStack);

        if (optionalMinionData.isEmpty()) {
            return EMPTY;
        }

        final MinionData minionData = optionalMinionData.get();

        return Optional.of(MinionBuilder.builder(this.plugin).
                minionData(minionData).
                position(position).build());
    }

    public void remove(final long id) {
        this.savedMinionData.remove(id);
    }

    public void add(final long id, final MinionData minionData) {
        this.savedMinionData.put(id, minionData);
    }

    public boolean isMinionItem(final ItemStack itemStack) {
        final ItemMeta itemMeta = itemStack.getItemMeta();

        if (itemMeta == null) {
            return false;
        }

        return itemMeta.getPersistentDataContainer().has(Keys.MINION_ID_KEY, PersistentDataType.LONG);
    }
}
