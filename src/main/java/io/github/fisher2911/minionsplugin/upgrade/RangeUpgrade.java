package io.github.fisher2911.minionsplugin.upgrade;

import io.github.fisher2911.fishcore.economy.Cost;
import io.github.fisher2911.minionsplugin.world.Range;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

public class RangeUpgrade extends MinionUpgrade<Range> {

    public RangeUpgrade(
            final String id,
            final String displayName,
            final Map<Integer, Range> levelDataMap,
            final Map<Integer, Cost> levelCostMap,
            final ItemStack guiItemStack,
            final String type) {
        super(id, displayName, levelDataMap, levelCostMap, guiItemStack, type);
    }
}
