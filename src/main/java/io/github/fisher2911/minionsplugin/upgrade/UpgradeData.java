package io.github.fisher2911.minionsplugin.upgrade;

import io.github.fisher2911.fishcore.economy.Cost;
import io.github.fisher2911.minionsplugin.user.MinionUser;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

public class UpgradeData<T extends MinionUpgrade<R>, R> {

    /**
     * Level of the upgrade
     */
    private int level;

    /**
     * The {@link io.github.fisher2911.minionsplugin.upgrade.MinionUpgrade}
     */
    private final T upgrade;

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

    public @Nullable Cost getNextLevelCost() {
        return this.upgrade.getCostAtLevel(this.level + 1);
    }

    public R getValue(final R defaultValue) {
        return this.upgrade.getDataAtLevel(this.level, defaultValue);
    }

    public ItemStack getGuiItemStack() {
        return this.upgrade.getGuiItemStack(this.level, this.upgrade.getDataAtLevel(this.level));
    }

    public void attemptUpgrade(final MinionUser user) {
        final double balance = user.getMoney();

        user.ifOnline(player -> player.sendMessage("Your money is " + balance));

        final Cost cost = this.upgrade.getCostAtLevel(this.level + 1);

        if (cost == null) {
            user.ifOnline(player -> player.sendMessage("Cost is null"));
            return;
        }

        final double moneyCost = cost.getMoneyCost();

        user.ifOnline(player -> player.sendMessage("Upgrade cost: " + moneyCost));

        if (balance >= moneyCost) {
            user.ifOnline(player -> player.sendMessage("Upgrade successful"));
            this.level++;
        } else {
            user.ifOnline(player -> player.sendMessage("Upgrade not successful"));
        }

    }
}
