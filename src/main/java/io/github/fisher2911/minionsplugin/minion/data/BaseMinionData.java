package io.github.fisher2911.minionsplugin.minion.data;

import io.github.fisher2911.minionsplugin.MinionsPlugin;
import io.github.fisher2911.minionsplugin.minion.inventory.Equipment;
import io.github.fisher2911.minionsplugin.minion.MinionType;
import io.github.fisher2911.minionsplugin.minion.food.FoodData;
import io.github.fisher2911.minionsplugin.minion.food.FoodGroup;
import io.github.fisher2911.minionsplugin.permission.MinionPermissionsGroup;
import io.github.fisher2911.minionsplugin.upgrade.Upgrades;
import org.bukkit.Bukkit;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.time.Instant;
import java.util.UUID;

public class BaseMinionData {

    private final String namedId;
    private final MinionClass minionClass;
    private final String minionPermissionsGroupId;
    private final String equipmentId;
    private final String foodDataId;
    private final String upgradesId;
    private String name;

    public BaseMinionData(
            final String namedId,
            final MinionClass minionClass,
            final String minionPermissionsGroupId,
            final String equipmentId,
            final String foodDataId,
            final String upgradesId,
            final String name) {
        this.namedId = namedId;
        this.minionClass = minionClass;
        this.minionPermissionsGroupId = minionPermissionsGroupId;
        this.equipmentId = equipmentId;
        this.foodDataId = foodDataId;
        this.upgradesId = upgradesId;
        this.name = name;
    }

    public String getMinionPermissionsGroupId() {
        return this.minionPermissionsGroupId;
    }

    public String getEquipmentId() {
        return this.equipmentId;
    }

    public String getName() {
        return this.name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getFoodDataId() {
        return this.foodDataId;
    }

    public String getUpgradesId() {
        return this.upgradesId;
    }

    public MinionClass getMinionClass() {
        return this.minionClass;
    }

    public MinionType getMinionType() {
        return this.minionClass.getMinionType();
    }

    public String getNamedId() {
        return this.namedId;
    }

    @Nullable
    public MinionData toMinionData(
            final long id,
            final UUID owner,
            final float foodLevel,
            final MinionsPlugin plugin) {

        final var upgradesGroupManager = plugin.getUpgradeGroupManager();
        final var permissionsManager = plugin.getPermissionManager();
        final var equipmentManager = plugin.getEquipmentManager();
        final var foodManger = plugin.getFoodManager();

        final var upgradeGroupOptional = upgradesGroupManager.get(this.upgradesId);
        final var permissionGroupOptional = permissionsManager.get(this.minionPermissionsGroupId);
        final var equipmentOptional = equipmentManager.get(this.equipmentId);
        final var foodGroupOptional = foodManger.get(this.foodDataId);

        if (upgradeGroupOptional.isEmpty()) {
            Bukkit.broadcastMessage("Upgrade group missing");
            return null;
        }

        final MinionPermissionsGroup permissionsGroup;

        if (permissionGroupOptional.isEmpty()) {
            permissionsGroup = permissionsManager.getDefaultGroup();
        } else {
            permissionsGroup = permissionGroupOptional.get();
        }

        if (equipmentOptional.isEmpty()) {
            Bukkit.broadcastMessage("Equipment missing");
            return null;
        }

        if (foodGroupOptional.isEmpty()) {
            Bukkit.broadcastMessage("Food group missing");
            return null;
        }

        final Upgrades upgrades = upgradeGroupOptional.get().toUpgrades();
        final Equipment equipment = equipmentOptional.get();
        final FoodGroup foodGroup = foodGroupOptional.get();

        return new MinionData(
                id,
                this.namedId,
                owner,
                this.minionClass,
                permissionsGroup,
                equipment,
                new FoodData(
                        foodGroup,
                        foodLevel),
                upgrades,
                this.name,
                Instant.now()
        );
    }
}