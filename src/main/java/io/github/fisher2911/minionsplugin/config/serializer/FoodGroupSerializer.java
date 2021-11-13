package io.github.fisher2911.minionsplugin.config.serializer;

import io.github.fisher2911.fishcore.configurate.ConfigurationNode;
import io.github.fisher2911.fishcore.configurate.serialize.SerializationException;
import io.github.fisher2911.fishcore.configurate.serialize.TypeSerializer;
import io.github.fisher2911.minionsplugin.MinionsPlugin;
import io.github.fisher2911.minionsplugin.minion.food.FoodGroup;
import org.bukkit.Material;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.lang.reflect.Type;
import java.util.EnumMap;
import java.util.Map;

public class FoodGroupSerializer implements TypeSerializer<FoodGroup> {

    private static final String ID = "id";
    private static final String FOOD = "food";

    private static final MinionsPlugin plugin;

    static {
        plugin = MinionsPlugin.getPlugin(MinionsPlugin.class);
    }

    public static final FoodGroupSerializer INSTANCE = new FoodGroupSerializer();

    private FoodGroupSerializer() {}

    @Override
    public FoodGroup deserialize(final Type type, final ConfigurationNode node) throws SerializationException {

        final ConfigurationNode idNode = node.node(ID);
        final ConfigurationNode foodNode = node.node(FOOD);

        final String id = idNode.getString();

        final Map<Material, Float> foodValues = new EnumMap<>(Material.class);

        for (final var entry : foodNode.childrenMap().entrySet()) {
            if (!(entry.getKey() instanceof final String foodType)) {
                continue;
            }

            final float value = entry.getValue().getFloat();

            try {
                final Material material = Material.valueOf(foodType);
                foodValues.put(material, value);

            } catch (final IllegalArgumentException exception) {
                plugin.logger().configWarning(foodType + " is not a valid material");
            }
        }

        return new FoodGroup(id, foodValues);
    }

    @Override
    public void serialize(final Type type, @Nullable final FoodGroup obj, final ConfigurationNode node) throws SerializationException {

    }
}
