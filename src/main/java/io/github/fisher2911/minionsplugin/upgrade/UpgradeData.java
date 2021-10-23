package io.github.fisher2911.minionsplugin.upgrade;

import org.jetbrains.annotations.NotNull;

public class UpgradeData<T extends MinionUpgrade> {

    final int level;
    final T upgrade;

    public UpgradeData(final int level, final @NotNull T upgrade) {
        this.level = level;
        this.upgrade = upgrade;
    }

    public int getLevel() {
        return this.level;
    }

    public @NotNull T getUpgrade() {
        return this.upgrade;
    }
}
