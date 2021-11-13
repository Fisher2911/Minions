package io.github.fisher2911.minionsplugin.minion.data;

import io.github.fisher2911.fishcore.util.helper.IdHolder;
import io.github.fisher2911.minionsplugin.minion.MinionType;
import io.github.fisher2911.minionsplugin.minion.food.FeedResponse;
import io.github.fisher2911.minionsplugin.minion.food.FoodData;
import io.github.fisher2911.minionsplugin.minion.inventory.Equipment;
import io.github.fisher2911.minionsplugin.permission.MinionPermissionsGroup;
import io.github.fisher2911.minionsplugin.upgrade.Upgrades;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Unmodifiable;

import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class MinionData implements IdHolder<Long> {

    private final long id;
    private final String namedId;
    private final MinionClass minionClass;
    private UUID owner;
    private final List<MinionPermissionsGroup> minionPermissionsGroups;
    private final Equipment equipment;
    private final FoodData foodData;
    private final Upgrades upgrades;
    private String name;
    private Instant lastActionTime;

    public MinionData(
            final long id,
            final String namedId,
            final UUID owner,
            final MinionClass minionClass,
            final List<MinionPermissionsGroup> minionPermissionsGroups,
            final Equipment equipment,
            final FoodData foodData,
            final Upgrades upgrades,
            final String name,
            final Instant lastActionTime) {
        this.id = id;
        this.namedId = namedId;
        this.minionClass = minionClass;
        this.owner = owner;
        this.minionPermissionsGroups = minionPermissionsGroups;
        Collections.sort(this.minionPermissionsGroups);
        this.equipment = equipment;
        this.foodData = foodData;
        this.upgrades = upgrades;
        this.name = name;
        this.lastActionTime = lastActionTime;
    }

    @Unmodifiable
    public List<MinionPermissionsGroup> getMinionPermissionsGroup() {
        return Collections.unmodifiableList(this.minionPermissionsGroups);
    }

    public boolean hasPermission(
            final String permission,
            final UUID uuid) {
        boolean hasPermission = false;
        for (final MinionPermissionsGroup group : this.minionPermissionsGroups) {
            hasPermission = group.hasPermission(uuid, permission);

            if (hasPermission) {
                break;
            }
        }

        return hasPermission;
    }

    public Equipment getArmor() {
        return this.equipment;
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

    @Override
    public Long getId() {
        return this.id;
    }

    public MinionClass getMinionClass() {
        return this.minionClass;
    }

    public UUID getOwner() {
        return this.owner;
    }

    public void setOwner(final UUID owner) {
        this.owner = owner;
    }

    public MinionType getMinionType() {
        return this.minionClass.getMinionType();
    }

    public Instant getLastActionTime() {
        return this.lastActionTime;
    }

    public void setLastActionTime(final Instant lastActionTime) {
        this.lastActionTime = lastActionTime;
    }

    public String getNamedId() {
        return this.namedId;
    }

    public void setArmor(final LivingEntity minion) {
        final EntityEquipment equipment = minion.getEquipment();

        if (equipment == null) {
            return;
        }

        equipment.setItemInMainHand(this.equipment.getMainHand());
        equipment.setItemInOffHand(this.equipment.getOffHand());
        equipment.setBoots(this.equipment.getBoots());
        equipment.setLeggings(this.equipment.getPants());
        equipment.setChestplate(this.equipment.getChestPlate());
        equipment.setHelmet(this.equipment.getHelmet());
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
