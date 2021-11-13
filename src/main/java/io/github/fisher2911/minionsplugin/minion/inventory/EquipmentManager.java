package io.github.fisher2911.minionsplugin.minion.inventory;

import io.github.fisher2911.fishcore.configurate.ConfigurateException;
import io.github.fisher2911.fishcore.configurate.yaml.YamlConfigurationLoader;
import io.github.fisher2911.fishcore.manager.Manager;
import io.github.fisher2911.minionsplugin.MinionsPlugin;
import io.github.fisher2911.minionsplugin.config.serializer.EquipmentSerializer;

import java.io.File;
import java.nio.file.Path;
import java.util.Map;

public class EquipmentManager extends Manager<String, Equipment> {

    private final MinionsPlugin plugin;

    private EquipmentManager(final MinionsPlugin plugin) {
        this.plugin = plugin;
    }

    public EquipmentManager(final Map<String, Equipment> map, final MinionsPlugin plugin) {
        super(map);
        this.plugin = plugin;
    }

    public void load() {
        final File parent = Path.of(this.plugin.getDataFolder().getPath(), "equipment").toFile();

        if (!parent.exists()) {
            parent.mkdirs();
        }

        final File[] files = parent.listFiles();

        if (files == null) {
            this.plugin.logger().error("No files found");
            return;
        }

        for (final File file : files) {
            this.plugin.logger().error("Loaded file: " + file.getName());
            final YamlConfigurationLoader loader = YamlConfigurationLoader.
                    builder().
                    path(file.toPath()).
                    defaultOptions(opts ->
                            opts.serializers(build ->
                                    build.register(Equipment.class, EquipmentSerializer.INSTANCE)))
                    .build();

            try {
                final Equipment equipment = loader.load().get(Equipment.class);

                if (equipment == null) {
                    continue;
                }

                this.add(equipment);
            } catch (final ConfigurateException exception) {
                exception.printStackTrace();
            }
        }
    }
}
