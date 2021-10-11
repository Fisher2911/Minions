package io.github.fisher2911.keys;

import io.github.fisher2911.MinionsPlugin;
import org.bukkit.NamespacedKey;

public class Keys {

    private static final MinionsPlugin plugin;

    static {
        plugin = MinionsPlugin.getPlugin(MinionsPlugin.class);
    }

    public static final NamespacedKey MINION_KEY = new NamespacedKey(plugin, "minion-id");

}
