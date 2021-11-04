package io.github.fisher2911.minionsplugin.upgrade;

import io.github.fisher2911.fishcore.economy.Cost;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

public class MaxFoodUpgrade extends MinionUpgrade<Float> {

    public MaxFoodUpgrade(
            final String id,
            final String displayName,
            final Map<Integer, Float> levelDataMap,
            final Map<Integer, Cost> levelCostMap,
            final ItemStack guiItemStack, final String type) {
        super(id, displayName, levelDataMap, levelCostMap, guiItemStack, type);
    }
}
