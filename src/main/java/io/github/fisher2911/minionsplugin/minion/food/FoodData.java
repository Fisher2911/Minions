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

    /**
     *
     * @param material Material being fed
     * @return the worth of the food
     */
    public FeedResponse feed(final Material material) {
        final float foodValue = this.getFoodWorth(material);
        if (foodValue == 0) {
            return FeedResponse.CANNOT_FEED;
        }
        this.foodLevel += foodValue;
        return FeedResponse.FED;
    }

    public void decreaseFood(final float amount) {
        this.foodLevel -= amount;
    }

    public boolean hasFood() {
        return this.foodLevel > 0;
    }
}
