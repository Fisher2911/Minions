package io.github.fisher2911.minionsplugin.config.serializer;

import io.github.fisher2911.fishcore.config.serializer.ItemSerializer;
import io.github.fisher2911.fishcore.configurate.ConfigurationNode;
import io.github.fisher2911.fishcore.configurate.serialize.SerializationException;
import io.github.fisher2911.fishcore.configurate.serialize.TypeSerializer;
import io.github.fisher2911.fishcore.economy.Cost;
import io.github.fisher2911.minionsplugin.upgrade.UpgradeGroup;
import io.github.fisher2911.minionsplugin.upgrade.type.FloatUpgrade;
import io.github.fisher2911.minionsplugin.upgrade.type.RangeUpgrade;
import io.github.fisher2911.minionsplugin.upgrade.type.UpgradeType;
import io.github.fisher2911.minionsplugin.world.Range;
import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemStack;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UpgradeGroupSerializer implements TypeSerializer<UpgradeGroup> {

    public static final UpgradeGroupSerializer INSTANCE = new UpgradeGroupSerializer();

    private UpgradeGroupSerializer() {}

    private static final String SPEED_UPGRADE = "speed-upgrade";
    private static final String RANGE_UPGRADE = "range-upgrade";
    private static final String FOOD_PER_ACTION_UPGRADE = "food-per-action-upgrade";
    private static final String MAX_FOOD_UPGRADE = "max-food-upgrade";

    private static final String ID = "id";
    private static final String DISPLAY_NAME = "display-name";
    private static final String DISPLAY_ITEM = "display-item";

    private static final String LEVEL_DATA = "level-data";
    private static final String LEVEL_COSTS = "level-costs";

    private static final String MONEY_COST = "money-cost";
    private static final String ITEM_COSTS = "item-costs";

    private static final String MIN_X = "min-x";
    private static final String MIN_Y = "min-y";
    private static final String MIN_Z = "min-z";
    private static final String MAX_X = "min-x";
    private static final String MAX_Y = "min-y";
    private static final String MAX_Z = "min-z";

    private ConfigurationNode nonVirtualNode(final ConfigurationNode source, final Object... path) throws SerializationException {
        if (!source.hasChild(path)) {
            throw new SerializationException("Required field " + Arrays.toString(path) + " was not present in node");
        }
        return source.node(path);
    }

    @Override
    public UpgradeGroup deserialize(final Type type, final ConfigurationNode node) throws SerializationException {

        final String id = node.node(ID).getString();

        final FloatUpgrade speedUpgrade = this.loadFloatUpgrade(
                this.nonVirtualNode(node.node(SPEED_UPGRADE)),
                        UpgradeType.SPEED_UPGRADE);
        final RangeUpgrade rangeUpgrade = this.loadRangeUpgrade(
                this.nonVirtualNode(node.node(RANGE_UPGRADE)),
                UpgradeType.RANGE_UPGRADE
        );
        final FloatUpgrade foodPerActionUpgrade = this.loadFloatUpgrade(
                this.nonVirtualNode(node.node(FOOD_PER_ACTION_UPGRADE)),
                UpgradeType.FOOD_PER_ACTION_UPGRADE);
        final FloatUpgrade maxFoodUpgrade = this.loadFloatUpgrade(
                this.nonVirtualNode(node.node(MAX_FOOD_UPGRADE)),
                UpgradeType.MAX_FOOD_UPGRADE);

        return UpgradeGroup.
                builder().
                id(id).
                setSpeedUpgrade(speedUpgrade).
                setRangeUpgrade(rangeUpgrade).
                setMaxFoodUpgrade(maxFoodUpgrade).
                setFoodPerActionUpgrade(foodPerActionUpgrade).
                build();
    }

    @Override
    public void serialize(final Type type, @Nullable final UpgradeGroup obj, final ConfigurationNode node) throws SerializationException {

    }
    
    private FloatUpgrade loadFloatUpgrade(
            final ConfigurationNode source,
            final UpgradeType upgradeType) throws SerializationException {
        final String id = this.nonVirtualNode(source, ID).getString();
        final String displayName = this.nonVirtualNode(source, DISPLAY_NAME).getString();
        final ItemStack displayItem = ItemSerializer.INSTANCE.deserialize(ItemStack.class,
                source.node(DISPLAY_ITEM));
        
        final var childrenMap = source.node(LEVEL_DATA).childrenMap();

        final Map<Integer, Float> levelDataMap = new HashMap<>();

        for (final var entry : childrenMap.entrySet()) {
            if (!(entry.getKey() instanceof final Integer level)) {
                continue;
            }

            final ConfigurationNode node = entry.getValue();
            final float value = node.getFloat();
            levelDataMap.put(level, value);
        }

        final var costMap = this.loadUpgradeCost(source);

        Bukkit.getLogger().info("[MinionsPlugin] Loaded Upgrade: " + upgradeType);

        return new FloatUpgrade(id, displayName, levelDataMap, costMap, displayItem, upgradeType);
    }

    private RangeUpgrade loadRangeUpgrade(
            final ConfigurationNode source,
            final UpgradeType upgradeType) throws SerializationException {

        final String id = this.nonVirtualNode(source, ID).getString();
        final String displayName = this.nonVirtualNode(source, DISPLAY_NAME).getString();
        final ItemStack displayItem = ItemSerializer.INSTANCE.deserialize(ItemStack.class,
                source.node(DISPLAY_ITEM));

        final var childrenMap = source.node(LEVEL_DATA).childrenMap();

        final Map<Integer, Range> levelDataMap = new HashMap<>();

        for (final var entry : childrenMap.entrySet()) {
            if (!(entry.getKey() instanceof final Integer level)) {
                continue;
            }

            final ConfigurationNode node = entry.getValue();
            final double minX = node.node(MIN_X).getDouble();
            final double minY = node.node(MIN_Y).getDouble();
            final double minZ = node.node(MIN_Z).getDouble();
            final double maxX = node.node(MAX_X).getDouble();
            final double maxY = node.node(MAX_Y).getDouble();
            final double maxZ = node.node(MAX_Z).getDouble();

            final Range range = new Range(
                    minX,
                    minY,
                    minZ,
                    maxX,
                    maxY,
                    maxZ
            );

            levelDataMap.put(level, range);
        }

        final var costMap = this.loadUpgradeCost(source);

        return new RangeUpgrade(id, displayName, levelDataMap, costMap, displayItem, upgradeType);
    }

    private Map<Integer, Cost> loadUpgradeCost(final ConfigurationNode source) throws SerializationException {
        final Map<Integer, Cost> costMap = new HashMap<>();

        final var childrenMap = source.node(LEVEL_COSTS).childrenMap();

        for (final var entry : childrenMap.entrySet()) {
            if (!(entry.getKey() instanceof final Integer level)) {
                continue;
            }

            final ConfigurationNode node = entry.getValue();

            final double moneyCost = node.node(MONEY_COST).getDouble();

            final List<ItemStack> itemStackCosts = new ArrayList<>();

            for (final ConfigurationNode listNode : node.node(ITEM_COSTS).childrenList()) {
                itemStackCosts.add(
                        ItemSerializer.INSTANCE.deserialize(
                                ItemStack.class,
                                listNode
                        )
                );
            }

            costMap.put(level, new Cost(moneyCost, itemStackCosts));
        }
        return costMap;
    }
}
