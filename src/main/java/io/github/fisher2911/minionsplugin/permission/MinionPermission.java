package io.github.fisher2911.minionsplugin.permission;

import io.github.fisher2911.fishcore.util.helper.IdHolder;
import io.github.fisher2911.minionsplugin.gui.item.TypeItem;
import io.github.fisher2911.minionsplugin.util.Displayable;

public class MinionPermission implements IdHolder<String>, Displayable {

    public static final String PLACE_MINION = "place-minion";
    public static final String PICKUP_MINION = "pickup-minion";
    public static final String OPEN_MINION_MENU = "open-minion-menu";
    public static final String UPGRADE_MINION = "upgrade-minion";
    
    private final String id;
    private final String displayName;
    private final TypeItem displayItem;

    public MinionPermission(
            final String id,
            final String displayName,
            final TypeItem displayItem) {
        this.id = id;
        this.displayName = displayName;
        this.displayItem = displayItem;
    }

    @Override
    public String getId() {
        return this.id;
    }

    public String getDisplayName() {
        return this.displayName;
    }

    @Override
    public TypeItem getDisplayItem() {
        return this.displayItem;
    }
}
