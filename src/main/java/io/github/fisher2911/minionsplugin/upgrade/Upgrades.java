package io.github.fisher2911.minionsplugin.upgrade;

import io.github.fisher2911.minionsplugin.upgrade.type.FloatUpgrade;
import io.github.fisher2911.minionsplugin.upgrade.type.RangeUpgrade;
import io.github.fisher2911.minionsplugin.upgrade.type.UpgradeType;
import io.github.fisher2911.minionsplugin.user.MinionUser;
import io.github.fisher2911.minionsplugin.world.Range;

/**
 * All upgrades that a minion can have, the default level is 1
 */
public class Upgrades {

    private final String id;
    private final UpgradeData<FloatUpgrade, Float> speedUpgrade;
    private final UpgradeData<RangeUpgrade, Range> rangeUpgrade;
    private final UpgradeData<FloatUpgrade, Float> foodPerActionUpgrade;
    private final UpgradeData<FloatUpgrade, Float> maxFoodUpgrade;

    public Upgrades(
            final String id,
            final UpgradeData<FloatUpgrade, Float> speedUpgrade,
            final UpgradeData<RangeUpgrade, Range> rangeUpgrade,
            final UpgradeData<FloatUpgrade, Float> foodPerActionUpgrade,
            final UpgradeData<FloatUpgrade, Float> maxFoodUpgrade) {
        this.id = id;
        this.speedUpgrade = speedUpgrade;
        this.rangeUpgrade = rangeUpgrade;
        this.foodPerActionUpgrade = foodPerActionUpgrade;
        this.maxFoodUpgrade = maxFoodUpgrade;
    }

    public UpgradeData<FloatUpgrade, Float> getSpeedUpgrade() {
        return this.speedUpgrade;
    }

    public UpgradeData<RangeUpgrade, Range> getRangeUpgrade() {
        return this.rangeUpgrade;
    }

    public UpgradeData<FloatUpgrade, Float> getFoodPerActionUpgrade() {
        return this.foodPerActionUpgrade;
    }

    public UpgradeData<FloatUpgrade, Float> getMaxFoodUpgrade() {
        return this.maxFoodUpgrade;
    }

    public float getSpeed() {
        return this.speedUpgrade.getValue();
    }

    public Range getRange() {
        return this.rangeUpgrade.getValue();
    }

    public float getFoodPerAction() {
        return this.foodPerActionUpgrade.getValue();
    }

    public float getMaxFood() {
        return this.maxFoodUpgrade.getValue();
    }

    public UpgradeData<?, ?> getUpgradeData(final UpgradeType type) {
        return switch (type) {
            case SPEED_UPGRADE -> this.speedUpgrade;
            case RANGE_UPGRADE -> this.rangeUpgrade;
            case FOOD_PER_ACTION_UPGRADE -> this.foodPerActionUpgrade;
            case MAX_FOOD_UPGRADE -> this.maxFoodUpgrade;
        };
    }

    public void attemptUpgrade(final UpgradeType type, final MinionUser user) {
        this.getUpgradeData(type).attemptUpgrade(user);
    }

    public String getId() {
        return this.id;
    }
}
