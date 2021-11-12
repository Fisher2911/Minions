package io.github.fisher2911.minionsplugin.minion.data;

import io.github.fisher2911.minionsplugin.minion.MinionInventory;
import io.github.fisher2911.minionsplugin.minion.MinionType;
import io.github.fisher2911.minionsplugin.minion.food.FeedResponse;
import io.github.fisher2911.minionsplugin.minion.food.FoodData;
import io.github.fisher2911.minionsplugin.permission.MinionPermissionsGroup;
import io.github.fisher2911.minionsplugin.upgrade.Upgrades;
import org.bukkit.inventory.ItemStack;

import java.time.Instant;
import java.util.UUID;

public class MinionData {

    protected final long id;
    private final MinionClass minionClass;
    protected final UUID owner;
    protected MinionType minionType;
    private final MinionPermissionsGroup minionPermissionsGroup;
    private final MinionInventory inventory;
    private final FoodData foodData;
    private final Upgrades upgrades;
    private String name;
    private final Instant lastActionTime;

    public MinionData(
            final long id,
            final UUID owner,
            final MinionClass minionClass,
            final MinionType minionType,
            final MinionPermissionsGroup minionPermissionsGroup,
            final MinionInventory inventory,
            final FoodData foodData,
            final Upgrades upgrades,
            final String name,
            final Instant lastActionTime) {
        this.id = id;
        this.minionClass = minionClass;
        this.owner = owner;
        this.minionType = minionType;
        this.minionPermissionsGroup = minionPermissionsGroup;
        this.inventory = inventory;
        this.foodData = foodData;
        this.upgrades = upgrades;
        this.name = name;
        this.lastActionTime = lastActionTime;
    }

    public MinionPermissionsGroup getMinionPermissionsGroup() {
        return this.minionPermissionsGroup;
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

    public long getId() {
        return this.id;
    }

    public MinionClass getMinionClass() {
        return this.minionClass;
    }

    public UUID getOwner() {
        return this.owner;
    }

    public MinionType getMinionType() {
        return this.minionType;
    }

    public Instant getLastActionTime() {
        return this.lastActionTime;
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
