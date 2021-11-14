package io.github.fisher2911.minionsplugin.permission;

import io.github.fisher2911.fishcore.util.builder.ItemBuilder;
import io.github.fisher2911.minionsplugin.gui.item.TypeItem;
import org.bukkit.Material;
import org.jetbrains.annotations.Unmodifiable;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

public class RegisteredPermissions {

    private static final Map<String, MinionPermission> registered = new LinkedHashMap<>();

    // todo - remove
    static {
        for (int i = 0; i < 2; i++) {
            register(new MinionPermission(
                    "test" + i,
                    "test" + i,
                    new TypeItem(
                            "permission",
                    "test",
                    ItemBuilder.from(Material.BUCKET).
                            name("Test permission " + i).
                            build())));
        }
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
