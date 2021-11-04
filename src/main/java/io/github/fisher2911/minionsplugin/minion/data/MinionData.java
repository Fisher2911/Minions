package io.github.fisher2911.minionsplugin.minion.data;

import io.github.fisher2911.minionsplugin.minion.MinionInventory;
import io.github.fisher2911.minionsplugin.minion.food.FoodData;

public class MinionData {

    private final MinionInventory inventory;
    private final FoodData foodData;
    private String name;

    public MinionData(
            final MinionInventory inventory,
            final FoodData foodData,
            final String name) {
        this.inventory = inventory;
        this.foodData = foodData;
        this.name = name;
    }

    public MinionInventory getInventory() {
        return this.inventory;
    }

    public String getName() {
        return this.name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public FoodData getFoodData() {
        return this.foodData;
    }
}
