package io.github.fisher2911.minionsplugin.permission;

import io.github.fisher2911.fishcore.manager.Manager;
import io.github.fisher2911.minionsplugin.minion.types.BaseMinion;

public class PermissionManager extends Manager<String, Permission> {

    // todo
    public boolean hasPermission(final BaseMinion<?> baseMinion) {
        return true;
    }

}
