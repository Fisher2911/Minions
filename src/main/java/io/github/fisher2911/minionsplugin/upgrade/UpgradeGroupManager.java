package io.github.fisher2911.minionsplugin.upgrade;

import io.github.fisher2911.fishcore.config.serializer.ItemSerializer;
import io.github.fisher2911.fishcore.configurate.ConfigurateException;
import io.github.fisher2911.fishcore.configurate.yaml.YamlConfigurationLoader;
import io.github.fisher2911.fishcore.manager.Manager;
import io.github.fisher2911.minionsplugin.MinionsPlugin;
import io.github.fisher2911.minionsplugin.config.serializer.GuiDataSerializer;
import io.github.fisher2911.minionsplugin.config.serializer.UpgradeGroupSerializer;
import io.github.fisher2911.minionsplugin.gui.GuiData;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.nio.file.Path;
import java.util.Map;

public class UpgradeGroupManager extends Manager<String, UpgradeGroup> {

    private final MinionsPlugin plugin;

    private UpgradeGroupManager(final MinionsPlugin plugin) {
        super();
        this.plugin = plugin;
    }

    public UpgradeGroupManager(final Map<String, UpgradeGroup> map, final MinionsPlugin plugin) {
        super(map);
        this.plugin = plugin;
    }

    public void loadAll() {
        final File file = Path.of(this.plugin.getDataFolder().getPath(),
                "upgrades").toFile();

        if (!file.exists()) {
            file.mkdirs();
        }

        final File[] files = file.listFiles();

        if (files == null) {
            this.plugin.logger().error("No upgrade files");
            return;
        }

        for (final File f : files) {
            this.load(f);
        }
    }

    private void load(final File file) {
        this.plugin.logger().info("Loaded upgrades: " + file.getName());
        final YamlConfigurationLoader loader = YamlConfigurationLoader.
                builder().
                path(file.toPath()).
                defaultOptions(opts ->
                        opts.serializers(build -> {
                            build.register(GuiData.class, GuiDataSerializer.INSTANCE);
                            build.register(ItemStack.class, ItemSerializer.INSTANCE);
                            build.register(UpgradeGroup.class, UpgradeGroupSerializer.INSTANCE);
                        }))
                .build();
        try {

            final var source = loader.load();

            final UpgradeGroup upgradeGroup = source.node("upgrades").get(UpgradeGroup.class);
            this.add(upgradeGroup);
        } catch (ConfigurateException e) {
            e.printStackTrace();
        }
    }
}
