package io.github.fisher2911.minionsplugin.minion.food;

import io.github.fisher2911.fishcore.configurate.ConfigurateException;
import io.github.fisher2911.fishcore.configurate.yaml.YamlConfigurationLoader;
import io.github.fisher2911.fishcore.manager.Manager;
import io.github.fisher2911.minionsplugin.MinionsPlugin;
import io.github.fisher2911.minionsplugin.config.serializer.FoodGroupSerializer;

import java.io.File;
import java.nio.file.Path;
import java.util.Map;

public class FoodManager extends Manager<String, FoodGroup> {

    private final MinionsPlugin plugin;

    private FoodManager(final MinionsPlugin plugin) {
        this.plugin = plugin;
    }

    public FoodManager(final Map<String, FoodGroup> map,
                       final MinionsPlugin plugin) {
        super(map);
        this.plugin = plugin;
    }

    public void load() {
        final File parent = Path.of(this.plugin.getDataFolder().getPath(), "food").toFile();

        if (!parent.exists()) {
            parent.mkdirs();
        }

        final File[] files = parent.listFiles();

        if (files == null) {
            return;
        }

        for (final File file : files) {
            final YamlConfigurationLoader loader = YamlConfigurationLoader.
                    builder().
                    path(file.toPath()).
                    defaultOptions(opts ->
                            opts.serializers(build ->
                                    build.register(FoodGroup.class, FoodGroupSerializer.INSTANCE)))
                    .build();

            try {
                final FoodGroup foodGroup = loader.load().get(FoodGroup.class);
                if (foodGroup == null || foodGroup.getId() == null) {
                    return;
                }

                this.add(foodGroup);
            } catch (final ConfigurateException exception) {
                exception.printStackTrace();
            }
        }
    }
}
