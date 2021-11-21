package io.github.fisher2911.minionsplugin.permission;

import io.github.fisher2911.fishcore.config.serializer.ItemSerializer;
import io.github.fisher2911.fishcore.configurate.ConfigurateException;
import io.github.fisher2911.fishcore.configurate.ConfigurationNode;
import io.github.fisher2911.fishcore.configurate.yaml.YamlConfigurationLoader;
import io.github.fisher2911.minionsplugin.MinionsPlugin;
import io.github.fisher2911.minionsplugin.config.serializer.PermissionSerializer;
import io.github.fisher2911.minionsplugin.gui.item.TypeItem;
import org.jetbrains.annotations.Unmodifiable;

import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;

public class PermissionManager {

    private static final MinionsPlugin plugin;
    private static final PermissionManager INSTANCE;

    private final Map<String, Boolean> defaultPermissions = new HashMap<>();

    static {
        plugin = MinionsPlugin.getPlugin(MinionsPlugin.class);
        INSTANCE = new PermissionManager();
        INSTANCE.load();
    }

    public static PermissionManager getInstance() {
        return INSTANCE;
    }

    private MinionPermissionsGroup defaultGroup;

    private TypeItem defaultGroupDisplayItem;
    private String defaultGroupName;
    private String defaultGroupId;

    private final Map<String, MinionPermission> registered = new LinkedHashMap<>();

    public void register(final MinionPermission minionPermission) {
        this.registered.put(minionPermission.getId(), minionPermission);
    }

    public MinionPermission get(final String id) {
        return this.registered.get(id);
    }

    @Unmodifiable
    public Map<String, MinionPermission> getAll() {
        return Collections.unmodifiableMap(this.registered);
    }

    private PermissionManager() {}

    private void load() {

        final File file = Path.of(plugin.getDataFolder().getPath(), "permissions", "permissions.yml").toFile();

        if (!file.exists()) {
            file.getParentFile().mkdirs();

            plugin.saveResource(new File("permissions", "permissions.yml").getPath(), false);
        }

        final YamlConfigurationLoader loader = YamlConfigurationLoader.
                builder().
                path(file.toPath()).
                defaultOptions(opts ->
                        opts.serializers(build ->
                                build.register(MinionPermission.class, PermissionSerializer.INSTANCE)))
                .build();

        try {

            final ConfigurationNode source = loader.load();

            this.defaultGroupId = source.node("default-group-id").getString();
            this.defaultGroupName = source.node("default-group-name").getString();

            this.defaultGroupDisplayItem =
                    new TypeItem(
                    TypeItem.Types.PERMISSION_GROUP,
                    this.defaultGroupId,
                    ItemSerializer.INSTANCE.deserialize(
                    ItemSerializer.class, source.node("default-group-display-item"))
            );

            final var childrenMap = source.node("permissions").childrenMap();

            for (final var node : childrenMap.values()) {
                this.register(node.get(MinionPermission.class));
            }

        } catch (final ConfigurateException exception) {
            exception.printStackTrace();
        }


        for (final var entry : this.getAll().entrySet()) {
            this.defaultPermissions.put(entry.getValue().getId(), false);
        }

        this.defaultGroup = new MinionPermissionsGroup(
                this.defaultGroupId,
                0,
                this.defaultGroupName,
                this.defaultGroupDisplayItem,
                // todo - change to all
                MinionPermissionsGroup.Mode.IN_GROUP,
                new HashSet<>(),
                new ArrayList<>(),
                new MinionPermissions(new HashMap<>(this.defaultPermissions))
        );

    }

    public Map<String, Boolean> getDefaultPermissions() {
        return new HashMap<>(this.defaultPermissions);
    }

    public MinionPermissionsGroup getDefaultGroup() {
        return this.defaultGroup;
    }

    public TypeItem getDefaultGroupDisplayItem() {
        return this.defaultGroupDisplayItem;
    }

    public String getDefaultGroupName() {
        return this.defaultGroupName;
    }

    public String getDefaultGroupId() {
        return this.defaultGroupId;
    }
}
