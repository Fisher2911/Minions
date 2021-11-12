package io.github.fisher2911.minionsplugin.permission;

import io.github.fisher2911.fishcore.util.helper.IdHolder;

public class MinionPermission implements IdHolder<String> {

    public static final String PLACE_MINION = "place-minion";
    public static final String PICKUP_MINION = "pickup-minion";
    public static final String OPEN_MINION_MENU = "open-minion-menu";
    public static final String UPGRADE_MINION = "upgrade-minion";
    
    private final String id;
    private final String displayName;

    public MinionPermission(final String id, final String displayName) {
        this.id = id;
        this.displayName = displayName;
    }

    @Override
    public String getId() {
        return this.id;
    }

    public String getDisplayName() {
        return this.displayName;
    }
}
