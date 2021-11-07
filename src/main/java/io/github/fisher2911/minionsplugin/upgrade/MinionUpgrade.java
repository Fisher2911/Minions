package io.github.fisher2911.minionsplugin.upgrade;

import io.github.fisher2911.fishcore.economy.Cost;
import io.github.fisher2911.fishcore.message.MessageHandlerRegistry;
import io.github.fisher2911.fishcore.upgrade.Upgrade;
import io.github.fisher2911.fishcore.util.builder.ItemBuilder;
import io.github.fisher2911.minionsplugin.MinionsPlugin;
import io.github.fisher2911.minionsplugin.lang.MinionMessages;
import io.github.fisher2911.minionsplugin.lang.Placeholder;
import io.github.fisher2911.minionsplugin.upgrade.type.UpgradeType;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

public abstract class MinionUpgrade<T> extends Upgrade<T> {

    /**
     * Used to represent the upgrade in a GUI
     */
    protected final ItemStack guiItemStack;
    protected final UpgradeType type;

    public MinionUpgrade(
            final String id,
            final String displayName,
            final Map<Integer, T> levelDataMap,
            final Map<Integer, Cost> levelCostMap,
            final ItemStack guiItemStack,
            final UpgradeType type) {
        super(id, displayName, levelDataMap, levelCostMap);
        this.guiItemStack = guiItemStack;
        this.type = type;
    }

    public ItemStack getGuiItemStack(final int level, final T value) {
        final Cost cost = this.getCostAtLevel(level);

        final String moneyPrice;

        if (cost == null) {
            moneyPrice = MessageHandlerRegistry.REGISTRY.get(MinionsPlugin.class).getMessage(MinionMessages.MAX_LEVEL);
        } else {
            moneyPrice = String.valueOf(cost.getMoneyCost());
        }

        final Map<String, String> placeHolders = Map.of(
                Placeholder.LEVEL, String.valueOf(level),
                Placeholder.SPEED,
                String.valueOf(this.getDataAtLevel(level)),
                Placeholder.MONEY_COST, moneyPrice);

        return ItemBuilder.from(this.guiItemStack).
                namePlaceholders(placeHolders).
                lorePlaceholders(placeHolders).
                build();
    }

    public UpgradeType getType() {
        return this.type;
    }
}
