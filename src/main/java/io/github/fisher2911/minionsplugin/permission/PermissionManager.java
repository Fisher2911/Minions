package io.github.fisher2911.minionsplugin.permission;

import io.github.fisher2911.fishcore.manager.Manager;
import io.github.fisher2911.minionsplugin.gui.item.TypeItem;
import io.github.fisher2911.minionsplugin.minion.types.BaseMinion;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class PermissionManager extends Manager<String, MinionPermissionsGroup> {

    private MinionPermissionsGroup defaultGroup;

    private PermissionManager() {}

    public PermissionManager(final Map<String, MinionPermissionsGroup> map) {
        super(map);
    }

    public boolean hasPermission(final BaseMinion<?> baseMinion) {
        return true;
    }

    // todo
    public void load() {
        this.defaultGroup = new MinionPermissionsGroup(
                "default",
                1,
                "Default Permissions",
                new TypeItem("permission", "default", new ItemStack(Material.GOLD_BLOCK, 1)),
                MinionPermissionsGroup.Mode.NOT_SPECIFIED,
                new HashSet<>(),
                new ArrayList<>(),
                new MinionPermissions(new HashMap<>())
        );
    }

    public MinionPermissionsGroup getDefaultGroup() {
        return this.defaultGroup;
    }
}
