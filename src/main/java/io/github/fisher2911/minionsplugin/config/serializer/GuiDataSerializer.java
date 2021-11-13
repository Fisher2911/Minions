package io.github.fisher2911.minionsplugin.config.serializer;

import dev.triumphteam.gui.guis.GuiItem;
import io.github.fisher2911.fishcore.config.serializer.ItemSerializer;
import io.github.fisher2911.fishcore.configurate.ConfigurationNode;
import io.github.fisher2911.fishcore.configurate.serialize.SerializationException;
import io.github.fisher2911.fishcore.configurate.serialize.TypeSerializer;
import io.github.fisher2911.fishcore.util.helper.StringUtils;
import io.github.fisher2911.fishcore.util.helper.Utils;
import io.github.fisher2911.minionsplugin.gui.ClickActions;
import io.github.fisher2911.minionsplugin.gui.GuiData;
import io.github.fisher2911.minionsplugin.gui.item.TypeItem;
import io.github.fisher2911.minionsplugin.gui.parser.ActionParser;
import org.bukkit.Material;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class GuiDataSerializer implements TypeSerializer<GuiData> {

    public static final GuiDataSerializer INSTANCE = new GuiDataSerializer();

    private GuiDataSerializer() {
    }

    private static final String TITLE = "title";
    private static final String ROWS = "rows";
    private static final String BORDER_ITEMS = "border-items";
    private static final String ITEMS = "items";
    private static final String ACTIONS = "actions";
    private static final String INSTRUCTIONS = "instructions";
    private static final String CLICK_TYPES = "click-types";
    private static final String FILL_ITEMS = "fill-items";

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
        final ConfigurationNode fillNode = source.node(FILL_ITEMS);

        final String title = Utils.replaceIfNull(
                StringUtils.parseStringToString(
                        titleNode.getString()
                ),
                "");

        final int rows = rowsNode.getInt();

        final List<GuiItem> borderItems = this.loadBorderItems(borderItemsNode);

        final Map<Integer, TypeItem> items = this.loadTypeItems(itemsNode);

        final Map<Integer, ClickActions> clickActionMap = this.loadClickActions(actionsNode);

        final Map<String, ClickActions> fillItems = this.loadFillItems(fillNode);

        return new GuiData(
                title,
                rows,
                borderItems,
                items,
                clickActionMap,
                fillItems);
    }

    private List<GuiItem> loadBorderItems(final ConfigurationNode borderItemsNode) {
        return borderItemsNode.
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
    }

    private Map<Integer, TypeItem> loadTypeItems(final ConfigurationNode itemsNode) {
        return itemsNode.
                childrenMap().
                entrySet().
                stream().
                collect(Collectors.toMap(entry -> {
                    if (entry.getKey() instanceof final Integer slot) {
                        return slot;
                    }
                    return 0;
                }, entry -> {

                    final String itemType = entry.getValue().node("type").getString();

                    try {
                        ItemStack itemStack = ItemSerializer.
                                INSTANCE.
                                deserialize(ItemStack.class, entry.getValue());

                        if (itemStack == null) {
                            itemStack = new ItemStack(Material.AIR);
                        }

                        return new TypeItem(itemType, new GuiItem(itemStack));
                    } catch (final SerializationException exception) {
                        exception.printStackTrace();
                        return new TypeItem(itemType, new GuiItem(Material.AIR));
                    }
                }));
    }

    private Map<Integer, ClickActions> loadClickActions(final ConfigurationNode actionsNode) throws SerializationException {

        final Map<Integer, ClickActions> clickActionMap = new HashMap<>();

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

            final ClickActions clickActions = new ClickActions(new ArrayList<>());

            for (final var actionEntry : actionChildrenMap.entrySet()) {
                if (!(actionEntry.getKey() instanceof final String action)) {
                    continue;
                }

                final List<String> instructions = Utils.replaceIfNull(
                        actionEntry.getValue().getList(String.class),
                        new ArrayList<>()
                );

                final ClickActions parsed = ActionParser.create(
                        action,
                        instructions,
                        clickTypes);
                clickActions.addAll(parsed);
            }

            clickActionMap.put(slot, clickActions);
        }

        return clickActionMap;
    }

    private Map<String, ClickActions> loadFillItems(final ConfigurationNode fillNode) throws SerializationException {

        final Map<String, ClickActions> clickActionsMap = new HashMap<>();

        final var childrenMap = fillNode.childrenMap();

        for (final var childrenEntry : childrenMap.entrySet()) {

            final ConfigurationNode fillTypeNode = childrenEntry.getValue();

            final String fillType = fillTypeNode.getString();

            final ConfigurationNode actionsNode = fillTypeNode.node(ACTIONS);

            final List<String> clickTypesStrings = Utils.replaceIfNull(
                    actionsNode.node(CLICK_TYPES).getList(String.class),
                    new ArrayList<>()
            );

            final Set<ClickType> clickTypes = new HashSet<>();

            for (final String clickType : clickTypesStrings) {
                try {
                    clickTypes.add(ClickType.valueOf(clickType));
                } catch (final IllegalArgumentException ignored) {}
            }

            final List<String> instructions = Utils.replaceIfNull(
                    actionsNode.node(INSTRUCTIONS).getList(String.class),
                    new ArrayList<>()
            );

            if (instructions.isEmpty()) {
                continue;
            }

            final ClickActions clickActions = ActionParser.create(
                    instructions.get(0),
                    instructions,
                    clickTypes
            );

            clickActionsMap.put(fillType, clickActions);
        }

        return clickActionsMap;
    }


    @Override
    public void serialize(
            final Type type,
            @Nullable final GuiData guiData,
            final ConfigurationNode node) throws SerializationException {

    }
}
