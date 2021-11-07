package io.github.fisher2911.minionsplugin.permission;

import io.github.fisher2911.fishcore.util.helper.IdHolder;

public class Permission implements IdHolder<String> {

    private final String id;
    private final String displayName;

    public Permission(final String id, final String displayName) {
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
