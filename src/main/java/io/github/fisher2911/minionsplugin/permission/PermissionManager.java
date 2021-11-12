package io.github.fisher2911.minionsplugin.permission;

import io.github.fisher2911.fishcore.manager.Manager;
import io.github.fisher2911.minionsplugin.minion.types.BaseMinion;

import java.util.Map;

public class PermissionManager extends Manager<String, MinionPermission> {

    /**
     * Key is minion id
     */
    private final Map<Long, MinionPermissionsGroup> permissionsGroups;

    public PermissionManager(final Map<Long, MinionPermissionsGroup> permissionsGroups) {
        this.permissionsGroups = permissionsGroups;
    }

    public boolean hasPermission(final BaseMinion<?> baseMinion) {
        return true;
    }

}
