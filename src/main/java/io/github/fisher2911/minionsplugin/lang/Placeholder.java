package io.github.fisher2911.minionsplugin.lang;

import io.github.fisher2911.fishcore.economy.Cost;
import io.github.fisher2911.fishcore.upgrade.Upgrade;
import io.github.fisher2911.fishcore.util.helper.Utils;
import io.github.fisher2911.fishcore.world.Position;
import io.github.fisher2911.minionsplugin.minion.data.MinionData;
import io.github.fisher2911.minionsplugin.minion.types.BaseMinion;
import io.github.fisher2911.minionsplugin.permission.MinionPermissionsGroup;
import io.github.fisher2911.minionsplugin.permission.PermissionManager;
import io.github.fisher2911.minionsplugin.upgrade.UpgradeData;
import io.github.fisher2911.minionsplugin.upgrade.Upgrades;
import io.github.fisher2911.minionsplugin.upgrade.type.UpgradeType;
import io.github.fisher2911.minionsplugin.world.Range;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class Placeholder {

    public static final String NAME = "%name%";
    public static final String PLAYER_NAME = "%player_name%";
    public static final String PLAYER_UUID = "%player_uuid%";
    public static final String ID = "%id%";
    public static final String OWNER_NAME = "%owner_name%";
    public static final String OWNER_UUID = "%owner_uuid%";
    public static final String SPEED = "%speed%";
    public static final String RANGE_X = "%x_range%";
    public static final String RANGE_Y = "%y_range";
    public static final String RANGE_Z = "%z_range%";
    public static final String FOOD_LEVEL = "%food_level%";
    public static final String MAX_FOOD = "%max_food%";
    public static final String FOOD_COST_PER_ACTION = "%food_cost_per_action%";
    public static final String POSITION_X = "%position_x%";
    public static final String POSITION_Y = "%position_y%";
    public static final String POSITION_Z = "%position_z%";
    public static final String POSITION_WORLD = "%position_world%";
    public static final String UPGRADE_TYPE = "%upgrade_type%";
    public static final String UPGRADE_MONEY_COST = "%" + UPGRADE_TYPE + "_money_cost%";
    public static final String UPGRADE_LEVEL = "%" + UPGRADE_TYPE + "_level%";
    public static final String UPGRADE_NAME = "%" + UPGRADE_TYPE + "_name%";
    public static final String UPGRADE_ID = "%" + UPGRADE_TYPE + "_id%";
    public static final String PERMISSION = "%permission_" + ID + "%";
    public static final String IN_PERMISSION_GROUP = "%in_" + ID + "_group_" + PLAYER_UUID + "%";
    public static final String THIS = "%this%";
    public static final String CLICKED = "%clicked%";
    public static final String EXTRA_DATA = "%extra-data%";
    public static final String TYPE = "%type%";
    public static final String VALUE = "%value%";

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

            final String replaceMoneyCostString = UPGRADE_MONEY_COST.replace(
                    UPGRADE_TYPE, upgradeTypeString
            );

            final Upgrade<?> upgrade = upgradeData.getUpgrade();

            final Cost cost = upgradeData.getNextLevelCost();

            final String moneyCost = cost == null ? "Max Level" :
                    String.valueOf(cost.getMoneyCost());

            final String level = cost == null ? "Max Level" :
                    String.valueOf(upgradeData.getLevel());

            placeholders.put(replaceLevelString, level);
            placeholders.put(replaceNameString, upgrade.getDisplayName());
            placeholders.put(replaceIdString, upgrade.getId());
            placeholders.put(replaceMoneyCostString, moneyCost);
        }

        return placeholders;
    }

    public static Map<String, String> getMinionPlaceholders(
            final BaseMinion<?> minion,
            final UUID uuid) {

        final Map<String, String> placeholders = getMinionPlaceholders(minion);

        final OfflinePlayer player = Bukkit.getOfflinePlayer(uuid);

        final String name = player.getName();

        if (name != null) {
            placeholders.put(PLAYER_NAME, player.getName());
        }

        placeholders.put(PLAYER_UUID, player.getUniqueId().toString());

        final MinionData minionData = minion.getMinionData();

        for (final var entry : PermissionManager.getInstance().getAll().entrySet()) {
            final String permission = entry.getKey();

            final String hasPermission =
                    String.valueOf(
                            minionData.hasPermission(permission, uuid)
                    ).toLowerCase(Locale.ROOT);

            placeholders.put(PERMISSION.replace(ID, permission), hasPermission);
        }

        return placeholders;
    }


    public static Map<String, String> getPermissionsPlaceholders(
            final BaseMinion<?> minion,
            final UUID uuid) {

        final Map<String, String> placeholders = new HashMap<>();

        final MinionData minionData = minion.getMinionData();

        final OfflinePlayer player = Bukkit.getOfflinePlayer(uuid);

        final String name = player.getName();

        for (final MinionPermissionsGroup group : minionData.getMinionPermissionsGroups()) {

            final String hasMember = String.valueOf(group.hasMember(uuid));

            placeholders.put(IN_PERMISSION_GROUP.
                            replace(ID, group.getId()).
                            replace(PLAYER_UUID, uuid.toString()),
                    hasMember);
            if (name != null) {
                placeholders.put(IN_PERMISSION_GROUP.
                                replace(ID, group.getId()).
                                replace(PLAYER_UUID, name),
                        hasMember);
            }
        }

        return placeholders;
    }
}
