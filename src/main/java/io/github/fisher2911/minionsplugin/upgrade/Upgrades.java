package io.github.fisher2911.minionsplugin.upgrade;

import io.github.fisher2911.minionsplugin.user.MinionUser;
import io.github.fisher2911.minionsplugin.world.Range;

/**
 * All upgrades that a minion can have, the default level is 1
 */
public class Upgrades {

    private final UpgradeData<SpeedUpgrade, Float> speedUpgrade;
    private final UpgradeData<RangeUpgrade, Range> rangeUpgrade;

    public Upgrades(final UpgradeData<SpeedUpgrade, Float> speedUpgrade, final UpgradeData<RangeUpgrade, Range> rangeUpgrade) {
        this.speedUpgrade = speedUpgrade;
        this.rangeUpgrade = rangeUpgrade;
    }

    public UpgradeData<SpeedUpgrade, Float> getSpeedUpgrade() {
        return this.speedUpgrade;
    }

    public UpgradeData<RangeUpgrade, Range> getRangeUpgrade() {
        return this.rangeUpgrade;
    }

    public float getSpeed() {
        return this.speedUpgrade.getValue();
    }

    public Range getRange() {
        return this.rangeUpgrade.getValue();
    }

    public UpgradeData<?, ?> getUpgradeData(final String type) {
        return switch (type) {
            case UpgradeType.SPEED_UPGRADE -> this.speedUpgrade;
            case UpgradeType.RANGE_UPGRADE -> this.rangeUpgrade;
            default -> null;
        };
    }

    public void attemptUpgrade(final String type, final MinionUser user) {
        this.getUpgradeData(type).attemptUpgrade(user);
    }
}
