package io.github.fisher2911.minionsplugin.config.serializer;

import io.github.fisher2911.fishcore.configurate.ConfigurationNode;
import io.github.fisher2911.fishcore.configurate.serialize.SerializationException;
import io.github.fisher2911.fishcore.configurate.serialize.TypeSerializer;
import io.github.fisher2911.fishcore.util.builder.LeatherArmorBuilder;
import io.github.fisher2911.minionsplugin.MinionsPlugin;
import io.github.fisher2911.minionsplugin.minion.data.BaseMinionData;
import io.github.fisher2911.minionsplugin.minion.data.MinionClass;
import org.bukkit.Color;
import org.bukkit.Material;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.lang.reflect.Type;
import java.util.Arrays;

public class MinionDataSerializer implements TypeSerializer<BaseMinionData> {

    private static final MinionsPlugin plugin;

    static {
        plugin = MinionsPlugin.getPlugin(MinionsPlugin.class);
    }

    private static final String NAME_ID = "id";
    private static final String DISPLAY_NAME = "display-name";
    private static final String UPGRADE_GROUP = "upgrades";
    private static final String MINION_CLASS = "class";
    private static final String PERMISSION = "permissions";
    private static final String EQUIPMENT = "equipment";
    private static final String FOOD_GROUP = "food-group";

    private MinionDataSerializer() {}

    public static final MinionDataSerializer INSTANCE = new MinionDataSerializer();

    private ConfigurationNode nonVirtualNode(final ConfigurationNode source, final Object... path) throws SerializationException {
        if (!source.hasChild(path)) {
            throw new SerializationException("Required field " + Arrays.toString(path) + " was not present in node");
        }
        return source.node(path);
    }


    @Override
    @Nullable
    public BaseMinionData deserialize(final Type type, final ConfigurationNode source) throws SerializationException {

        final var idNode = this.nonVirtualNode(source, NAME_ID);
        final var displayNameNode = this.nonVirtualNode(source, DISPLAY_NAME);
        final var upgradeGroupNode = this.nonVirtualNode(source, UPGRADE_GROUP);
        final var minionClassNode = this.nonVirtualNode(source, MINION_CLASS);
        final var permissionNode = source.node(PERMISSION);
        final var equipmentNode = this.nonVirtualNode(source, EQUIPMENT);
        final var foodGroupNode = this.nonVirtualNode(source, FOOD_GROUP);

        final String id = idNode.getString();
        final String displayName = displayNameNode.getString();
        final String upgradesGroup = upgradeGroupNode.getString();
        final String minionClassString = minionClassNode.getString();
        final String permissions = permissionNode.getString();
        final String equipment = equipmentNode.getString();
        final String foodGroupId = foodGroupNode.getString();

        try {

            final LeatherArmorBuilder builder =
                    LeatherArmorBuilder.from(Material.LEATHER_BOOTS).
                            color(Color.RED);

            return new BaseMinionData(
                    id,
                    MinionClass.valueOf(minionClassString.toUpperCase()),
                    permissions,
                    equipment,
                    foodGroupId,
                    upgradesGroup,
                    displayName
            );
        } catch (final IllegalArgumentException | NullPointerException exception) {
            plugin.logger().configWarning(minionClassString + " is not a valid minion class.");
        }

        return null;
    }

    @Override
    public void serialize(final Type type, @Nullable final BaseMinionData obj, final ConfigurationNode node) throws SerializationException {

    }
}
