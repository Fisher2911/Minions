package io.github.fisher2911.minionsplugin.lang;

import io.github.fisher2911.fishcore.upgrade.Upgrade;
import io.github.fisher2911.fishcore.util.helper.Utils;
import io.github.fisher2911.fishcore.world.Position;
import io.github.fisher2911.minionsplugin.minion.data.MinionData;
import io.github.fisher2911.minionsplugin.minion.types.BaseMinion;
import io.github.fisher2911.minionsplugin.upgrade.UpgradeData;
import io.github.fisher2911.minionsplugin.upgrade.type.UpgradeType;
import io.github.fisher2911.minionsplugin.upgrade.Upgrades;
import io.github.fisher2911.minionsplugin.world.Range;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class Placeholder {

    public static final String NAME = "%name%";
    public static final String ID = "%id%";
    public static final String OWNER_NAME = "%owner_name%";
    public static final String OWNER_UUID = "%owner_uuid%";
    public static final String SPEED = "%speed%";
    public static final String RANGE_X = "%x_range%";
    public static final String RANGE_Y = "%y_range";
    public static final String RANGE_Z = "%z_range%";
    public static final String FOOD_LEVEL = "%food_level%";
    public static final String MAX_FOOD = "%max_food&";
    public static final String FOOD_COST_PER_ACTION = "%food_cost_per_action%";
    public static final String POSITION_X = "%position_x%";
    public static final String POSITION_Y = "%position_y%";
    public static final String POSITION_Z = "%position_z%";
    public static final String POSITION_WORLD = "%position_world%";
    public static final String LEVEL = "%level%";
    public static final String MONEY_COST = "%money_cost%";
    public static final String UPGRADE_TYPE = "%upgrade_type%";
    public static final String UPGRADE_LEVEL = "%" + UPGRADE_TYPE + "_LEVEL%";
    public static final String UPGRADE_NAME = "%" + UPGRADE_TYPE + "_NAME%";
    public static final String UPGRADE_ID = "%" + UPGRADE_TYPE + "_ID%";

    public static Map<String, String> getMinionPlaceholders(
            final BaseMinion<?> minion) {

        final MinionData minionData = minion.getMinionData();
        final Upgrades upgrades = minion.getUpgrades();
        final Range range = upgrades.getRange();
        final double rangeX = range.minX() + range.maxX();
        final double rangeY = range.minY() + range.maxY();
        final double rangeZ = range.minZ() + range.maxZ();

        final Position position = minion.getPosition();

        String world = "Not Found";

        final Optional<World> worldOptional = position.getWorld();

        if (worldOptional.isPresent()) {
            world = worldOptional.get().getName();
        }

        final UUID ownerUUID = minion.getOwner();

        final OfflinePlayer owner = Bukkit.getOfflinePlayer(ownerUUID);

        final Map<String, String> placeholders = new HashMap<>();
        placeholders.put(NAME, minionData.getName());
        placeholders.put(ID, String.valueOf(minion.getId()));
        placeholders.put(OWNER_NAME, Utils.replaceIfNull(owner.getName(), "Not Found"));
        placeholders.put(OWNER_UUID, ownerUUID.toString());
        placeholders.put(SPEED, String.valueOf(upgrades.getSpeed()));
        placeholders.put(RANGE_X, String.valueOf(rangeX));
        placeholders.put(RANGE_Y, String.valueOf(rangeY));
        placeholders.put(RANGE_Z, String.valueOf(rangeZ));
        placeholders.put(FOOD_LEVEL, String.valueOf(minionData.getFoodData().getFoodLevel()));
        placeholders.put(MAX_FOOD, String.valueOf(upgrades.getMaxFood()));
        placeholders.put(FOOD_COST_PER_ACTION, String.valueOf(upgrades.getFoodPerAction()));
        placeholders.put(POSITION_X, String.valueOf(position.getBlockX()));
        placeholders.put(POSITION_Y, String.valueOf(position.getBlockY()));
        placeholders.put(POSITION_Z, String.valueOf(position.getBlockZ()));
        placeholders.put(POSITION_WORLD, world);

        for (final UpgradeType upgradeType : UpgradeType.values) {

            final String upgradeTypeString = upgradeType.toString();

            final UpgradeData<?, ?> upgradeData = upgrades.getUpgradeData(upgradeType);

            final String replaceLevelString = UPGRADE_LEVEL.replace(
                    UPGRADE_TYPE, upgradeTypeString
            );

            final String replaceNameString = UPGRADE_NAME.replace(
                    UPGRADE_TYPE, upgradeTypeString
            );

            final String replaceIdString = UPGRADE_ID.replace(
                    UPGRADE_TYPE, upgradeTypeString
            );

            final Upgrade<?> upgrade = upgradeData.getUpgrade();

            placeholders.put(replaceLevelString, String.valueOf(upgradeData.getLevel()));
            placeholders.put(replaceNameString, upgrade.getDisplayName());
            placeholders.put(replaceIdString, upgrade.getId());
        }

        return placeholders;
    }
}
