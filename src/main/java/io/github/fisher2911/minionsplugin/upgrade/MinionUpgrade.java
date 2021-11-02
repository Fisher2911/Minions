package io.github.fisher2911.minionsplugin.upgrade;

import io.github.fisher2911.fishcore.economy.Cost;
import io.github.fisher2911.fishcore.upgrade.Upgrade;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

public abstract class MinionUpgrade<T> extends Upgrade<T> {

    /**
     * Used to represent the upgrade in a GUI
     */

    protected final ItemStack guiItemStack;

    public MinionUpgrade(
            final String id,
            final String displayName,
            final Map<Integer, T> levelDataMap,
            final Map<Integer, Cost> levelCostMap,
            final ItemStack guiItemStack) {
        super(id, displayName, levelDataMap, levelCostMap);
        this.guiItemStack = guiItemStack;
    }

    public abstract ItemStack getGuiItemStack(final int level, final T value);
}
