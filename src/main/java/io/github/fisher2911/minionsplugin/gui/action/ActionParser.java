package io.github.fisher2911.minionsplugin.gui.action;

import com.google.common.collect.ListMultimap;
import com.google.common.collect.Multimaps;
import io.github.fisher2911.minionsplugin.MinionsPlugin;
import io.github.fisher2911.minionsplugin.gui.BaseMinionGui;
import io.github.fisher2911.minionsplugin.gui.GuiManager;
import io.github.fisher2911.minionsplugin.gui.item.TypeItem;
import io.github.fisher2911.minionsplugin.lang.Placeholder;
import io.github.fisher2911.minionsplugin.minion.data.MinionData;
import io.github.fisher2911.minionsplugin.permission.MinionPermissionsGroup;
import io.github.fisher2911.minionsplugin.upgrade.Upgrades;
import io.github.fisher2911.minionsplugin.upgrade.type.UpgradeType;
import io.github.fisher2911.minionsplugin.user.MinionUser;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.function.Function;

public class ActionParser {

    private ActionParser() {
    }

    private static final MinionsPlugin plugin;

    static {
        plugin = MinionsPlugin.getPlugin(MinionsPlugin.class);
    }

    // opens a menu
    public static final String OPEN_MENU = "open";
    // used for upgrading a minion
    public static final String ATTEMPT_UPGRADE = "attempt-upgrade";
    // used for things like permissions, changing true to false
    public static final String SWAP_VALUE = "swap-value";
    // used for things like permissions, adding players
    public static final String ADD_VALUE = "add-value";
    // used to add a player to a permission group
    public static final String ADD_TO_PERMISSION_GROUP = "add-to-permission-group";
    // used to remove a player from a permission group
    public static final String REMOVE_FROM_PERMISSION_GROUP = "remove-from-permission-group";
    // used to add items, such as upgrade items
    public static final String ADD_ITEMS = "add-items";
    // used to set items in the gui
    public static final String SET_ITEMS = "set-items";
    // used for extra data when opening guis
    public static final String EXTRA_DATA = "extra-data";
    // used to end instructions
    public static final String END = "end";

    private static final Set<String> keyWords = Set.of(
            OPEN_MENU,
            ATTEMPT_UPGRADE,
            SWAP_VALUE,
            ADD_VALUE,
            ADD_ITEMS,
            ADD_TO_PERMISSION_GROUP,
            REMOVE_FROM_PERMISSION_GROUP,
            SET_ITEMS,
            END
    );

    public static ClickActions create(
            final String type,
            final List<String> instructions,
            final Set<ClickType> clickTypes) {

        if (instructions.isEmpty()) {
            return ClickActions.none();
        }

        return parseInstructions(type, instructions, clickTypes);
    }

    private static ClickActions parseInstructions(
            String previousInstruction,
            final List<String> instructions,
            final Set<ClickType> clickTypes) {
        final var instructionsMap = getInstructions(previousInstruction, instructions);
        return translateInstructions(
                instructionsMap,
                clickTypes
        );
    }

    private static ClickActions translateInstructions(
            final ListMultimap<String, String> instructionsMap,
            final Set<ClickType> clickTypes) {

        final ClickActions clickActions = new ClickActions();

        for (final var entry : instructionsMap.asMap().entrySet()) {

            Bukkit.getLogger().info("Loading action: " + entry.getKey() + " : " +
                    entry.getValue());

            final String keyWord = entry.getKey();
            final List<String> instructions = (List<String>) entry.getValue();
            switch (keyWord.toLowerCase()) {
                case OPEN_MENU -> {
                    addAllClickActions(
                            clickActions,
                            instructions,
                            instruction -> createOpenMenuAction(
                                    instruction,
                                    instructions,
                                    clickTypes
                            )
                    );
                }

                case ATTEMPT_UPGRADE -> addAllClickActions(
                        clickActions,
                        instructions,
                        instruction -> {

                            try {
                                final UpgradeType upgradeType = UpgradeType.valueOf(instruction);

                                return createAttemptUpgradeAction(
                                        upgradeType, clickTypes
                                );
                            } catch (final IllegalArgumentException ignored) {
                                return ClickAction.none();
                            }
                        }
                );

                case SWAP_VALUE -> {
                    addAllClickActions(
                            clickActions,
                            instructions,
                            instruction ->
                                    createSwapValueAction(instructions, clickTypes)
                    );
                }

                case ADD_TO_PERMISSION_GROUP -> addAllClickActions(clickActions,
                        instructions,
                        instruction ->
                                createAddToMinionGroupValueAction(
                                        instructions,
                                        clickTypes
                                ));

                case REMOVE_FROM_PERMISSION_GROUP -> addAllClickActions(clickActions,
                        instructions,
                        instruction ->
                                createRemoveFromPermissionGroupAction(
                                        instructions,
                                        clickTypes
                                ));
            }
        }

        return clickActions;
    }

