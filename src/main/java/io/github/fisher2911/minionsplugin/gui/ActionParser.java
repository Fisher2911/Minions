package io.github.fisher2911.minionsplugin.gui;

import io.github.fisher2911.minionsplugin.MinionsPlugin;
import io.github.fisher2911.minionsplugin.user.MinionUser;
import org.bukkit.event.inventory.ClickType;

import java.util.List;
import java.util.Locale;
import java.util.Set;

public class ActionParser {

    private static final MinionsPlugin plugin;

    static {
        plugin = MinionsPlugin.getPlugin(MinionsPlugin.class);
    }

    // opens a menu
    public static final String OPEN_MENU = "open";
    // used for upgrading a minion
    public static final String UPGRADE = "upgrade";
    // used for things like permissions, changing true to false
    public static final String CHANGE_VALUE = "change-value";
    // used for things like permissions, adding players
    public static final String ADD_VALUE = "add-value";

    public static ClickAction create(final String type, final List<String> instructions, final Set<ClickType> clickTypes) {

        if (instructions.isEmpty()) {
            return ClickAction.none();
        }

        return switch (type.toLowerCase(Locale.ROOT)) {
            case OPEN_MENU -> createOpenMenuAction(instructions, clickTypes);
            default -> ClickAction.none();
        };
    }

    private static ClickAction createOpenMenuAction(
            final List<String> instructions,
            final Set<ClickType> clickTypes) {
        final GuiManager guiManager = plugin.getGuiManager();
        final String menuToOpen = instructions.get(0);
        return new ClickAction(clickTypes, menu -> {
            final MinionUser minionUser = menu.guiOwner;
            minionUser.ifOnline(player -> guiManager.openMinionGui(menuToOpen, minionUser, menu.getMinion()));
        });
    }

    private static ClickAction createUpgradeAction(
            final List<String> instructions,
            final Set<ClickType> clickTypes) {
        return ClickAction.none();
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
