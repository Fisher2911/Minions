package io.github.fisher2911.minionsplugin.upgrade;

import io.github.fisher2911.fishcore.economy.Cost;
import io.github.fisher2911.fishcore.upgrade.Upgrade;
import io.github.fisher2911.minionsplugin.minion.types.BaseMinion;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public abstract class MinionUpgrade<T> extends Upgrade<T> {

    // Used to represent the upgrade in a GUI
    protected final ItemStack guiItemStack;

    public MinionUpgrade(final @NotNull String id,
                         final @NotNull String displayName,
                         final @NotNull Map<Integer, T> levelDataMap,
                         final @NotNull Map<Integer, Cost> levelCostMap,
                         final @NotNull ItemStack guiItemStack) {
        super(id, displayName, levelDataMap, levelCostMap);
        this.guiItemStack = guiItemStack;
    }

    public abstract ItemStack getGuiItemStack(final @NotNull BaseMinion<?> minion);
}