    private static void addAllClickActions(
            final ClickActions clickActions,
            final List<String> instructions,
            final Function<String, ClickAction> clickActionFunction) {

        instructions.forEach(instruction ->
                clickActions.addClickAction(
                        clickActionFunction.apply(instruction)
                )
        );
    }

    private static ListMultimap<String, String> getInstructions(
            String previousInstruction,
            final List<String> instructions) {
        final ListMultimap<String, String> instructionsMap = Multimaps.
                newListMultimap(new HashMap<>(), ArrayList::new);

        for (final String instruction : instructions) {
            if (keyWords.contains(instruction)) {
                previousInstruction = instruction;
                continue;
            }

            instructionsMap.put(previousInstruction, instruction);
        }

        return instructionsMap;
    }

    private static ClickAction createOpenMenuAction(
            final String guiName,
            final List<String> instructions,
            final Set<ClickType> clickTypes) {
        final GuiManager guiManager = plugin.getGuiManager();
        return new ClickAction(clickTypes, (menu, slot) -> {
            final MinionUser minionUser = menu.getGuiOwner();

            String extraData = "";

            int index = 0;


            for (final String instruction : instructions) {

                if (instruction.equals(EXTRA_DATA)) {
                    if (instructions.size() <= index + 1) {
                        break;
                    }
                    extraData = instructions.get(index + 1);

                    if (extraData.equals(Placeholder.THIS)) {
                        extraData = menu.getExtraData();
                    }

                    if (extraData.equals(Placeholder.CLICKED)) {
                        final TypeItem typeItem = menu.getItemAtSlot(slot, true);
                        if (typeItem == null) {
                            continue;
                        }
                        extraData = typeItem.getValue() == null ? "" : typeItem.getValue();
                    }

                    break;
                }
                index++;
            }

            final String finalExtraData = extraData;

            minionUser.ifOnline(player -> {

                if (guiName.equals(BaseMinionGui.PREVIOUS_PAGE)) {
                    menu.openPreviousPage(player);
                    return;
                }

                if (guiName.equals(BaseMinionGui.NEXT_PAGE)) {
                    menu.openNextPage(player);
                    return;
                }

                guiManager.openMinionGui(guiName, minionUser, menu.getMinion(), finalExtraData);
            });
        });
    }

    private static ClickAction createSetGuiItemsAction(
            final List<String> instructions,
            final Set<ClickType> clickTypes) {
        return new ClickAction(clickTypes, (menu, slot) -> {
        });
    }

    private static ClickAction createAttemptUpgradeAction(
            final UpgradeType upgradeType,
            final Set<ClickType> clickTypes) {

        return new ClickAction(
                clickTypes, (menu, slot) -> {

            final TypeItem typeItem = menu.getGuiData().getTypeItem(slot);

            if (typeItem == null) {
                return;
            }

            try {
                final String stringType = typeItem.getType();
                final String value = typeItem.getValue();

                if (stringType == null || value == null) {
                    return;
                }

                final UpgradeType type = UpgradeType.valueOf(value);

                if (!upgradeType.equals(type)) {
                    return;
                }

                final Upgrades upgrades = menu.getMinion().getUpgrades();

                upgrades.attemptUpgrade(type, menu.getGuiOwner());
                menu.updateItems();
            } catch (final IllegalArgumentException ignored) {
            }
        });
    }

