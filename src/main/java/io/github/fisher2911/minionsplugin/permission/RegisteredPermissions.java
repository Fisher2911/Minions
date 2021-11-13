package io.github.fisher2911.minionsplugin.permission;

import io.github.fisher2911.fishcore.util.builder.ItemBuilder;
import org.bukkit.Material;
import org.jetbrains.annotations.Unmodifiable;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class RegisteredPermissions {

    private static final Map<String, MinionPermission> registered = new HashMap<>();

    // todo - remove
    static {
        register(new MinionPermission(
                "test",
                "test",
                ItemBuilder.from(Material.BUCKET).
                        name("Test permission").
                        build()
        ));
    }

    public static void register(final MinionPermission minionPermission) {
        registered.put(minionPermission.getId(), minionPermission);
    }

    public static MinionPermission get(final String id) {
        return registered.get(id);
    }

    @Unmodifiable
    public static Map<String, MinionPermission> getAll() {
        return Collections.unmodifiableMap(registered);
    }
}
