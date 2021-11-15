package io.github.fisher2911.minionsplugin.config.serializer;

import io.github.fisher2911.fishcore.configurate.ConfigurationNode;
import io.github.fisher2911.fishcore.configurate.serialize.SerializationException;
import io.github.fisher2911.fishcore.configurate.serialize.TypeSerializer;
import io.github.fisher2911.minionsplugin.MinionsPlugin;
import io.github.fisher2911.minionsplugin.minion.data.BaseMinionData;
import io.github.fisher2911.minionsplugin.minion.data.MinionClass;
import io.github.fisher2911.minionsplugin.permission.MinionPermissionsGroup;
import io.github.fisher2911.minionsplugin.permission.PermissionManager;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MinionDataSerializer implements TypeSerializer<BaseMinionData> {

    private static final MinionsPlugin plugin;

    static {
        plugin = MinionsPlugin.getPlugin(MinionsPlugin.class);
    }

    private static final String NAME_ID = "id";
    private static final String DISPLAY_NAME = "display-name";
    private static final String UPGRADE_GROUP = "upgrades";
    private static final String MINION_CLASS = "class";
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
        final var equipmentNode = this.nonVirtualNode(source, EQUIPMENT);
        final var foodGroupNode = this.nonVirtualNode(source, FOOD_GROUP);

        final List<MinionPermissionsGroup> permissionsGroups = new ArrayList<>();

        final String id = idNode.getString();
        final String displayName = displayNameNode.getString();
        final String upgradesGroup = upgradeGroupNode.getString();
        final String minionClassString = minionClassNode.getString();
        permissionsGroups.add(PermissionManager.getInstance().getDefaultGroup());
        final String equipment = equipmentNode.getString();
        final String foodGroupId = foodGroupNode.getString();

        try {
            return new BaseMinionData(
                    id,
                    MinionClass.valueOf(minionClassString.toUpperCase()),
                    permissionsGroups,
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