    private static ClickAction createSwapValueAction(
            final List<String> instructions,
            final Set<ClickType> clickTypes) {

        return new ClickAction(clickTypes, (menu, slot) -> {
            for (final String instruction : instructions) {
                if (instruction.equals(Placeholder.CLICKED)) {
                    final TypeItem clicked = menu.getItemAtSlot(slot, true);

                    if (clicked == null || clicked.getType() == null) {
                        continue;
                    }

                    if (clicked.getType().equals(TypeItem.Types.PERMISSION)) {
                        final String permissionId = clicked.getValue();

                        if (permissionId == null) {
                            continue;
                        }

                        final String permissionGroup = menu.getExtraData();

                        final MinionData minionData = menu.getMinion().getMinionData();

                        final MinionPermissionsGroup group = minionData.getMinionPermissionsGroup(permissionGroup);

                        if (group == null) {
                            continue;
                        }

                        group.getMinionPermissions().swapPermissionValue(permissionId);
                        menu.updateItems();
                    }
                }
            }
        });
    }

    private static ClickAction createAddToMinionGroupValueAction(
            final List<String> instructions,
            final Set<ClickType> clickTypes) {

        return new ClickAction(
                clickTypes,
                (menu, slot) -> {

                    final TypeItem clicked = menu.getItemAtSlot(slot, true);

                    final MinionPermissionsGroup group = getPermissionGroup(clicked, menu);

                    if (group == null) {
                        return;
                    }

                    final Player player = Bukkit.getPlayer(UUID.fromString(clicked.getValue()));

                    if (player == null) {
                        return;
                    }

                    if (group.hasMember(player.getUniqueId())) {
                        menu.getGuiOwner().ifOnline(p -> p.sendMessage("Already in group"));
                    } else {
                        group.addMember(player.getUniqueId());
                        menu.getGuiOwner().ifOnline(p -> p.sendMessage("Added to group"));
                    }

                    menu.updateItems();
                }
        );
    }

    private static ClickAction createRemoveFromPermissionGroupAction(
            final List<String> instructions,
            final Set<ClickType> clickTypes) {
        return new ClickAction(
                clickTypes,
                (menu, slot) -> {
                    final TypeItem clicked = menu.getItemAtSlot(slot, true);

                    final MinionPermissionsGroup group = getPermissionGroup(
                            clicked,
                            menu
                    );

                    if (group == null) {
                        return;
                    }

                    final Player player = Bukkit.getPlayer(UUID.fromString(clicked.getValue()));

                    if (player == null) {
                        return;
                    }

                    if (group.hasMember(player.getUniqueId())) {
                        menu.getGuiOwner().ifOnline(p -> p.sendMessage("Removed from group"));
                        group.removeMember(player.getUniqueId());
                    } else {
                        menu.getGuiOwner().ifOnline(p -> p.sendMessage("Not in group"));
                    }

                    menu.updateItems();
                }
        );
    }

    @Nullable
    private static MinionPermissionsGroup getPermissionGroup(
            final @Nullable TypeItem clicked,
            final BaseMinionGui<?> menu
            ) {
        final String extraData = menu.getExtraData();

        if (extraData == null) {
            return null;
        }

        if (clicked == null) {
            return null;
        }

        final String permissionGroup = menu.getExtraData();

        if (permissionGroup == null) {
            return null;
        }

        final String type = clicked.getType();

        final String value = clicked.getValue();

        if (type == null || !type.equals(TypeItem.Types.PLAYER_UUID) ||
                value == null) {
            return null;
        }

        final MinionPermissionsGroup group = menu.
                getMinion().
                getMinionData().
                getMinionPermissionsGroup(extraData);

        return group;
    }

}
