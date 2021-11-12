package io.github.fisher2911.minionsplugin.permission;

import org.jetbrains.annotations.Unmodifiable;

import java.util.Collections;
import java.util.Map;

public class MinionPermissions {

    private final Map<String, Boolean> permissionMap;

    public MinionPermissions(final Map<String, Boolean> permissionMap) {
        this.permissionMap = permissionMap;
    }

    @Unmodifiable
    public Map<String, Boolean> getPermissionMap() {
        return Collections.unmodifiableMap(this.permissionMap);
    }

    public boolean hasPermission(final String permission, final boolean defaultValue) {
        return this.permissionMap.getOrDefault(permission, defaultValue);
    }

    public boolean hasPermission(final String permission) {
        return this.hasPermission(permission, false);
    }

    public void setPermission(final String permission, final boolean value) {
        this.permissionMap.put(permission, value);
    }

    public void swapPermissionValue(final String permission) {
        this.permissionMap.put(
                permission,
                !this.hasPermission(permission, false));
    }
}
