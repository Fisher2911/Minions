package io.github.fisher2911.minionsplugin.config.serializer;

import dev.triumphteam.gui.guis.BaseGui;
import dev.triumphteam.gui.guis.GuiItem;
import io.github.fisher2911.fishcore.config.serializer.ItemSerializer;
import io.github.fisher2911.fishcore.configurate.ConfigurationNode;
import io.github.fisher2911.fishcore.configurate.serialize.SerializationException;
import io.github.fisher2911.fishcore.configurate.serialize.TypeSerializer;
import io.github.fisher2911.fishcore.util.helper.StringUtils;
import io.github.fisher2911.fishcore.util.helper.Utils;
import io.github.fisher2911.minionsplugin.gui.ActionParser;
import io.github.fisher2911.minionsplugin.gui.ClickAction;
import io.github.fisher2911.minionsplugin.gui.GuiData;
import org.bukkit.Material;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class GuiDataSerializer<T extends BaseGui> implements TypeSerializer<GuiData> {

    public static final GuiDataSerializer<?> INSTANCE = new GuiDataSerializer<>();

    private GuiDataSerializer() {
    }

    private static final String TITLE = "title";
    public static final String ROWS = "rows";
    public static final String BORDER_ITEMS = "border-items";
    public static final String ITEMS = "items";
    public static final String ACTIONS = "actions";
    public static final String INSTRUCTIONS = "instructions";
    public static final String CLICK_TYPES = "click-types";

    private ConfigurationNode nonVirtualNode(final ConfigurationNode source, final Object... path) throws SerializationException {
        if (!source.hasChild(path)) {
            throw new SerializationException("Required field " + Arrays.toString(path) + " was not present in node");
        }
        return source.node(path);
    }

    @Override
    public GuiData deserialize(final Type type, final ConfigurationNode source) throws SerializationException {
        final ConfigurationNode titleNode = this.nonVirtualNode(source, TITLE);
        final ConfigurationNode rowsNode = this.nonVirtualNode(source, ROWS);
        final ConfigurationNode borderItemsNode = source.node(BORDER_ITEMS);
        final ConfigurationNode itemsNode = source.node(ITEMS);
        final ConfigurationNode actionsNode = source.node(ACTIONS);

        final String title = Utils.replaceIfNull(
                StringUtils.parseStringToString(
                        titleNode.getString()
                ),
                "");
        final int rows = rowsNode.getInt();

        final List<GuiItem> borderItems = borderItemsNode.
                childrenMap().
                values().
                stream().
                map(node -> {
                    try {
                        return new GuiItem(Utils.replaceIfNull(
                                ItemSerializer.
                                        INSTANCE.
                                        deserialize(ItemStack.class, node),
                                new ItemStack(Material.AIR)
                        ));
                    } catch (final SerializationException exception) {
                        exception.printStackTrace();
                        return new GuiItem(Material.AIR);
                    }
                }).
                collect(Collectors.toList());

        final Map<Integer, GuiItem> items =
                itemsNode.
                        childrenMap().
                        entrySet().
                        stream().
                        collect(Collectors.toMap(entry -> {
                            if (entry.getKey() instanceof final Integer slot) {
                                return slot;
                            }
                            return 0;
                        }, entry -> {
                            try {
                                ItemStack itemStack = ItemSerializer.
                                        INSTANCE.
                                        deserialize(ItemStack.class, entry.getValue());

                                if (itemStack == null) {
                                    itemStack = new ItemStack(Material.AIR);
                                }

                                return new GuiItem(itemStack);
                            } catch (final SerializationException exception) {
                                exception.printStackTrace();
                                return new GuiItem(new ItemStack(Material.AIR));
                            }
                        }));


        final Map<Integer, ClickAction> clickActionMap = new HashMap<>();

        final var childrenMap = actionsNode.childrenMap();

        for (final var entry : childrenMap.entrySet()) {
            if (!(entry.getKey() instanceof final Integer slot)) {
                continue;
            }

            final Set<ClickType> clickTypes = new HashSet<>();

            final var clickTypesNode = actionsNode.
                    node(slot).
                    node(CLICK_TYPES);

            final List<String> clickTypesList = Utils.replaceIfNull(
                    clickTypesNode.getList(String.class),
                    new ArrayList<>());

            for (final String clickType : clickTypesList) {
                try {
                    clickTypes.add(ClickType.valueOf(clickType));
                } catch (final IllegalArgumentException exception) {
                    clickTypes.add(ClickType.UNKNOWN);
                }
            }

            final var instructionsNode = actionsNode.
                    node(slot).
                    node(INSTRUCTIONS);

            final var actionChildrenMap = instructionsNode.childrenMap();

            for (final var actionEntry : actionChildrenMap.entrySet()) {
                if (!(actionEntry.getKey() instanceof final String action)) {
                    continue;
                }

                final List<String> instructions = Utils.replaceIfNull(
                        actionEntry.getValue().getList(String.class),
                        new ArrayList<>()
                );

                final ClickAction clickAction = ActionParser.create(action, instructions, clickTypes);
                clickActionMap.put(slot, clickAction);
            }

        }
        return new GuiData(title, rows, borderItems, items, clickActionMap);
    }

    @Override
    public void serialize(
            final Type type,
            @Nullable final GuiData guiData,
            final ConfigurationNode node) throws SerializationException {

    }
}
