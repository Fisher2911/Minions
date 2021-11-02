package io.github.fisher2911.minionsplugin.upgrade;

public class UpgradeData<T extends MinionUpgrade<R>, R> {

    /**
     * Level of the upgrade
     */
    final int level;

    /**
     * The {@link io.github.fisher2911.minionsplugin.upgrade.MinionUpgrade}
     */
    final T upgrade;

    public UpgradeData(final int level, final T upgrade) {
        this.level = level;
        this.upgrade = upgrade;
    }

    public int getLevel() {
        return this.level;
    }

    public T getUpgrade() {
        return this.upgrade;
    }

    public R getValue() {
        return this.upgrade.getDataAtLevel(this.level);
    }
}
