package io.github.fisher2911.minionsplugin.upgrade;

import io.github.fisher2911.minionsplugin.user.MinionUser;
import io.github.fisher2911.minionsplugin.world.Range;

/**
 * All upgrades that a minion can have, the default level is 1
 */
public class Upgrades {

    private final UpgradeData<SpeedUpgrade, Float> speedUpgrade;
    private final UpgradeData<RangeUpgrade, Range> rangeUpgrade;
    private final UpgradeData<FoodPerActionUpgrade, Float> foodPerActionUpgrade;

    public Upgrades(
            final UpgradeData<SpeedUpgrade, Float> speedUpgrade,
            final UpgradeData<RangeUpgrade, Range> rangeUpgrade,
            final UpgradeData<FoodPerActionUpgrade, Float> foodPerActionUpgrade) {
        this.speedUpgrade = speedUpgrade;
        this.rangeUpgrade = rangeUpgrade;
        this.foodPerActionUpgrade = foodPerActionUpgrade;
    }

    public UpgradeData<SpeedUpgrade, Float> getSpeedUpgrade() {
        return this.speedUpgrade;
    }

    public UpgradeData<RangeUpgrade, Range> getRangeUpgrade() {
        return this.rangeUpgrade;
    }

    public UpgradeData<FoodPerActionUpgrade, Float> getFoodPerActionUpgrade() {
        return this.foodPerActionUpgrade;
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

    public UpgradeData<?, ?> getUpgradeData(final String type) {
        return switch (type) {
            case UpgradeType.SPEED_UPGRADE -> this.speedUpgrade;
            case UpgradeType.RANGE_UPGRADE -> this.rangeUpgrade;
            case UpgradeType.FOOD_PER_ACTION -> this.foodPerActionUpgrade;
            default -> null;
        };
    }

    public void attemptUpgrade(final String type, final MinionUser user) {
        this.getUpgradeData(type).attemptUpgrade(user);
    }
}
