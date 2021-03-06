package io.github.fisher2911.minionsplugin.keys;

import io.github.fisher2911.minionsplugin.MinionsPlugin;
import org.bukkit.NamespacedKey;

public class Keys {

    private static final MinionsPlugin plugin;

    static {
        plugin = MinionsPlugin.getPlugin(MinionsPlugin.class);
    }

    public static final NamespacedKey MINION_ID_KEY = new NamespacedKey(plugin, "minion-id");
    public static final NamespacedKey MINION_TYPE_KEY = new NamespacedKey(plugin, "minion-type");

}
