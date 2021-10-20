package io.github.fisher2911.minionsplugin.upgrade;

import io.github.fisher2911.fishcore.economy.Cost;
import io.github.fisher2911.fishcore.upgrade.Upgrade;

import java.util.Map;

public class SpeedUpgrade extends Upgrade<Float> {

    public SpeedUpgrade(final String id,
                        final String displayName,
                        final Map<Integer, Float> levelDataMap,
                        final Map<Integer, Cost> levelCostMap) {
        super(id, displayName, levelDataMap, levelCostMap);
    }
}
