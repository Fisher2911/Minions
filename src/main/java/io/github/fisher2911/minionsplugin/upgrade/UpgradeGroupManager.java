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

import java.nio.file.Path;

public class UpgradeGroupManager extends Manager<String, UpgradeGroup> {

    private final MinionsPlugin plugin;

    public UpgradeGroupManager(final MinionsPlugin plugin) {
        this.plugin = plugin;
    }

    public void load(final String... path) {
        final YamlConfigurationLoader loader = YamlConfigurationLoader.
                builder().
                path(Path.of(this.plugin.getDataFolder().getPath(), path)).
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
