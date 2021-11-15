package io.github.fisher2911.minionsplugin.config.serializer;

import io.github.fisher2911.fishcore.config.serializer.ItemSerializer;
import io.github.fisher2911.fishcore.configurate.ConfigurationNode;
import io.github.fisher2911.fishcore.configurate.serialize.SerializationException;
import io.github.fisher2911.fishcore.configurate.serialize.TypeSerializer;
import io.github.fisher2911.minionsplugin.gui.item.TypeItem;
import io.github.fisher2911.minionsplugin.permission.MinionPermission;
import org.bukkit.inventory.ItemStack;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.lang.reflect.Type;
import java.util.Arrays;

public class PermissionSerializer implements TypeSerializer<MinionPermission> {

    public static final PermissionSerializer INSTANCE = new PermissionSerializer();

    private static final String ID = "id";
    private static final String DISPLAY_NAME = "display-name";
    private static final String DISPLAY_ITEM = "display-item";

    private ConfigurationNode nonVirtualNode(final ConfigurationNode source, final Object... path) throws SerializationException {
        if (!source.hasChild(path)) {
            throw new SerializationException("Required field " + Arrays.toString(path) + " was not present in node");
        }
        return source.node(path);
    }

    @Override
    public MinionPermission deserialize(final Type type, final ConfigurationNode source) throws SerializationException {
        final var idNode = this.nonVirtualNode(source, ID);
        final var displayNameNode = this.nonVirtualNode(source, DISPLAY_NAME);
        final var displayItemNode = this.nonVirtualNode(source, DISPLAY_ITEM);

        final String id = idNode.getString();
        final String displayName = displayNameNode.getString();
        final TypeItem displayItem =
                new TypeItem(
                        TypeItem.Types.PERMISSION,
                        id,
                        ItemSerializer.INSTANCE.deserialize(ItemStack.class, displayItemNode)
                );

        return new MinionPermission(
                id,
                displayName,
                displayItem
        );
    }

    @Override
    public void serialize(final Type type, @Nullable final MinionPermission obj, final ConfigurationNode node) throws SerializationException {

    }
}
