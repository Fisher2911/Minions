package io.github.fisher2911.minionsplugin.config;

import io.github.fisher2911.fishcore.config.serializer.ItemSerializer;
import io.github.fisher2911.fishcore.configurate.ConfigurateException;
import io.github.fisher2911.fishcore.configurate.yaml.YamlConfigurationLoader;
import io.github.fisher2911.fishcore.message.MessageHandler;
import io.github.fisher2911.fishcore.message.MessageHandlerRegistry;
import io.github.fisher2911.minionsplugin.MinionsPlugin;
import io.github.fisher2911.minionsplugin.config.serializer.GuiDataSerializer;
import io.github.fisher2911.minionsplugin.gui.GuiData;
import io.github.fisher2911.minionsplugin.gui.GuiManager;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.nio.file.Path;

public class GuiLoader {

    private final MinionsPlugin plugin;
    private final MessageHandler messageHandler;
    private final GuiManager guiManager;

    public GuiLoader(final MinionsPlugin plugin) {
        this.plugin = plugin;
        this.messageHandler = MessageHandlerRegistry.REGISTRY.get(MinionsPlugin.class);
        this.guiManager = this.plugin.getGuiManager();
    }

    public void loadAll() {
        final File parent = Path.of(this.plugin.getDataFolder().getPath(), "menus").toFile();

        if (!parent.exists()) {
            parent.mkdirs();
        }

        final File[] files = parent.listFiles();

        if (files == null) {
            return;
        }

        for (final File file : files) {
            this.load(file);
        }
    }

    // Loads a minion gui and registers it
    private void load(final File file) {

        if (!file.exists()) {
            return;
        }

        final YamlConfigurationLoader loader = YamlConfigurationLoader.
                builder().
                path(Path.of(file.getPath())).
                 defaultOptions(opts ->
                         opts.serializers(build -> {
                             build.register(GuiData.class, GuiDataSerializer.INSTANCE);
                             build.register(ItemStack.class, ItemSerializer.INSTANCE);
                         }))
                .build();
        try {

            final var source = loader.load();

            final GuiData guiData = source.get(GuiData.class);

            this.plugin.getLogger().severe("Loaded gui: " + file.getName());

            this.guiManager.registerGui(file.getName().replace(".yml", ""), guiData);
        } catch (ConfigurateException e) {
            e.printStackTrace();
        }

    }
}
