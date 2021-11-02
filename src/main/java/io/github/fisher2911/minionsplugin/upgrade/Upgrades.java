package io.github.fisher2911.minionsplugin.upgrade;

import io.github.fisher2911.minionsplugin.user.MinionUser;

/**
 * All upgrades that a minion can have, the default level is 1
 */
public class Upgrades {

    private final UpgradeData<SpeedUpgrade, Float> speedUpgrade;

    public Upgrades(final UpgradeData<SpeedUpgrade, Float> speedUpgrade) {
        this.speedUpgrade = speedUpgrade;
    }

    public UpgradeData<SpeedUpgrade, Float> getSpeedUpgrade() {
        return this.speedUpgrade;
    }

    public float getSpeed() {
        return this.speedUpgrade.getValue();
    }

    public void attemptUpgrade(final String type, final MinionUser user) {
        switch (type) {
            case UpgradeType.SPEED_UPGRADE -> {
                this.speedUpgrade.attemptUpgrade(user);
            }
        }
    }
}
