package io.github.fisher2911.minionsplugin.upgrade;

import org.jetbrains.annotations.NotNull;

public class Upgrades {

    private final UpgradeData<SpeedUpgrade> speedUpgrade;

    public Upgrades(final @NotNull UpgradeData<SpeedUpgrade> speedUpgrade) {
        this.speedUpgrade = speedUpgrade;
    }

    public @NotNull UpgradeData<SpeedUpgrade> getSpeedUpgrade() {
        return this.speedUpgrade;
    }
}
