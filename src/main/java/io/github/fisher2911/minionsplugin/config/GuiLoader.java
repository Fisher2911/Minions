package io.github.fisher2911.minionsplugin.config;

import dev.triumphteam.gui.guis.GuiItem;
import io.github.fisher2911.fishcore.message.MessageHandler;
import io.github.fisher2911.fishcore.message.MessageHandlerRegistry;
import io.github.fisher2911.fishcore.util.helper.StringUtils;
import io.github.fisher2911.minionsplugin.MinionsPlugin;
import io.github.fisher2911.minionsplugin.config.serializer.GuiItemSerializer;
import io.github.fisher2911.minionsplugin.gui.GuiManager;
import io.github.fisher2911.minionsplugin.gui.SimpleMinionGui;
import org.spongepowered.configurate.ConfigurateException;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.yaml.YamlConfigurationLoader;

import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GuiLoader {

    private final MinionsPlugin plugin;
    private final MessageHandler messageHandler;

    public GuiLoader(final MinionsPlugin plugin) {
        this.plugin = plugin;
        this.messageHandler = MessageHandlerRegistry.REGISTRY.get(MinionsPlugin.class);
    }

    // Loads a minion gui and registers it
    public void load(final String... path) {

        final File file = Path.of(this.plugin.getDataFolder().getPath(), path).toFile();

        if (!file.exists()) {
            file.getParentFile().mkdirs();
            final String first = path[0];

            if (path.length <= 1) {
                this.plugin.saveResource(first, false);
            } else {
                final String[] rest = new String[path.length - 1];
                System.arraycopy(path, 1, rest, 0, path.length - 1);
                this.plugin.saveResource(Path.of(first, rest).toString(), false);
            }
        }

        final YamlConfigurationLoader loader = YamlConfigurationLoader.
                builder().
                path(Path.of(this.plugin.getDataFolder().getPath(), path)).
                 defaultOptions(opts ->
                         opts.serializers(build -> build.register(GuiItem.class, GuiItemSerializer.INSTANCE)))
                .build();
        try {
            final ConfigurationNode root = loader.load();
            final ConfigurationNode titleNode = root.node("title");
            final ConfigurationNode rowsNode = root.node("rows");
            final ConfigurationNode borderNode = root.node("border-items");
            final ConfigurationNode guiItemsNode = root.node("items");

            final String title = titleNode.getString();
            final int rows = rowsNode.getInt();
            final List<GuiItem> borderItems = new ArrayList<>();

            final Map<Object, ? extends ConfigurationNode> borderItemsNodeMap = borderNode.childrenMap();

            if (!borderItemsNodeMap.isEmpty()) {
                for (ConfigurationNode configurationNode : borderItemsNodeMap.values()) {
                    borderItems.add(configurationNode.get(GuiItem.class));
                }
            }

            final Map<Object, ? extends ConfigurationNode> map = guiItemsNode.childrenMap();

            final Map<Integer, GuiItem> guiItemMap = new HashMap<>();

            for (final Object key : map.keySet()) {
                final ConfigurationNode itemNode = guiItemsNode.node(key);
                final GuiItem guiItem = itemNode.get(GuiItem.class);

                try {
                    guiItemMap.put(Integer.parseInt(key.toString()), guiItem);
                } catch (final NumberFormatException exception) {
                    exception.printStackTrace();
                }
            }

            final SimpleMinionGui gui = new SimpleMinionGui(
                    StringUtils.parseStringToString(title),
                    rows, borderItems, guiItemMap);

            GuiManager.registerGui(file.getName().replace(".yml", ""), gui);
        } catch (ConfigurateException e) {
            e.printStackTrace();
        }

    }
}
