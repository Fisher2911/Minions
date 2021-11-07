package io.github.fisher2911.minionsplugin.upgrade.type;

import io.github.fisher2911.fishcore.economy.Cost;
import io.github.fisher2911.minionsplugin.upgrade.MinionUpgrade;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

public class FloatUpgrade extends MinionUpgrade<Float> {

    public FloatUpgrade(
            final String id,
            final String displayName,
            final Map<Integer, Float> levelDataMap,
            final Map<Integer, Cost> levelCostMap,
            final ItemStack guiItemStack,
            final UpgradeType type) {
        super(id, displayName, levelDataMap, levelCostMap, guiItemStack, type);
    }
}
