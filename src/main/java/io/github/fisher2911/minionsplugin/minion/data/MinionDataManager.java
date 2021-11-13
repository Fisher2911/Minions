package io.github.fisher2911.minionsplugin.minion.data;

import io.github.fisher2911.fishcore.configurate.ConfigurateException;
import io.github.fisher2911.fishcore.configurate.yaml.YamlConfigurationLoader;
import io.github.fisher2911.fishcore.world.Position;
import io.github.fisher2911.minionsplugin.MinionsPlugin;
import io.github.fisher2911.minionsplugin.config.serializer.MinionDataSerializer;
import io.github.fisher2911.minionsplugin.minion.builder.MinionBuilder;
import io.github.fisher2911.minionsplugin.minion.types.BaseMinion;

import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class MinionDataManager {

    private final MinionsPlugin plugin;
    private final Map<String, BaseMinionData> minionDataMap;

    public MinionDataManager(final MinionsPlugin plugin, final Map<String, BaseMinionData> minionDataMap) {
        this.plugin = plugin;
        this.minionDataMap = minionDataMap;
    }

    public Optional<BaseMinionData> get(final String id) {
        return Optional.ofNullable(this.minionDataMap.get(id));
    }

    public void remove(final String id) {
        this.minionDataMap.remove(id);
    }

    public Optional<BaseMinion<?>> newMinion(
            final String minionDataId,
            final long id,
            final UUID owner,
            final float foodLevel,
            final Position position) {
        final Optional<BaseMinionData> optionalMinionData = this.get(minionDataId);

        if (optionalMinionData.isEmpty()) {
            return Optional.empty();
        }

        return Optional.of(MinionBuilder.builder(this.plugin).
                minionData(optionalMinionData.get().
                        toMinionData(id,
                                owner,
                                foodLevel,
                                this.plugin)).
                position(position).
                build());
    }

    public void load() {
        final File parent = Path.of(this.plugin.getDataFolder().getPath(), "minions").toFile();

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
                                    build.register(BaseMinionData.class, MinionDataSerializer.INSTANCE)))
                    .build();

            try {
                final BaseMinionData baseMinionData = loader.load().get(BaseMinionData.class);

                if (baseMinionData == null) {
                    continue;
                }

                this.minionDataMap.put(baseMinionData.getNamedId(), baseMinionData);
            } catch (final ConfigurateException exception) {
                exception.printStackTrace();
            }
        }
    }

    public List<String> listIds() {
        return new ArrayList<>(this.minionDataMap.keySet());
    }
}
