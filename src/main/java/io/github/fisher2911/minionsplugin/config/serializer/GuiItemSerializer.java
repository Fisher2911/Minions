package io.github.fisher2911.minionsplugin.config.serializer;

import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.guis.GuiItem;
import io.github.fisher2911.fishcore.config.serializer.ItemSerializer;
import io.github.fisher2911.minionsplugin.gui.BaseMinionGui;
import io.github.fisher2911.minionsplugin.gui.GuiManager;
import org.bukkit.inventory.ItemStack;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;
import org.spongepowered.configurate.serialize.TypeSerializer;

import java.lang.reflect.Type;
import java.util.Arrays;

public class GuiItemSerializer implements TypeSerializer<GuiItem> {

    public static final GuiItemSerializer INSTANCE = new GuiItemSerializer();

    private static final String ITEM = "item";
    private static final String ACTIONS = "actions";

    private ConfigurationNode nonVirtualNode(final ConfigurationNode source, final Object... path) throws SerializationException {
        if (!source.hasChild(path)) {
            throw new SerializationException("Required field " + Arrays.toString(path) + " was not present in node");
        }
        return source.node(path);
    }

    @Override
    public GuiItem deserialize(final Type type, final ConfigurationNode source) throws SerializationException {
        final ConfigurationNode itemNode = this.nonVirtualNode(source, ITEM);
        final ConfigurationNode actionsNode = source.node(ACTIONS, "open");

        final ItemStack itemStack = ItemSerializer.INSTANCE.deserialize(ItemStack.class, itemNode);

        final String open = actionsNode.getString();

        return ItemBuilder.from(itemStack).asGuiItem(event -> {
            if (open == null) {
                return;
            }
            final BaseMinionGui<?> gui = GuiManager.getGui(open);
            if (gui != null) {
                gui.create().open(event.getWhoClicked());
            }
        });
    }

    @Override
    public void serialize(final Type type, @Nullable final GuiItem obj, final ConfigurationNode node) throws SerializationException {

    }
}
