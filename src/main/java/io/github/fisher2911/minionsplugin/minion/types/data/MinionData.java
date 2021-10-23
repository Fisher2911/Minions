package io.github.fisher2911.minionsplugin.minion.types.data;

import io.github.fisher2911.minionsplugin.minion.MinionInventory;
import org.jetbrains.annotations.NotNull;

public class MinionData {

    private final MinionInventory inventory;
    private String name;
    private int foodLevel;

    public MinionData(final @NotNull MinionInventory inventory, final @NotNull String name, final int foodLevel) {
        this.inventory = inventory;
        this.name = name;
        this.foodLevel = foodLevel;
    }

    public @NotNull MinionInventory getInventory() {
        return this.inventory;
    }

    public @NotNull String getName() {
        return this.name;
    }

    public int getFoodLevel() {
        return this.foodLevel;
    }

    public void setName(final @NotNull String name) {
        this.name = name;
    }

    public void addFood(final int amount) {
        this.foodLevel += amount;
    }

    public void removeFood(final int amount) {
        this.foodLevel -= amount;
    }
}
