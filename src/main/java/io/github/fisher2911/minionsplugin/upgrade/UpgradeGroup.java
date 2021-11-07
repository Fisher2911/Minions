package io.github.fisher2911.minionsplugin.upgrade;

import io.github.fisher2911.fishcore.util.helper.IdHolder;
import io.github.fisher2911.minionsplugin.upgrade.type.FloatUpgrade;
import io.github.fisher2911.minionsplugin.upgrade.type.RangeUpgrade;

public class UpgradeGroup implements IdHolder<String> {

    private final String id;
    private final FloatUpgrade speedUpgrade;
    private final RangeUpgrade rangeUpgrade;
    private final FloatUpgrade maxFoodUpgrade;
    private final FloatUpgrade foodPerActionUpgrade;

    UpgradeGroup(
            final String id,
            final FloatUpgrade speedUpgrade,
            final RangeUpgrade rangeUpgrade,
            final FloatUpgrade maxFoodUpgrade,
            final FloatUpgrade foodPerActionUpgrade) {
        this.id = id;
        this.speedUpgrade = speedUpgrade;
        this.rangeUpgrade = rangeUpgrade;
        this.maxFoodUpgrade = maxFoodUpgrade;
        this.foodPerActionUpgrade = foodPerActionUpgrade;
    }

    public static UpgradeGroupBuilder builder() {
        return new UpgradeGroupBuilder();
    }

    @Override
    public String getId() {
        return this.id;
    }

    public FloatUpgrade getSpeedUpgrade() {
        return this.speedUpgrade;
    }

    public RangeUpgrade getRangeUpgrade() {
        return this.rangeUpgrade;
    }

    public FloatUpgrade getFoodPerActionUpgrade() {
        return this.maxFoodUpgrade;
    }

    public FloatUpgrade getMaxFoodUpgrade() {
        return this.foodPerActionUpgrade;
    }

    public Upgrades toUpgrades() {
        return new Upgrades(
                this.id,
                new UpgradeData<>(1, this.speedUpgrade),
                new UpgradeData<>(1, this.rangeUpgrade),
                new UpgradeData<>(1, this.foodPerActionUpgrade),
                new UpgradeData<>(1, this.maxFoodUpgrade)
        );
    }
}
