package io.github.fisher2911.minionsplugin.minion.food;

import org.bukkit.Material;

public class FoodData {

    private final FoodGroup foodGroup;
    private float foodLevel;

    public FoodData(
            final FoodGroup foodGroup,
            final float foodLevel) {
        this.foodGroup = foodGroup;
        this.foodLevel = foodLevel;
    }

    public float getFoodLevel() {
        return this.foodLevel;
    }

    public float getFoodWorth(final Material material) {
        return this.foodGroup.getFoodValue(material);
    }

    public void setFood(final float foodLevel) {
        this.foodLevel = foodLevel;
    }

    public void decreaseFood(final float amount) {
        this.foodLevel -= amount;
    }

    public boolean hasFood() {
        return this.foodLevel > 0;
    }
}
