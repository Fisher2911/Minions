package io.github.fisher2911.minionsplugin.gui.parser;

import com.google.common.collect.ListMultimap;
import com.google.common.collect.Multimaps;
import io.github.fisher2911.minionsplugin.MinionsPlugin;
import io.github.fisher2911.minionsplugin.gui.BaseMinionGui;
import io.github.fisher2911.minionsplugin.gui.ClickAction;
import io.github.fisher2911.minionsplugin.gui.ClickActions;
import io.github.fisher2911.minionsplugin.gui.GuiManager;
import io.github.fisher2911.minionsplugin.gui.item.TypeItem;
import io.github.fisher2911.minionsplugin.lang.Placeholder;
import io.github.fisher2911.minionsplugin.upgrade.Upgrades;
import io.github.fisher2911.minionsplugin.upgrade.type.UpgradeType;
import io.github.fisher2911.minionsplugin.user.MinionUser;
import org.bukkit.Bukkit;
import org.bukkit.event.inventory.ClickType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
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
    public static final String CHANGE_VALUE = "change-value";
    // used for things like permissions, adding players
    public static final String ADD_VALUE = "add-value";
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
            CHANGE_VALUE,
            ADD_VALUE,
            ADD_ITEMS,
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
                    Bukkit.getLogger().info("Open Menu Task");
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

            final MinionUser user = menu.getGuiOwner();
            final TypeItem typeItem = menu.getGuiData().getTypeItem(slot);

            if (typeItem == null) {
                return;
            }


            try {
                final String stringType = typeItem.getType();

                if (stringType == null) {
                    return;
                }

                final UpgradeType type = UpgradeType.valueOf(stringType);

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

    private static ClickAction createChangeValueAction(
            final List<String> instructions,
            final Set<ClickType> clickTypes) {
        return ClickAction.none();
    }

    private static ClickAction createAddValueAction(
            final List<String> instructions,
            final Set<ClickType> clickTypes) {
        return ClickAction.none();
    }

}
