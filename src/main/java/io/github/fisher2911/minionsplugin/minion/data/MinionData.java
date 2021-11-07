package io.github.fisher2911.minionsplugin.minion.data;

import io.github.fisher2911.minionsplugin.minion.MinionInventory;
import io.github.fisher2911.minionsplugin.minion.food.FeedResponse;
import io.github.fisher2911.minionsplugin.minion.food.FoodData;
import io.github.fisher2911.minionsplugin.upgrade.Upgrades;
import org.bukkit.inventory.ItemStack;

public class MinionData {

    private final MinionInventory inventory;
    private final FoodData foodData;
    private final Upgrades upgrades;
    private String name;

    public MinionData(
            final MinionInventory inventory,
            final FoodData foodData,
            final Upgrades upgrades,
            final String name) {
        this.inventory = inventory;
        this.foodData = foodData;
        this.upgrades = upgrades;
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

    public Upgrades getUpgrades() {
        return this.upgrades;
    }

    public FeedResponse feed(final ItemStack itemStack) {

        final float itemWorth = this.foodData.getFoodWorth(itemStack.getType());

        if (itemWorth <= 0) {
            return FeedResponse.CANNOT_FEED;
        }

        final float currentFoodLevel = this.foodData.getFoodLevel();

        final float maxFood = this.upgrades.getMaxFood();

        if (currentFoodLevel >= maxFood) {
            return FeedResponse.ALREADY_FULLY_FED;
        }

        final float setAmount = itemWorth + currentFoodLevel;

        if (setAmount >= maxFood) {
            this.foodData.setFood(maxFood);
            return FeedResponse.FED_TO_FULL;
        }

        this.foodData.setFood(setAmount);
        return FeedResponse.FED;
    }
}
