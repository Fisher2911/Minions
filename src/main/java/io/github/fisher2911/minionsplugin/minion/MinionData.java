package io.github.fisher2911.minionsplugin.minion;

public class MinionData {

    private final MinionInventory inventory;
    private String name;
    private int foodLevel;

    public MinionData(final MinionInventory inventory, final String name, final int foodLevel) {
        this.inventory = inventory;
        this.name = name;
        this.foodLevel = foodLevel;
    }

    public MinionInventory getInventory() {
        return this.inventory;
    }

    public String getName() {
        return this.name;
    }

    public int getFoodLevel() {
        return this.foodLevel;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public void addFood(final int amount) {
        this.foodLevel += amount;
    }

    public void removeFood(final int amount) {
        this.foodLevel -= amount;
    }
}
