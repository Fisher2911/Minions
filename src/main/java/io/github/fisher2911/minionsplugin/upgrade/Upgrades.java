package io.github.fisher2911.minionsplugin.upgrade;

/**
 * All upgrades that a minion can have, the default level is 1
 */
public class Upgrades {

    private final UpgradeData<SpeedUpgrade> speedUpgrade;

    public Upgrades(final UpgradeData<SpeedUpgrade> speedUpgrade) {
        this.speedUpgrade = speedUpgrade;
    }

    public UpgradeData<SpeedUpgrade> getSpeedUpgrade() {
        return this.speedUpgrade;
    }
}
